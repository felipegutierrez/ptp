package distribution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Um ResourceManager tem uma coleção re resources para para tarefa da
 * Transaction.
 * 
 * @author felipe
 *
 */
public class ResourceManager {

	private Map<Integer, Resource> resources;

	public ResourceManager() {
		this.resources = new HashMap<Integer, Resource>();
	}

	private Map<Integer, Resource> getResources() {
		return resources;
	}

	public Resource getResource(Integer key) {
		return this.resources.get(key);
	}

	public Boolean createNewResource(MessageResource messageResource) {
		if (!messageResource.getFile().exists()) {
			System.out.println("O arquivo [" + messageResource.getFile().getAbsolutePath() + "] não existe!");
			return false;
		}
		Integer key = getNewResourceKey();
		Resource resource = new Resource(key, messageResource);
		this.resources.put(key, resource);
		return true;
	}

	private Integer getNewResourceKey() {
		Integer lastKey = 0;
		Set<Integer> keySet = this.getResources().keySet();
		for (Integer key : keySet) {
			if (key > lastKey) {
				lastKey = key;
			}
		}
		Integer newKey = lastKey++;
		return newKey;
	}
}
