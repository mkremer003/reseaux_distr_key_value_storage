package lu.uni.reseaux_info.node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
	
	public void saveToFile(File file) throws IOException{
		try(FileOutputStream fos = new FileOutputStream(file)){
			Properties props = new Properties();
			StringBuilder sb = new StringBuilder();
			synchronized(neighborInfo){
				for(ConnectionInfo ci : neighborInfo){
					sb.append(",");
					sb.append(ci);
				}
			}
			String valueString = sb.toString();
			if(valueString.length() > 0){
				props.setProperty("neighbors", valueString.substring(1));
			}
			sb = new StringBuilder();
			synchronized(keyMap){
				for(Map.Entry<String, String> e : keyMap.entrySet()){
					sb.append(",");
					sb.append(e.getKey().replace(',', ' ').replace(':', ' '));
					sb.append(":");
					sb.append(e.getValue().replace(',', ' ').replace(':', ' '));
				}
			}
			valueString = sb.toString();
			if(valueString.length() > 0){
				props.setProperty("mappings", valueString.substring(1));
			}
			props.store(fos, "Node configuration file");
			fos.flush();
		}
	}
	
	public boolean loadFromFile(File file) throws IOException{
		if(file.exists()){
			try(FileInputStream fis = new FileInputStream(file)){
				Properties props = new Properties();
				props.load(fis);
				if(props.containsKey("neighbors")){
					synchronized(neighborInfo){
						neighborInfo.clear();
						String parsed[] = props.getProperty("neighbors").split(",");
						for(String peer : parsed){
							try{
								ConnectionInfo ci = new ConnectionInfo(peer);
								neighborInfo.add(ci);
							}catch(Exception e){
								System.err.println("Peer skipped: " + peer);
								e.printStackTrace();
							}
						}
					}
				}
				if(props.containsKey("mappings")){
					synchronized(keyMap){
						keyMap.clear();
						String parsed[] = props.getProperty("mappings").split(",");
						for(String entry : parsed){
							String mapping[] = entry.split(":");
							if(mapping.length == 2){
								keyMap.put(mapping[0], mapping[1]);
							}else{
								System.err.println("Mapping skipped: " + entry);
							}
						}
					}
				}
				return true;
			}
		}
		return false;
	}
}
