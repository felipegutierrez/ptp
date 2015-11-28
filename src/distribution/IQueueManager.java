package distribution;

import java.io.IOException;

public interface IQueueManager {

	public void send(String msg) throws IOException, InterruptedException;

	public String receive() throws IOException, InterruptedException,
			ClassNotFoundException;

}
