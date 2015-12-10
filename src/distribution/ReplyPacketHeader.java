package distribution;

import java.io.Serializable;

public class ReplyPacketHeader implements Serializable {

	private Boolean transactional;
	private String operation;

	private static final long serialVersionUID = 1L;

	public ReplyPacketHeader(String operation, Boolean transactional) {
		this.operation = operation;
		this.transactional = transactional;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Boolean isTransactional() {
		return transactional;
	}

	public void setTransactional(Boolean transactional) {
		this.transactional = transactional;
	}
}
