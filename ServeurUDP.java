import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurUDP implements Runnable{
    private ArrayList<ClientInfo> clientInfoList;
    private int port;
    public ServeurUDP(int port){
        this.port = port;
    }

    public static void main(String []args){
        Thread t = new Thread(new ServeurUDP(Integer.parseInt(args[0])));
        t.start();
    }

    //recoit les messages envoyes en UDP sur le port this.port et les imprime a l'ecran
    public void run(){
        int nombreDonneesRecus = 0;
        try{
            System.out.println("********************************");
            System.out.println("*       Starting Server UDP    *");
            System.out.println("*       Starting Client TCP    *");
            System.out.println("********************************");
            DatagramSocket serverSocket = new DatagramSocket(this.port);
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String dataReceived = new String(receivePacket.getData());
                if(!dataReceived.startsWith(Protocole.prefixSendDataRequest))
                    continue;
                dataReceived = dataReceived.substring(Protocole.prefixSendDataRequest.length(), dataReceived.length());
                InetAddress ipAddressSender = receivePacket.getAddress();
                String addresseClientActuel = ipAddressSender.getHostAddress();
                int port = receivePacket.getPort();
                nombreDonneesRecus++;
                System.out.println(nombreDonneesRecus+") "+dataReceived+" ---> FROM: "+ipAddressSender +" " + port);
                receiveData = new byte[1024];
                if(nombreDonneesRecus == 5){
                    try{ Thread.sleep(3000); }catch(Exception e){};
                    System.out.println("\nDOWNLOAD COMPLETED... -> STOPPING SERVEUR...");
                    System.exit(0);
                    break;
                }
            }
        }
        catch(SocketException se){ se.printStackTrace(); }
        catch(IOException e){ e.printStackTrace(); }
    }
}
