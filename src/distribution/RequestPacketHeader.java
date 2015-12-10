package distribution;

import java.io.Serializable;

public class RequestPacketHeader implements Serializable {

	private Operation operation;

	private static final long serialVersionUID = 1L;

	public RequestPacketHeader(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
