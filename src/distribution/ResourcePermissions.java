package distribution;

public enum ResourcePermissions {

	NOTHING(0), READ(1), WRITE(2), EXECUTE(3), READ_AND_WRITE(4), READ_AND_EXECUTE(5), WRITE_AND_EXECUTE(
			6), READ_AND_WRITE_AND_EXECUTE(7);

	private int permission;

	private ResourcePermissions(int permission) {
		this.permission = permission;
	}

	public int getStatus() {
		return permission;
	}

	public int toInt() {
		return permission;
	}

	public static ResourcePermissions fromInt(int value) {
		switch (value) {
		case 1:
			return READ;
		case 2:
			return WRITE;
		case 3:
			return EXECUTE;
		case 4:
			return READ_AND_WRITE;
		case 5:
			return READ_AND_EXECUTE;
		case 6:
			return WRITE_AND_EXECUTE;
		case 7:
			return READ_AND_WRITE_AND_EXECUTE;
		default:
			return NOTHING;
		}
	}

	@Override
	public String toString() {
		switch (this) {
		case READ:
			return "READ";
		case WRITE:
			return "WRITE";
		case EXECUTE:
			return "EXECUTE";
		case READ_AND_WRITE:
			return "READ_AND_WRITE";
		case READ_AND_EXECUTE:
			return "READ_AND_EXECUTE";
		case WRITE_AND_EXECUTE:
			return "WRITE_AND_EXECUTE";
		case READ_AND_WRITE_AND_EXECUTE:
			return "READ_AND_WRITE_AND_EXECUTE";
		default:
			return "NOTHING";
		}
	}
}
