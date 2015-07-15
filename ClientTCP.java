import java.util.*;
import java.io.*;
import java.net.*;

//Composant du Client qui s'occupe de la communication avec le ServeurCentrale, l'elaboration des reponses du
//Serveur et l'envoie des donnees aux clients connectes au Peer To Peer
public class ClientTCP implements Runnable{
    private ArrayList<ClientInfo> listeClients;
    private int tcpServerPort, udpServerPort;
    private String serverAddress;
    private String donnees;
    private Socket s;

    public ClientTCP(String serverAddress, int tcpServerPort, int udpServerPort, String donnees){
        this.serverAddress = serverAddress;
        this.tcpServerPort = tcpServerPort;
        this.udpServerPort = udpServerPort;
        this.donnees = donnees;
        this.listeClients = new ArrayList<ClientInfo>();
        try{
            this.s = new Socket(serverAddress, tcpServerPort);
        }
        catch(UnknownHostException e){ e.printStackTrace(); }
        catch(IOException e){ e.printStackTrace(); }
    }

    public void run(){

        if(this.connect() != 0){
            this.demarrePartage();
            this.waitForUpdates();
        }//si on est le premier client a se connecter on s'attend pas une liste des clients connectes
        else
            this.waitForUpdates();
    }

    //a chaque fois qu'un nouveau client se connecte, le ServeurCentrale nous envoie une mise-a-jour
    public void waitForUpdates(){
        BufferedReader in;
        String tmp = "";
        //reception liste Clients sous forme de String "ipADDR#udpPORT"
            try{
                in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
                while(true){
                    tmp = in.readLine();
                    if(!tmp.startsWith(Protocole.prefixUpdateRequest)) continue;
                    String [] informationClient = new String[2];
                    informationClient = Protocole.biSplit(tmp.substring(Protocole.prefixUpdateRequest.length(), tmp.length()), '#');

                    if(tmp.equals(Protocole.clientListEnd)) continue;
                    ClientInfo tmpInfo = new ClientInfo(informationClient[0], Integer.parseInt(informationClient[1]));
                    ClientUDP udpClient = new ClientUDP(tmpInfo.getUdpServerPort(), tmpInfo.getAddress(), this.donnees);
                    udpClient.sendData();
                }
            }
            catch(NullPointerException npe){
                System.out.println("\nSERVER CONNECTION LOST...\nSTOPPING EXECUTION...");
                System.exit(0);
            }
            catch(SocketException se){ se.printStackTrace(); }
            catch(IOException e){ e.printStackTrace(); }
    }

    public void demarrePartage(){
        for(ClientInfo c : this.listeClients){
            ClientUDP tmp =    new ClientUDP(c.getUdpServerPort(), c.getAddress(), this.donnees);
            tmp.sendData();
        }
    }

    public int getContactsInfo(){
        int clientNumber = 0;
        String tmp = "";
        BufferedReader in;
        //reception liste Clients sous forme de String "ipADDR#udpPORT"
        ArrayList<String> listeClientsStr = new ArrayList<String>();
        try{
            in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            while(true){
                tmp = in.readLine();
                boolean alreadyReceived = false;
                for(String tmpString : listeClientsStr){
                    if(tmp.equals(tmpString)) alreadyReceived = true;
                }
                if(alreadyReceived) continue;
                if(!tmp.equals(Protocole.clientListEnd)){
                    listeClientsStr.add(tmp);
                    clientNumber++;
                }
                else{
                    if(clientNumber == 0)
                        return 0;
                    else
                        break;
                }
            }
        }
        catch(NullPointerException npe){ npe.printStackTrace(); }
        catch(SocketException se){ se.printStackTrace(); }
        catch(IOException e){ e.printStackTrace(); }

        //remplissage liste des client sous forme d'objets ClientInfo
        for(String s : listeClientsStr){
            String [] informationClient = new String[2];
            informationClient = Protocole.biSplit(s, '#');
            this.listeClients.add(new ClientInfo(informationClient[0], Integer.parseInt(informationClient[1])));
        }
        return clientNumber;
    }

    //tentatif de connexion au ServeurCentrale pour recevoir la liste clients connectes
    public int connect(){
        try{
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.s.getOutputStream()));
            pw.println(Protocole.connectToServer(udpServerPort));
            pw.flush();
        }
        catch(Exception e){ e.printStackTrace(); }
        return this.getContactsInfo();
    }
}
