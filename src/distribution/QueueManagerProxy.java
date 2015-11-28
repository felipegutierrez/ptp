package distribution;

import java.io.IOException;
import java.util.ArrayList;

import infrastructure.ClientRequestHandler;

public class QueueManagerProxy implements IQueueManager {

	private String queueName = null;

	public QueueManagerProxy(String queueName) {
		this.setQueueName(queueName);
	}

	@Override
	public void send(String m, Boolean transactional) throws IOException, InterruptedException {
		// configure
		ClientRequestHandler crh = new ClientRequestHandler("localhost", 1313, false);
		Marshaller marshaller = new Marshaller();
		RequestPacket packet = new RequestPacket();
		Message message = new Message();

		// configure message
		message.setHeader(new MessageHeader(this.queueName, transactional));
		message.setBody(new MessageBody(m));

		// configure packet
		RequestPacketBody packetBody = new RequestPacketBody();
		ArrayList<Object> parameters = new ArrayList<Object>(0);

		packetBody.setParameters(parameters);
		packetBody.setMessage(message);
		packet.setPacketHeader(new RequestPacketHeader("send"));
		packet.setPacketBody(packetBody);

		// send request
		crh.send(marshaller.marshall((Object) packet));

		return;
	}

	@Override
	public String receive(Boolean transactional) throws IOException, InterruptedException, ClassNotFoundException {
		ClientRequestHandler crh = new ClientRequestHandler("localhost", 1313, true);
		Marshaller marshaller = new Marshaller();
		RequestPacket requestPacket = new RequestPacket();
		ReplyPacket marshalledReplyPacket = new ReplyPacket();
		byte[] unmarshalledReplyPacket = new byte[1024];
		Message message = new Message();

		// configure message
		message.setHeader(new MessageHeader(this.queueName, transactional));
		message.setBody(new MessageBody("messageBody"));

		// configure packet
		RequestPacketBody packetBody = new RequestPacketBody();
		ArrayList<Object> parameters = new ArrayList<Object>(0);
		packetBody.setParameters(parameters);
		packetBody.setMessage(message);
		requestPacket.setPacketHeader(new RequestPacketHeader("receive"));
		requestPacket.setPacketBody(packetBody);

		// send request
		crh.send(marshaller.marshall((Object) requestPacket));

		// receive reply
		unmarshalledReplyPacket = crh.receive();
		marshalledReplyPacket = (ReplyPacket) marshaller.unmarshall(unmarshalledReplyPacket);

		return marshalledReplyPacket.getReply(); // TODO
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
