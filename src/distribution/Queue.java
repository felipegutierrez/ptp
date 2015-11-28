package distribution;

import java.util.ArrayList;

public class Queue {
	private ArrayList<Message> queue = new ArrayList<Message>();

	public Queue(){};
	
	public void enqueue(Message msg){
		this.queue.add(msg);
	}
	
	public Message dequeue(){
		Message t;
		
		t = this.queue.get(0);
		this.queue.remove(0);
		
		return t;
	}
	
	public int queueSize(){
		return this.queue.size();
	}
}
