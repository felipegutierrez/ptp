package distribution;

import java.io.Serializable;

public class MessageHeader implements Serializable {

	private String destination;

	private Boolean transactional;

	private static final long serialVersionUID = 1L;

	public MessageHeader(String destination, Boolean transactional) {
		this.destination = destination;
		this.transactional = transactional;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Boolean getTransactional() {
		return transactional;
	}

	public void setTransactional(Boolean transactional) {
		this.transactional = transactional;
	}

	@Override
	public String toString() {
		return this.destination + "-" + this.transactional;
	}
}
