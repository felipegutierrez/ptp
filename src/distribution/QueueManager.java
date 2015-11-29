package distribution;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import infrastructure.ServerRequestHandler;

public class QueueManager {

	private String host;

	private int port;

	private Map<String, Queue> queues = new HashMap<String, Queue>();

	private TransactionManager transactionManager;

	public QueueManager(int port) throws UnknownHostException {
		this.setHost(InetAddress.getLocalHost().getHostName());
		this.setPort(port);
		this.transactionManager = new TransactionManager();
	}

	public void run() throws IOException, Throwable {
		byte[] requestPacketUnmarshalled = new byte[1024];
		byte[] replyPacketMarshalled = new byte[1024];
		RequestPacket requestPacketMarshalled = new RequestPacket();
		ReplyPacket replyPacketUnmarshalled = new ReplyPacket();
		String queueName = null;

		Marshaller marshaller = new Marshaller();
		ServerRequestHandler srh = new ServerRequestHandler(this.port);

		while (true) {

			requestPacketUnmarshalled = srh.receive();
			requestPacketMarshalled = (RequestPacket) marshaller.unmarshall(requestPacketUnmarshalled);

			switch (requestPacketMarshalled.getPacketHeader().getOperation().trim()) {

			// put something in a queue
			case "send":
				queueName = new String(requestPacketMarshalled.getPacketBody().getMessage().getHead().getDestination());
				Message message = requestPacketMarshalled.getPacketBody().getMessage();
				if (queues.containsKey(queueName)) {
					queues.get(queueName).enqueue(message);
				} else {
					queues.put(queueName, new Queue());
					queues.get(queueName).enqueue(message);
				}
				break;

			// remove a message from a queue
			case "receive":
				queueName = new String(requestPacketMarshalled.getPacketBody().getMessage().getHead().getDestination());
				if (queues.containsKey(queueName) && queues.get(queueName).queueSize() > 0) {

					// verifica se é transacional
					Message messageReceived = queues.get(queueName).getMessage();
					if (messageReceived.getHead().isTransactional()) {

						Integer transactionKey = transactionManager.createNewTransaction();
						Transaction transaction = transactionManager.getTransaction(transactionKey);

						// analisa o header para verificar se pode realizar o
						// dequeue

						Boolean startTransaction = transactionManager.startTransaction(transactionKey);
						if (startTransaction) {
							Boolean voteRequestForAll = transactionManager.voteRequestForAll(transactionKey);
							if (voteRequestForAll) {
								// TODO: se recebeu VOTE_COMMIT_ALL escreve
								// GLOBAL_COMMIT
								Boolean globalCommitForAll = transactionManager.globalCommitForAll(transactionKey);
								if (globalCommitForAll) {

									// realiza o dequeue pois a transação foi
									// completada com sucesso
									String body = queues.get(queueName).dequeue().getBody().getBody();
									replyPacketUnmarshalled.setReply(body);
								} else {
									// não consegui comitar para algum dos
									// participantes
									replyPacketUnmarshalled
											.setReply("erro no globalCommitForAll da transação " + transaction);
								}
							} else {
								// não conseguiu fazer o request para algum dos
								// participantes
								replyPacketUnmarshalled
										.setReply("erro no voteRequestForAll da transação " + transaction);
							}
						} else {
							// não conseguiu iniciar a transação
							replyPacketUnmarshalled.setReply("erro no start da transação " + transaction);
						}
					}

				} else {
					replyPacketUnmarshalled.setReply("");
				}

				replyPacketMarshalled = marshaller.marshall((Object) replyPacketUnmarshalled); // TODO
				srh.send(replyPacketMarshalled);
				break;
			}
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
