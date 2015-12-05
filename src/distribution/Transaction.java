package distribution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
		boolean flag = true;

		// TODO tem que criar a quantidade de recursos necessários para esta
		// transação
		for (MessageResource messageResource : messageResources) {
			if (this.resourceManager.createNewResource(messageResource)) {
				this.resourceManager.setStart2PC(messageResource);
			} else {
				// não conseguiu criar o resource
				flag = false;
			}
		}
		// se tudo der certo
		if (flag) {
			this.transactionState.setStart2PC(true);
		} else {
			this.transactionState.setGlobalAbortAll(true);
		}
		return flag;
	}

	protected Boolean voteRequestForAll(List<MessageResource> messageResources) {
		boolean flag = true;
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
				flag = false;
			} else {
				this.resourceManager.setResourcePermission(messageResource);
				this.resourceManager.setRequestVote(messageResource);
			}
		}
		// se tudo der certo
		if (flag) {
			this.transactionState.setVoteRequestAll(true);
		} else {
			this.transactionState.setGlobalAbortAll(true);
		}
		return flag;
	}

	protected Boolean globalCommitForAll(List<MessageResource> messageResources) {

		boolean flag = true;
		// TODO: multicast vote_commit para todos os participantes

		for (MessageResource messageResource : messageResources) {

			switch (messageResource.getResourceAction()) {
			case READ_AND_WRITE_AND_EXECUTE:

				break;
			case READ_AND_WRITE:
				PrintWriter pw = null;
				try {
					File file = messageResource.getFile();
					FileWriter fw = new FileWriter(file, true);
					pw = new PrintWriter(fw);
					pw.println(messageResource.getValue());
					this.resourceManager.setVoteCommit(messageResource);
				} catch (IOException e) {
					e.printStackTrace();
					flag = false;
				} finally {
					if (pw != null) {
						pw.close();
					}
				}
				break;
			case READ_AND_EXECUTE:
				break;
			case WRITE_AND_EXECUTE:
				break;
			case READ:
				break;
			case WRITE:
				break;
			case EXECUTE:
				break;
			default:
				flag = false;
			}
		}

		// TODO: se der timeout ou alguma coisa errado
		// this.transactionState.setGlobalAbortAll(true);
		// return;

		// se tudo der certo
		if (flag) {
			this.transactionState.setVoteCommitAll(true);
		} else {
			this.transactionState.setGlobalAbortAll(true);
		}
		return flag;
	}

	public String toString() {
		return "[Transaction id=" + this.id.toString() + " - transactionState=" + this.transactionState + "]";
	}
}
