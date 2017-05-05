package lu.uni.reseaux_info.commons;

public class ConnectionInfo {
	private String IP = "";
	private int port = 0;
	
	public ConnectionInfo(String ip, int port) {
		this.IP = ip;
		this.port = port;
	}
	
	public ConnectionInfo(String peer) throws NumberFormatException{
		String splitted[] = peer.split(":");
		if(splitted.length == 2){
			this.IP = splitted[0];
			this.port = Integer.parseInt(splitted[1]);
		}else if(splitted.length == 1){
			this.IP = splitted[0];
		}else{
			throw new IllegalArgumentException("Invalid format: " + peer);
		}
	}
	
	public String getIp() {
		return this.IP;
	}
	
	public int getPort() {
		return this.port;
	}
	
	@Override
	public String toString(){
		return IP + ":" + port;
	}
}
