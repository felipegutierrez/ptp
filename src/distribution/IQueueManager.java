package distribution;

import java.io.IOException;
import java.util.List;

public interface IQueueManager {

	public void send(String msg, List<MessageResource> messageFiles, Boolean transactional)
			throws IOException, InterruptedException;

	public String receive(Boolean transactional) throws IOException, InterruptedException, ClassNotFoundException;
}
