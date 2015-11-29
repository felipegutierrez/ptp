package distribution;

public class Resource {

	private Integer id;

	private ResourceState resourceState;

	public Resource(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public ResourceState getResourceState() {
		return this.resourceState;
	}
}
