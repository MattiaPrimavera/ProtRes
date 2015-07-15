//classe qui represente les informations d'importance concernant les clients du P2P: address, udpServerPort
public class ClientInfo{
	private String address;
	private int udpServerPort;
	public ClientInfo(String address, int udpServerPort){
		this.address = address;
		this.udpServerPort = udpServerPort;
	}
	//getters
	public int getUdpServerPort(){ return this.udpServerPort; }
	public String getAddress(){ return this.address; }
	//setters
	public void setUdpServerPort(){ this.udpServerPort = udpServerPort; }
	public void setAddress(String address){ this.address = address; }
	public String toString(){ return this.address+"#"+this.udpServerPort; }
}
