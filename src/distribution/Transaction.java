package distribution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

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

		// tem que criar a quantidade de recursos necessários para esta
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
		// multicast vote_commit para todos os participantes

		for (MessageResource messageResource : messageResources) {

			switch (messageResource.getResourceAction()) {
			case READ_AND_WRITE_AND_EXECUTE:

				break;
			case READ_AND_WRITE:
				PrintWriter pw = null;
				try {
					if (messageResource.isFaultInject()) {
						System.out.println("Fault Inject TRUE for " + messageResource);
						throw new Exception();
					} else {
						File file = messageResource.getFile();
						FileWriter fw = new FileWriter(file, true);
						pw = new PrintWriter(fw);
						pw.println(messageResource.getValue());
						this.resourceManager.setVoteCommit(messageResource);
					}
				} catch (IOException e) {
					// e.printStackTrace();
					flag = false;
				} catch (Exception e) {
					// e.printStackTrace();
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

	protected Boolean globalRollBackForAll(List<MessageResource> messageResources) {

		boolean flag = true;

		for (MessageResource messageResource : messageResources) {

			if (this.resourceManager.isVoteCommit(messageResource)) {
				switch (messageResource.getResourceAction()) {
				case READ_AND_WRITE_AND_EXECUTE:

					break;
				case READ_AND_WRITE:
					File file = messageResource.getFile();
					try {
						List<String> lines = FileUtils.readLines(file);
						List<String> updatedLines = lines.stream().filter(s -> !s.contains(messageResource.getValue()))
								.collect(Collectors.toList());
						FileUtils.writeLines(file, updatedLines, false);
					} catch (IOException e) {
						// e.printStackTrace();
						flag = false;
					} catch (Exception e) {
						// e.printStackTrace();
						flag = false;
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
		}

		// TODO: se der timeout ou alguma coisa errado
		// this.transactionState.setGlobalAbortAll(true);
		// return;

		// se tudo der certo
		if (flag) {
			this.transactionState.setGlobalAbortOK(true);
		} else {
			this.transactionState.setGlobalAbortOK(false);
		}
		return flag;
	}

	public String toString() {
		return "[Transaction id=" + this.id.toString() + " - transactionState=" + this.transactionState + "]";
	}
}
