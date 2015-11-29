package distribution;

/**
 * Uma transação tem seu estado TransactionState e apenas um ResourceManager que
 * irá gerenciar todos os recursos para garantir o ACID.
 * 
 * @author felipe
 *
 */
public class Transaction {

	private Integer id;

	private TransactionState transactionState;

	private ResourceManager resourceManager;

	public Transaction(Integer id) {
		this.id = id;
		this.resourceManager = new ResourceManager();
	}

	public Integer getId() {
		return id;
	}

	public TransactionState getTransactionState() {
		return this.transactionState;
	}

	protected void startTransaction() {
		this.transactionState.setStart2PC(true);

		// TODO tem que criar a quantidade de recursos necessários para esta
		// transação
		this.resourceManager.createNewResource();
		this.resourceManager.createNewResource();
	}

	protected void voteRequestForAll() {
		// TODO: multicast vote_request para todos os particpantes

		// TODO: se der timeout ou alguma coisa errado
		// this.transactionState.setGlobalAbortAll(true);
		// return;

		// se tudo der certo
		this.transactionState.setVoteRequestAll(true);
	}

	protected void globalCommitForAll() {
		// TODO: multicast vote_commit para todos os participantes

		// TODO: se der timeout ou alguma coisa errado
		// this.transactionState.setGlobalAbortAll(true);
		// return;

		// se tudo der certo
		this.transactionState.setGlobalCommitAll(true);
	}

	public String toString() {
		return "[Transaction id=" + this.id.toString() + " - transactionState=" + this.transactionState + "]";
	}
}
