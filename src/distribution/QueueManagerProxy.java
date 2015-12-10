package distribution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import infrastructure.ClientRequestHandler;

public class QueueManagerProxy implements IQueueManager {

	private String queueName = null;

	private TransactionManager transactionManager;

	public QueueManagerProxy(String queueName) {
		this.setQueueName(queueName);
		this.transactionManager = new TransactionManager();
	}

	@Override
	public void send(String m, List<MessageResource> messageFiles, Boolean transactional)
			throws IOException, InterruptedException {
		// configure
		ClientRequestHandler crh = new ClientRequestHandler("localhost", 1313, false);
		Marshaller marshaller = new Marshaller();
		RequestPacket packet = new RequestPacket();
		Message message = new Message();

		// configure message
		message.setHeader(new MessageHeader(this.queueName, transactional));
		message.setBody(new MessageBody(m));
		for (MessageResource messageFile : messageFiles) {
			message.getBody().addMessageResources(messageFile);
		}

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

		ReplyPacketHeader header = marshalledReplyPacket.getHeader();
		ReplyPacketBody body = marshalledReplyPacket.getBody();
		if (header.isTransactional()) {

			List<MessageResource> messageResources = body.getMessage().getBody().getMessageResources();

			// inicia uma transação
			Integer transactionKey = transactionManager.createNewTransaction();
			Transaction transaction = transactionManager.getTransaction(transactionKey);

			Boolean startTransaction = transaction.startTransaction(messageResources);
			if (startTransaction) {
				Boolean voteRequestForAll = transaction.voteRequestForAll(messageResources);
				if (voteRequestForAll) {
					Boolean globalCommitForAll = transaction.globalCommitForAll(messageResources);
					if (globalCommitForAll) {
						// passou por todas as fases
						requestPacket.getPacketHeader().setOperation("dequeue");
						crh.send(marshaller.marshall((Object) requestPacket));
						unmarshalledReplyPacket = crh.receive();
						
						marshalledReplyPacket.setReply("sucesso");
					} else {

					}
				} else {

				}
			} else {

			}
		} else {

		}

		return marshalledReplyPacket.getReply(); // TODO
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
