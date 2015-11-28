package elements;

import java.io.IOException;

import distribution.QueueManagerProxy;

public class Publisher {
	QueueManagerProxy queueManagerProxy;

	public Publisher(String queueName) {
		queueManagerProxy = new QueueManagerProxy(queueName);
	}

	public void send(String message) {
		try {
			queueManagerProxy.send(message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Publisher publisher = new Publisher("queue");
		publisher.send("message-01");
	}
}