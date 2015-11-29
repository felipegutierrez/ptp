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

	public Integer createNewResource() {
		Integer key = getNewResourceKey();
		Resource resource = new Resource(key);
		this.resources.put(key, resource);
		return key;
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
