package client;

import common.Question;;
import ui.ClientGui;
import ui.SearchingThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ClientMain {
    private static final String IP = "192.168.56.1";
    private static final int PORT = 5000;
    private static ArrayList<Question> questionList = new ArrayList<>();
    private static Socket socket;
    private static Integer SIZE;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private static ClientGui clientGui;
    private static String myName;
    public static String opponentName;

    public static void main(String[] args) {
        int time = 0;
        try {
            socket = new Socket(IP, PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            socket.setSoTimeout(15000);
            clientGui = new ClientGui();
            clientGui.showNameDialog();
            while ((clientGui.isNameEntered())==false);
            myName = clientGui.getNameEntered();
            //System.out.println(myName +"Entered");
            sendNameAndGetOpponent();
            while (SearchingThread.isStartPressed()==false);

            getQuestions();
            time = getTime();
            clientGui.setQuestionList(questionList);
            clientGui.setTIME(time);
            clientGui.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //new ClientGui(questionList, time);
    }

    private static int getTime() throws IOException, ClassNotFoundException {
        int time = 0;
        time = (Integer) inputStream.readObject();
        return time;
    }

    private static void getQuestions() throws IOException, ClassNotFoundException {
        System.out.println("Starting to recv questions ");
        Question question;
        SIZE = (Integer) inputStream.readObject();
        for (int i = 0; i < SIZE; i++) {
            question = (Question) inputStream.readObject();
            questionList.add(question);
            System.out.println(question.getId());
        }
        System.out.println("Questions recevied");
    }
    public static int[] sendResult() {
        try {
            int score[] = new int[3];
            HashMap<Integer, String> answerList = ClientGui.getQuestioAnswer();
            //System.out.println(answerList.size());
            outputStream.writeObject(answerList);
            score[0] = (Integer) inputStream.readObject();
            score[1] = (Integer) inputStream.readObject();
            score[2] = ((Boolean) inputStream.readObject())? 1: 0;
            System.out.println("Score ="+score[0]+" "+score[1]+" "+score[2]);
            return score;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new int[]{0};
    }

    public static void sendNameAndGetOpponent(){
        try {
            outputStream.writeObject(myName);
            SearchingThread searchingThread = new SearchingThread(clientGui);
            searchingThread.start();
            opponentName = (String) inputStream.readObject();
            System.out.println("Oppoenent = "+opponentName);
            searchingThread.setOpponentFound(true);
            searchingThread.join();

            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }
}

