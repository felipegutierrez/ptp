package distribution;

public class Resource {

	private Integer id;

	private MessageResource messageResource;

	private ResourceState resourceState;

	public Resource(Integer id, MessageResource messageResource) {
		this.id = id;
		this.messageResource = messageResource;
	}

	public Integer getId() {
		return id;
	}

	public ResourceState getResourceState() {
		return this.resourceState;
	}

	public MessageResource getMessageResource() {
		return messageResource;
	}

	public void setMessageResource(MessageResource messageResource) {
		this.messageResource = messageResource;
	}

}
