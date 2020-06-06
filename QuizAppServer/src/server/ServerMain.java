package server;

import database.FillUp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerMain {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 5000;
    public static ArrayList<ClientThread> clientThreadPools;
    public static void main(String[] args) {
        //new FillUp().fillData();
        clientThreadPools = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(true){
                ClientThread clientThread = new ClientThread(serverSocket.accept());
                //oppenentSearch(clientThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void oppenentSearch(ClientThread clientThread) throws InterruptedException {

        for(int i=0; i<clientThreadPools.size(); i++){
            ClientThread opponent = clientThreadPools.get(i);
            if(opponent.isOpponentAssigned() ==false){
                RandQuestions randQuestions = new RandQuestions();
                opponent.setOpponentAssigned(true);
                opponent.setOpponetThread(clientThread);
                opponent.setRandQuestions(randQuestions);
                Thread.sleep(30);
                clientThread.setOpponentAssigned(true);
                clientThread.setOpponetThread(opponent);
                clientThread.setRandQuestions(randQuestions);

                break;
            }
        }

         clientThreadPools.add(clientThread);
    }
}
