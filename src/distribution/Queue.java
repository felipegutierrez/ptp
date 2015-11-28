package distribution;

import java.util.ArrayList;

public class Queue {
	private ArrayList<Message> queue = new ArrayList<Message>();

	public Queue() {
	}

	/**
	 * The method add a message into the queue
	 * 
	 * @param msg
	 */
	public void enqueue(Message msg) {
		this.queue.add(msg);
	}

	/**
	 * This method only get the message and perform a dequeue
	 * 
	 * @return
	 */
	public Message dequeue() {
		Message t;

		t = this.queue.get(0);
		this.queue.remove(0);

		return t;
	}

	/**
	 * This method only get the message but not perform a dequeue
	 * 
	 * @return
	 */
	public Message getMessage() {
		Message t;

		t = this.queue.get(0);

		return t;
	}

	public int queueSize() {
		return this.queue.size();
	}
}
