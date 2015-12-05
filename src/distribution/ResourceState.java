package distribution;

public class ResourceState {

	private Boolean start2PC = false;

	private Boolean voteResquest = false;

	private Boolean voteCommit = false;

	public ResourceState() {
		this.start2PC = false;
		this.voteResquest = false;
		this.voteCommit = false;
	}

	public Boolean isVoteResquest() {
		return voteResquest;
	}

	public void setVoteResquest(Boolean voteResquest) {
		this.voteResquest = voteResquest;
	}

	public Boolean isVoteCommit() {
		return voteCommit;
	}

	public void setVoteCommit(Boolean voteCommit) {
		this.voteCommit = voteCommit;
	}

	public Boolean isStart2PC() {
		return start2PC;
	}

	public void setStart2PC(Boolean start2pc) {
		start2PC = start2pc;
	}
}
