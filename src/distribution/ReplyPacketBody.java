package distribution;

import java.io.Serializable;
import java.util.ArrayList;

public class ReplyPacketBody implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Object> parameters = new ArrayList<Object>();
	private Message message;

	public ReplyPacketBody(ArrayList<Object> parameters, Message message) {
		this.parameters = parameters;
		this.message = message;
	}

	public ArrayList<Object> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Object> parameters) {
		this.parameters = parameters;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
