package server;

import com.sun.source.doctree.ThrowsTree;
import common.Question;
import database.QuizDb;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientThread implements Runnable{
    Socket socket;
    final Integer SIZE = 10;
    Integer TIME = 20;
    private String myName = null;
    //private String opponentName;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    HashMap<Integer, String> rightAnswer;
    HashMap<Integer, String> answerClient;
    private boolean isNameEntered;
    private boolean isOpponentAssigned = false;
    ClientThread opponetThread;
    private  boolean answerFound;
    Integer score;
    private  boolean oppenentSend;
    public RandQuestions getRandQuestions() {
        return randQuestions;
    }

    public void setRandQuestions(RandQuestions randQuestions) throws InterruptedException {
        Thread.sleep(10);
        this.randQuestions = randQuestions;
    }

    RandQuestions randQuestions;

    public ClientThread getOpponetThread() {
        return opponetThread;
    }

    public void setOpponetThread(ClientThread opponetThread) throws InterruptedException {
        this.opponetThread = opponetThread;
        Thread.sleep(10);

    }

    public ClientThread(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    public String getMyName() throws InterruptedException {
        //Thread.sleep(10);
        return myName;
    }

    public boolean isNameEntered() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isNameEntered;
    }

    public boolean isOpponentAssigned() {
        return isOpponentAssigned;
    }

    public void setOpponentAssigned(boolean opponentAssigned) throws InterruptedException {
        this.isOpponentAssigned = opponentAssigned;
        Thread.sleep(10);
    }

    public boolean isOppenentSend() {
        return oppenentSend;
    }

    @Override
    public void run() {

            try {
                //Thread.sleep(50);
                inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                while (getMyName()==null){
                    myName = getName();
                }

                isNameEntered = true;
                System.out.println("Name Entered......="+getMyName());
//            System.out.println("Name = "+myName);
//            opponentName = lookForOpponent();
                System.out.println("Searching opponent......for"+getMyName());
                while (isOpponentAssigned() == false) {
                    oppenentSearch();
                    Thread.sleep(10);
                }


                while (!isOppenentSend()) {
                    sendOpponent();
                }
                sendQuestion();
                Thread.sleep(5);
                //sendQuestion();
                sendTime();
                Thread.sleep(5);
                getAnswer();
                Thread.sleep(5);
                sendScore();
                Thread.sleep(5);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
//            try {
//                //socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            }
    }

    private void sendScore() throws IOException, InterruptedException {
        System.out.println("Sending score......to "+getMyName());
        score = randQuestions.compare(answerClient);
        outputStream.writeObject(score);
        while(opponetThread.answerFound == false || opponetThread.score == null){
            Thread.sleep(5);
        }
        outputStream.writeObject(opponetThread.score);
        outputStream.writeObject(score > opponetThread.score);
        System.out.println("Score = "+opponetThread.score+" "+score);
        System.out.println("Score Sent......to "+getMyName());
    }

    private void sendQuestion() throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        System.out.println("Questions sending to "+getMyName());
        outputStream.writeObject(randQuestions.number);
        for(int i: randQuestions.sendQuestion){
            outputStream.writeObject(randQuestions.questionArrayList.get(i));
        }
        System.out.println("Questions sent to"+getMyName());
    }

    private void sendOpponent() throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Sending opponent......to "+getMyName());
        if(getMyName()!=null && isOpponentAssigned()){
            outputStream.writeObject(opponetThread.getMyName());
            System.out.println("Opponent sent ="+opponetThread.getMyName());
            oppenentSend = true;
            Thread.sleep(20);
        }else{
            System.out.println("No opponent......of "+getMyName());
            return;
        }
    }

    private  void sendTime() throws IOException, InterruptedException {
        System.out.println("Sending time......to "+getMyName());
        outputStream.writeObject(TIME);
        System.out.println("time sent......"+TIME+" to "+getMyName());
    }

    private String getName() throws IOException, ClassNotFoundException {
        System.out.println("Getting name......");
        return (String) inputStream.readObject();
    }

    private String lookForOpponent() {
        return "Static";
    }

    private void getAnswer() throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Getting answer......of "+getMyName());
        answerClient = (HashMap<Integer, String>) inputStream.readObject();
        answerFound = true;
        System.out.println("Answer Found......of "+getMyName());
    }

    public void oppenentSearch() throws InterruptedException {

        for(int i=0; i<ServerMain.clientThreadPools.size(); i++){
            ClientThread opponent = ServerMain.clientThreadPools.get(i);
            if(opponent.equals(this)==false && opponent.isOpponentAssigned() ==false){
                RandQuestions randQuestions = new RandQuestions();
                opponent.setOpponentAssigned(true);
                opponent.setOpponetThread(this);
                opponent.setRandQuestions(randQuestions);
                //Thread.sleep(30);
                this.setOpponentAssigned(true);
                this.setOpponetThread(opponent);
                this.setRandQuestions(randQuestions);

                break;
            }
        }

        ServerMain.clientThreadPools.add(this);
    }
}
