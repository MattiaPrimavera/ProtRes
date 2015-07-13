//Classe qui abstrait le Protocole du reste du programme
public class Protocole{ 
	public static String clientListEnd = "CLIENT_LIST_END";
	public static String prefixConnectRequest = "CONNECT_";
	public static String prefixDeconnectRequest = "DECONNECT_";
	public static String prefixUpdateRequest = "UPDATE_";
	public static String prefixSendDataRequest = "DATA_";

	public static String sendDataRequest(String data){
		return ""+prefixSendDataRequest+data;
	}

	public static String updateRequest(String clientInfo){ 
		return ""+prefixUpdateRequest+clientInfo;
	}
	public static String deconnectFromServer(String clientInfo){
		return ""+prefixDeconnectRequest+clientInfo;
	}

	/*public static String deconnectFromServer(int udpServerPort){
		return "DECONNECT_"+udpServerPort;
	}*/
	public static String connectToServer(int udpServerPort){
		return "CONNECT_"+udpServerPort;
	}

	public static String[] biSplit(String toSplit, char delimiter){
		String[] result = new String[2];
		int indexDelimiter = 0;
		for(int i = 0; i < toSplit.length(); i++){ 
			indexDelimiter++;
			if(toSplit.charAt(i) == delimiter) break; 
		}
		if(indexDelimiter == 0) return null;
		result[0] = toSplit.substring(0,indexDelimiter-1);
		result[1] = toSplit.substring(indexDelimiter, toSplit.length());
		return result;
	}
}