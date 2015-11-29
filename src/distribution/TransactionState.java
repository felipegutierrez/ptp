package distribution;

public class TransactionState {

	private Boolean start2PC;
	private Boolean voteRequestAll;
	private Boolean globalAbortAll;
	private Boolean voteCommitAll;
	private Boolean globalCommitAll;

	public TransactionState() {
		this.setStart2PC(false);
		this.setVoteRequestAll(false);
		this.setGlobalAbortAll(false);
		this.setVoteCommitAll(false);
		this.setGlobalCommitAll(false);
	}

	public Boolean isStart2PC() {
		return start2PC;
	}

	protected void setStart2PC(Boolean start2pc) {
		start2PC = start2pc;
	}

	public Boolean isVoteRequestAll() {
		return voteRequestAll;
	}

	protected void setVoteRequestAll(Boolean voteRequestAll) {
		this.voteRequestAll = voteRequestAll;
	}

	public Boolean isGlobalAbortAll() {
		return globalAbortAll;
	}

	protected void setGlobalAbortAll(Boolean globalAbortAll) {
		this.globalAbortAll = globalAbortAll;
	}

	public Boolean isVoteCommitAll() {
		return voteCommitAll;
	}

	protected void setVoteCommitAll(Boolean voteCommitAll) {
		this.voteCommitAll = voteCommitAll;
	}

	public Boolean isGlobalCommitAll() {
		return globalCommitAll;
	}

	protected void setGlobalCommitAll(Boolean globalCommitAll) {
		this.globalCommitAll = globalCommitAll;
	}

	@Override
	public String toString() {
		return "[TransactionState start2PC=" + this.start2PC + " - voteRequestAll=" + this.voteRequestAll
				+ " - globalAbortAll=" + this.globalAbortAll + " - voteCommitAll=" + this.voteCommitAll
				+ " - globalCommitAll=" + this.globalCommitAll + "]";
	}
}
