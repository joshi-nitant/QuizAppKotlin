package ui;

import client.ClientMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchingThread extends Thread{

    ClientGui frame;
    static boolean isOpponentFound;
    private static boolean isStartPressed;

    public static boolean isStartPressed() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isStartPressed;
    }

    public static void setStartPressed(boolean startPressed) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isStartPressed = startPressed;
    }


    public boolean isOpponentFound() {
        return isOpponentFound;
    }

    public void setOpponentFound(boolean opponentFound) {
        isOpponentFound = opponentFound;
    }

    public SearchingThread(ClientGui Jframe){
        this.frame = Jframe;
    }

    @Override
    public void run() {
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);

        JTextField nameTxt = new JTextField();
        nameTxt.setBounds(15,30, 280,50);
        nameTxt.setFont(font);
        nameTxt.setBorder(null);
        nameTxt.setEditable(false);

        JButton buttonSubmit = new JButton("Start");
        buttonSubmit.setBounds(90, 210,100,40);
        buttonSubmit.setEnabled(false);

        JDialog dialog = new JDialog(frame,"Name");
        dialog.setLayout(null);
        dialog.setSize(300, 300);
        dialog.add(nameTxt);
        dialog.add(buttonSubmit);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        buttonSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                SearchingThread.setStartPressed(true);
                //frame.start();
            }
        });
        while (!isOpponentFound()){
            nameTxt.setText("Searching for opponent............");
        }

        nameTxt.setText("Your Opponent = " + ClientMain.opponentName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buttonSubmit.setEnabled(true);
    }
}
