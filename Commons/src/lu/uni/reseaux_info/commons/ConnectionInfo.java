package lu.uni.reseaux_info.commons;

public class ConnectionInfo {
	private String IP = "";
	private int port = 0;
	
	public ConnectionInfo(String ip, int port) {
		this.IP = ip;
		this.port = port;
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
