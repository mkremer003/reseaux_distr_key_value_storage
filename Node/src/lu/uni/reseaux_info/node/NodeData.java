package lu.uni.reseaux_info.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data structure which holds the mappings of key to values as well as the addresses of neighbor nodes
 * @author michm
 *
 */
public class NodeData {
	
	private final Map<String, String> data;
	private final List<String> neighbor;

	NodeData(){
		data = Collections.synchronizedMap(new HashMap<String, String>());
		neighbor = Collections.synchronizedList(new ArrayList<String>());
	}
	
	public Map<String, String> getDataMap(){
		return data;
	}
	
	public List<String> getNeighborAddresses(){
		return neighbor;
	}
}
