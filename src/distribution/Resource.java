package distribution;

public class Resource {

	private Integer id;

	private MessageResource messageResource;

	private ResourceState resourceState;

	private ResourcePermissions resourcePermissions;

	public Resource(Integer id, MessageResource messageResource) {
		this.id = id;
		this.messageResource = messageResource;
		this.resourceState = new ResourceState();
	}

	public Integer getId() {
		return id;
	}

	public ResourceState getResourceState() {
		return this.resourceState;
	}

	public void setResourceState(ResourceState resourceState) {
		this.resourceState = resourceState;
	}

	public MessageResource getMessageResource() {
		return messageResource;
	}

	public void setMessageResource(MessageResource messageResource) {
		this.messageResource = messageResource;
	}

	public ResourcePermissions getResourcePermissions() {
		return resourcePermissions;
	}

	public void setResourcePermissions(ResourcePermissions resourcePermissions) {
		this.resourcePermissions = resourcePermissions;
	}

}
