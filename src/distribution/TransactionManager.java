package distribution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransactionManager {

	private Map<Integer, Transaction> transactions;

	public TransactionManager() {
		this.transactions = new HashMap<Integer, Transaction>();
	}

	private Map<Integer, Transaction> getTransactions() {
		return transactions;
	}

	public Transaction getTransaction(Integer key) {
		return this.transactions.get(key);
	}

	public Integer createNewTransaction() {
		Integer key = getNewTransactionKey();
		Transaction transaction = new Transaction(key);
		this.transactions.put(key, transaction);
		return key;
	}

	private Integer getNewTransactionKey() {
		Integer lastKey = 0;
		Set<Integer> keySet = this.getTransactions().keySet();
		for (Integer key : keySet) {
			if (key > lastKey) {
				lastKey = key;
			}
		}
		Integer newKey = lastKey++;
		return newKey;
	}

	public Boolean startTransaction(Integer key, List<MessageResource> messageFiles) {
		Transaction transaction = this.getTransaction(key);
		Boolean startTransaction = transaction.startTransaction(messageFiles);
		return startTransaction;
	}

	public Boolean voteRequestForAll(Integer key, List<MessageResource> messageResources) {
		Transaction transaction = this.getTransaction(key);
		if (transaction.getTransactionState().isStart2PC()) {
			Boolean voteRequestForAll = transaction.voteRequestForAll(messageResources);
			return voteRequestForAll;
		} else {
			// TODO: a transação não foi iniciada!
			return false;
		}
	}

	public Boolean globalCommitForAll(Integer key, List<MessageResource> messageFiles) {

		Transaction transaction = this.getTransaction(key);
		if (transaction.getTransactionState().isVoteCommitAll()) {
			transaction.globalCommitForAll();
			return true;
		} else {
			// TODO: a transação não retornou VOTE_COMMIT_ALL
			return false;
		}
	}
}
