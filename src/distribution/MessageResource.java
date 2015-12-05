package distribution;

import java.io.File;
import java.io.Serializable;

public class MessageResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idResource;

	private File file;

	private ResourcePermissions resourceAction;

	private ResourcePermissions resourcePermissions;

	private String value;

	private Boolean faultInject;

	public MessageResource(File file, String value, ResourcePermissions resourceAction) {
		this.file = file;
		this.value = value;
		this.resourceAction = resourceAction;
		this.resourcePermissions = ResourcePermissions.NOTHING;
		this.faultInject = false;
	}

	public MessageResource(File file, String value, ResourcePermissions resourceAction, Boolean faultInject) {
		this.file = file;
		this.value = value;
		this.resourceAction = resourceAction;
		this.resourcePermissions = ResourcePermissions.NOTHING;
		this.faultInject = faultInject;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "[MessageResource file=" + this.file.getPath() + "- resourcePermissions=" + resourcePermissions
				+ " - value=" + this.value + "]";
	}

	public ResourcePermissions getResourcePermissions() {
		return resourcePermissions;
	}

	public void setResourcePermissions(ResourcePermissions resourcePermissions) {
		this.resourcePermissions = resourcePermissions;
	}

	public ResourcePermissions getResourceAction() {
		return resourceAction;
	}

	public void setResourceAction(ResourcePermissions resourceAction) {
		this.resourceAction = resourceAction;
	}

	public Integer getIdResource() {
		return idResource;
	}

	public void setIdResource(Integer idResource) {
		this.idResource = idResource;
	}

	public Boolean isFaultInject() {
		return faultInject;
	}

	public void setFaultInject(Boolean faultInject) {
		this.faultInject = faultInject;
	}
}
