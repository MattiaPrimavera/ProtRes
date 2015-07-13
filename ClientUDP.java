import java.net.*;
import java.util.Scanner;

//Partie UDP du client charge' de l'envoie des donnees aux autres clients 
public class ClientUDP{
    private int udpPort;
    private String toSend;
    private static int alreadySent = 0;
    private String ipAddressServer;
    public ClientUDP(int udpPort, String ipAddressServer, String toSend){
        this.udpPort = udpPort;
        this.ipAddressServer = ipAddressServer;
        this.toSend = toSend;
    }

    public void sendData(){
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress inetAddressServer = InetAddress.getByName(this.ipAddressServer);
            DatagramPacket sendPacket;
            byte[] sendData = new byte[1024];
            sendData = Protocole.sendDataRequest(this.toSend).getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, inetAddressServer, this.udpPort);
            clientSocket.send(sendPacket);
            this.alreadySent++;
        }
        catch(SocketException se){ se.printStackTrace(); }
        catch(Exception e){ e.printStackTrace(); }
	}
}

