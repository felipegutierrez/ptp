package elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import distribution.MessageResource;
import distribution.QueueManagerProxy;
import distribution.ResourcePermissions;

public class Publisher {

	QueueManagerProxy queueManagerProxy;

	public Publisher(String queueName) {
		queueManagerProxy = new QueueManagerProxy(queueName);
	}

	public void send(String message, List<MessageResource> messageFiles, Boolean transactional) {
		try {
			queueManagerProxy.send(message, messageFiles, transactional);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Publisher publisher = new Publisher("queue");
		List<MessageResource> messageResources = new ArrayList<MessageResource>();

		MessageResource messageFile = new MessageResource(new File("/home/felipe/Temp/2FC/teste00.txt"),
				"escrevendo 000000", ResourcePermissions.READ_AND_WRITE);
		messageResources.add(messageFile);
		messageFile = new MessageResource(new File("/home/felipe/Temp/2FC/teste11.txt"), "escrevendo 111111",
				ResourcePermissions.READ_AND_WRITE);
		messageResources.add(messageFile);
		messageFile = new MessageResource(new File("/home/felipe/Temp/2FC/teste22.txt"), "escrevendo 222222",
				ResourcePermissions.READ_AND_WRITE);
		messageResources.add(messageFile);

		publisher.send("message-01", messageResources, true);
	}
}
