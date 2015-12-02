package distribution;

import java.util.List;

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
		this.transactionState = new TransactionState();
		this.resourceManager = new ResourceManager();
	}

	public Integer getId() {
		return id;
	}

	public TransactionState getTransactionState() {
		return this.transactionState;
	}

	protected Boolean startTransaction(List<MessageResource> messageResources) {
		this.transactionState.setStart2PC(true);

		// TODO tem que criar a quantidade de recursos necessários para esta
		// transação
		for (MessageResource messageResource : messageResources) {
			if (!this.resourceManager.createNewResource(messageResource)) {
				// não conseguiu criar o resource
				return false;
			}
		}
		return true;
	}

	protected Boolean voteRequestForAll(List<MessageResource> messageResources) {
		// multicast vote_request para todos os particpantes
		for (MessageResource messageResource : messageResources) {
			if (messageResource.getFile().canRead() && messageResource.getFile().canWrite()
					&& messageResource.getFile().canExecute()) {
				messageResource.setResourcePermissions(ResourcePermissions.READ_AND_WRITE_AND_EXECUTE);
			} else if (messageResource.getFile().canRead() && messageResource.getFile().canWrite()
					&& !messageResource.getFile().canExecute()) {
				messageResource.setResourcePermissions(ResourcePermissions.READ_AND_WRITE);
			} else if (messageResource.getFile().canRead() && !messageResource.getFile().canWrite()
					&& messageResource.getFile().canExecute()) {
				messageResource.setResourcePermissions(ResourcePermissions.READ_AND_EXECUTE);
			} else if (!messageResource.getFile().canRead() && messageResource.getFile().canWrite()
					&& messageResource.getFile().canExecute()) {
				messageResource.setResourcePermissions(ResourcePermissions.WRITE_AND_EXECUTE);
			} else if (messageResource.getFile().canRead() && !messageResource.getFile().canWrite()
					&& !messageResource.getFile().canExecute()) {
				messageResource.setResourcePermissions(ResourcePermissions.READ);
			} else if (!messageResource.getFile().canRead() && messageResource.getFile().canWrite()
					&& !messageResource.getFile().canExecute()) {
				messageResource.setResourcePermissions(ResourcePermissions.WRITE);
			} else if (!messageResource.getFile().canRead() && !messageResource.getFile().canWrite()
					&& !messageResource.getFile().canExecute()) {
				messageResource.setResourcePermissions(ResourcePermissions.NOTHING);
			}
			if (!messageResource.getResourceAction().equals(messageResource.getResourcePermissions())) {
				System.out.println("A ação requisitada [" + messageResource.getResourceAction()
						+ "] não é permitita para o recurso [" + messageResource + "]");
				this.transactionState.setGlobalAbortAll(true);
				return false;
			}
		}
		// se tudo der certo
		this.transactionState.setVoteRequestAll(true);
		return true;
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
