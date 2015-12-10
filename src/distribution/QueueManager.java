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

	public QueueManager(int port) throws UnknownHostException {
		this.setHost(InetAddress.getLocalHost().getHostName());
		this.setPort(port);
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

			switch (requestPacketMarshalled.getPacketHeader().getOperation()) {

			// put something in a queue
			case SEND:
				queueName = new String(requestPacketMarshalled.getPacketBody().getMessage().getHead().getDestination());
				Message message = requestPacketMarshalled.getPacketBody().getMessage();
				if (queues.containsKey(queueName)) {
					queues.get(queueName).enqueue(message);
				} else {
					queues.put(queueName, new Queue());
					queues.get(queueName).enqueue(message);
				}
				break;

			// recebe a mensagem e só remove agora se não for transacional
			case RECEIVE:
				queueName = new String(requestPacketMarshalled.getPacketBody().getMessage().getHead().getDestination());
				if (queues.containsKey(queueName) && queues.get(queueName).queueSize() > 0) {

					// verifica se é transacional
					Message messageReceived = queues.get(queueName).getMessage();
					if (messageReceived.getHead().isTransactional()) {

						replyPacketUnmarshalled.setHeader(new ReplyPacketHeader("write", true));
						replyPacketUnmarshalled.setBody(new ReplyPacketBody(null, messageReceived));
						replyPacketUnmarshalled.setReply(Reply.EXECUTING);

					} else {
						// a mensagem não é transacional
						String body = queues.get(queueName).dequeue().getBody().getBody();

						replyPacketUnmarshalled.setHeader(new ReplyPacketHeader("write", false));
						replyPacketUnmarshalled.setBody(new ReplyPacketBody(null, messageReceived));
						replyPacketUnmarshalled.setReply(Reply.EXECUTING);
					}
				} else {
					replyPacketUnmarshalled.setReply(Reply.ERROR);
				}

				replyPacketMarshalled = marshaller.marshall((Object) replyPacketUnmarshalled); // TODO
				srh.send(replyPacketMarshalled);
				break;

			// tira da fila
			case DEQUEUE:
				queueName = new String(requestPacketMarshalled.getPacketBody().getMessage().getHead().getDestination());
				if (queues.containsKey(queueName) && queues.get(queueName).queueSize() > 0) {

					Message messageReceived = queues.get(queueName).dequeue();

					replyPacketUnmarshalled.setHeader(new ReplyPacketHeader("write", false));
					replyPacketUnmarshalled.setBody(new ReplyPacketBody(null, messageReceived));
					replyPacketUnmarshalled.setReply(Reply.DEQUEUE);
					replyPacketMarshalled = marshaller.marshall((Object) replyPacketUnmarshalled); // TODO

					srh.send(replyPacketMarshalled);
				}
				break;
			default:
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
