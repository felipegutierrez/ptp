package distribution;

import java.io.Serializable;

public class RequestPacketHeader implements Serializable {
	private String operation;

	private static final long serialVersionUID = 1L;
	
	public RequestPacketHeader(String operation){
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
