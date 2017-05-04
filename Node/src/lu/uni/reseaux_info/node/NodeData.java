package lu.uni.reseaux_info.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lu.uni.reseaux_info.commons.ConnectionInfo;

/**
 * Data structure which holds the mappings of key to values as well as the addresses of neighbor nodes
 * @author michm
 *
 */
public class NodeData {
	
	private final Map<String, String> keyMap;
	private final List<ConnectionInfo> neighborInfo;
	private final Set<Integer> treatedIdSet;

	public NodeData(){
		keyMap = Collections.synchronizedMap(new HashMap<String, String>());
		neighborInfo = Collections.synchronizedList(new ArrayList<ConnectionInfo>());
		treatedIdSet = Collections.synchronizedSet(new HashSet<Integer>());
	}
	
	public Map<String, String> getKeyMap(){
		return keyMap;
	}
	
	public List<ConnectionInfo> getNeighborAddresses(){
		return neighborInfo;
	}
	
	public Set<Integer> getTreatedIdSet(){
		return treatedIdSet;
	}
}
