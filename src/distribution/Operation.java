package distribution;

public enum Operation {

	NOTHING(0), SEND(1), RECEIVE(2), DEQUEUE(3);

	private int operation;

	private Operation(int operation) {
		this.operation = operation;
	}

	public int getStatus() {
		return operation;
	}

	public int toInt() {
		return operation;
	}

	public static Operation fromInt(int value) {
		switch (value) {
		case 0:
			return NOTHING;
		case 1:
			return SEND;
		case 2:
			return RECEIVE;
		case 3:
			return DEQUEUE;
		default:
			return NOTHING;
		}
	}

	@Override
	public String toString() {
		switch (this) {
		case SEND:
			return "SEND";
		case RECEIVE:
			return "RECEIVE";
		case DEQUEUE:
			return "DEQUEUE";
		case NOTHING:
			return "NOTHING";
		default:
			return "NOTHING";
		}
	}
}
