package lu.uni.reseaux_info.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data structure which holds the mappings of key to values as well as the addresses of neighbor nodes
 * @author michm
 *
 */
public class NodeData {
	
	private final Map<String, String> keyMap;
	private final List<String> neighborNodes;
	private final Set<Long> treatedIdSet;

	NodeData(){
		keyMap = Collections.synchronizedMap(new HashMap<String, String>());
		neighborNodes = Collections.synchronizedList(new ArrayList<String>());
		treatedIdSet = Collections.synchronizedSet(new HashSet<Long>());
	}
	
	public Map<String, String> getKeyMap(){
		return keyMap;
	}
	
	public List<String> getNeighborAddresses(){
		return neighborNodes;
	}
	
	public Set<Long> getTreatedIdSet(){
		return treatedIdSet;
	}
}
