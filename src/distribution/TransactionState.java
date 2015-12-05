package distribution;

public class TransactionState {

	// indica se iniciou a transação
	private Boolean start2PC;

	// indica se votou a requisição para todos os recursos da transação
	private Boolean voteRequestAll;

	// indica se realizou o commit para todos os recursos da transação
	private Boolean voteCommitAll;

	// indica se precisa abortar o voteRequest ou o voteCommit da transação.
	// Dependendo de quais outras flags estiverem ativas o rollback será
	// realizado
	private Boolean globalAbortAll;

	public TransactionState() {
		this.setStart2PC(false);
		this.setVoteRequestAll(false);
		this.setGlobalAbortAll(false);
		this.setVoteCommitAll(false);
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

	@Override
	public String toString() {
		return "[TransactionState start2PC=" + this.start2PC + " - voteRequestAll=" + this.voteRequestAll
				+ " - globalAbortAll=" + this.globalAbortAll + " - voteCommitAll=" + this.voteCommitAll + "]";
	}
}
