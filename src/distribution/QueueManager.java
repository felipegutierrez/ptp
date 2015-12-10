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

						replyPacketUnmarshalled.setHeader(new ReplyPacketHeader("write", true));
						replyPacketUnmarshalled.setBody(new ReplyPacketBody(null, messageReceived));
						replyPacketUnmarshalled.setReply("executando");

					} else {
						// a mensagem não é transacional
						String body = queues.get(queueName).dequeue().getBody().getBody();
						replyPacketUnmarshalled.setReply(body);
					}
				} else {
					replyPacketUnmarshalled.setReply("");
				}

				replyPacketMarshalled = marshaller.marshall((Object) replyPacketUnmarshalled); // TODO
				srh.send(replyPacketMarshalled);
				break;

			// tira da fila
			case "dequeue":
				queueName = new String(requestPacketMarshalled.getPacketBody().getMessage().getHead().getDestination());
				if (queues.containsKey(queueName) && queues.get(queueName).queueSize() > 0) {
					String body = queues.get(queueName).dequeue().getBody().getBody();
					replyPacketUnmarshalled.setReply(body);
					replyPacketMarshalled = marshaller.marshall((Object) replyPacketUnmarshalled); // TODO
					srh.send(replyPacketMarshalled);
				}
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
