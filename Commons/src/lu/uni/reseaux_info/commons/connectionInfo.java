package lu.uni.reseaux_info.commons;

public class connectionInfo {
	private String IP = "";
	private int port = 0;
	
	public connectionInfo(String ip, int port) {
		this.IP = ip;
		this.port = port;
	}
	
	public String getIp() {
		return this.IP;
	}
	
	public int getPort() {
		return this.port;
	}
}
