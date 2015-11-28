package distribution;

import java.io.Serializable;

public class MessageHeader implements Serializable {
	private String destination;

	private static final long serialVersionUID = 1L;

	public MessageHeader(String destination) {
		this.destination = destination;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return this.destination;
	}
}
