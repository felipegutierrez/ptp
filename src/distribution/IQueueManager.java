package distribution;

import java.io.IOException;

public interface IQueueManager {

	public void send(String msg, Boolean transactional) throws IOException, InterruptedException;

	public String receive(Boolean transactional) throws IOException, InterruptedException, ClassNotFoundException;
}
