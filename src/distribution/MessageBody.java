package distribution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageBody implements Serializable {

	private String body;

	private List<MessageResource> messageResoources;

	private static final long serialVersionUID = 1L;

	public MessageBody(String body) {
		this.body = body;
		this.messageResoources = new ArrayList<MessageResource>();
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<MessageResource> getMessageResources() {
		return messageResoources;
	}

	public void addMessageResources(MessageResource messageResource) {
		this.messageResoources.add(messageResource);
	}

	@Override
	public String toString() {
		return "[MessageBody body=" + this.body + " - messageResources=" + this.messageResoources + "]";
	}
}
