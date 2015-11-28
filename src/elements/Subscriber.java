package elements;

import java.io.IOException;

import distribution.QueueManagerProxy;

public class Subscriber {

	QueueManagerProxy queueManagerProxy;

	public Subscriber(String queueName) {
		queueManagerProxy = new QueueManagerProxy(queueName);
	}

	public void receive(Boolean transactional) {
		String receive = null;
		try {
			receive = queueManagerProxy.receive(transactional);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("receive: " + receive);
	}

	public void stop() {
	}

	public static void main(String[] args) {
		Subscriber subscriber = new Subscriber("queue");
		subscriber.receive(true);
	}
}
