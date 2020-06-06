package ui;

import client.ClientMain;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;

public class TimeThread implements  Runnable{
    private JTextField timeTxt;
    private  int time;
    private boolean isOver;
    private  JFrame parentFrame;
    TimeThread(JFrame parentFrame,JTextField timeTxt, int time){
        this.parentFrame = parentFrame;
        this.timeTxt = timeTxt;
        this.time = time;

        new Thread(this).run();
    }

    public boolean isOver() {
        return isOver;
    }

    @Override
    public void run() {

        for(int i=time; i>=0; i--){
            timeTxt.setFont(new Font(Font.SANS_SERIF,Font.BOLD,17));
            timeTxt.setText(i + "");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isOver = true;
        ClientGui.disableAll();
        int score[] = ClientMain.sendResult();
        ClientGui.printScore(parentFrame,score);
    }
}
