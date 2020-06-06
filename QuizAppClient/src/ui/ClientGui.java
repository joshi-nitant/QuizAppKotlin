package ui;

import client.ClientMain;
import common.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import static ui.ClientGui.disableAll;

public class ClientGui extends  JFrame implements ActionListener, ItemListener {
    private static JButton button1;
    private JTextArea questionTxt;
    private static JRadioButton option1;
    private static JRadioButton option2;
    private static JRadioButton option3;
    private static JRadioButton option4;
    private static ButtonGroup buttonGroup;
    private String answer = "";
    private ArrayList<Question> questionList = new ArrayList<>();
    private static int index;
    private static HashMap<Integer, String> questioAnswer = new HashMap();
    private  Question current;
    private Integer TIME;
    private Font font;
    private JTextField timeTxt;
    private boolean isOpponentFound;
    private boolean isNameEntered;
    private String nameEntered;

    public ClientGui(){
    }
    public String getNameEntered() {
        //System.out.println("In name entered");
        return nameEntered;
    }

    public void setNameEntered(String nameEntered) {
        this.nameEntered = nameEntered;
    }

    public boolean isNameEntered() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isNameEntered;
    }


    public boolean isOpponentFound() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isOpponentFound;
    }

    public void setOpponentFound(boolean opponentFound) {
        isOpponentFound = opponentFound;
    }


    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    public Integer getTIME() {
        return TIME;
    }

    public void setTIME(Integer TIME) {
        this.TIME = TIME;
    }

    public void start(){
        System.out.println("In start");
        font = new Font(Font.SANS_SERIF, Font.PLAIN,18);

        setLayout(new BorderLayout(10,10));
        setTitle("Quiz");

        questionTxt = new JTextArea();
        questionTxt.setFont(font);
        questionTxt.setRows(7);
        questionTxt.setEditable(false);
        questionTxt.setWrapStyleWord(true);
        questionTxt.setLineWrap(true);

        buttonGroup = new ButtonGroup();
        button1 = new JButton("Next");
        button1.setFont(font);
        button1.addActionListener(this);

        JPanel jPanel = new JPanel(new GridLayout(4,1));
        option1 = new JRadioButton("option 1");
        option1.setFont(font);
        option1.addItemListener(this);

        option2 = new JRadioButton("option 1");
        option2.addItemListener(this);
        option2.setFont(font);

        option3 = new JRadioButton("option 1");
        option3.addItemListener(this);
        option3.setFont(font);

        option4 = new JRadioButton("option 1");
        option4.addItemListener(this);
        option4.setFont(font);

        buttonGroup.add(option1);
        buttonGroup.add(option2);
        buttonGroup.add(option3);
        buttonGroup.add(option4);

        jPanel.add(option1);
        jPanel.add(option2);
        jPanel.add(option3);
        jPanel.add(option4);


        //add(questionTxt, BorderLayout.NORTH);
        add(jPanel, BorderLayout.CENTER);
        add(button1, BorderLayout.SOUTH);

        JPanel upperPanel = new JPanel();
        questionTxt.setBounds(0, 50, 350,100);
        timeTxt = new JTextField();
        timeTxt.setBounds(350,0,70,70);
        upperPanel.add(questionTxt);
        upperPanel.add(timeTxt);
        add(upperPanel, BorderLayout.NORTH);

        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);


        //add(timeTxt, BorderLayout.EAST);
        nextQuestion();
        new TimeThread(this, timeTxt, TIME);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        nextQuestion();
        //setAnswer();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        answer = ((JRadioButton)e.getSource()).getText();
        questioAnswer.put(current.getId(),answer);
        button1.setEnabled(true);
    }

    void setAnswer(){
        questioAnswer.put(current.getId(), answer);
    }

    public static HashMap<Integer, String> getQuestioAnswer() {
        return questioAnswer;
    }

    void nextQuestion(){
        //printScore(0);
        if(index < questionList.size()){
            current = questionList.get(index);
            //System.out.println(current.getId());
            questionTxt.setText(current.getQuestion());
            String[] options = current.getIncorrect_answers();
            option1.setText(options[0]);
            option2.setText(options[1]);
            option3.setText(options[2]);
            option4.setText(options[3]);

            buttonGroup.clearSelection();

        if(index == questionList.size()-1){
                button1.setText("Submit");
                button1.setEnabled(false);
            }else{
                button1.setText("Next");
                button1.setEnabled(false);
            }

        }else if(index == questionList.size()){
            //System.out.println("here");
            //button1.setEnabled(false);
            ClientGui.disableAll();
            int score[] = ClientMain.sendResult();
            //System.out.println("Score = "+score);
            printScore(this, score);
        }
        index++;
        //current = questionList.get(index);
    }

    public static void disableAll(){
        button1.setEnabled(false);
        buttonGroup.clearSelection();
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }

    public static void printScore(JFrame parentFrame,int score[]){

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);

        JTextField highlightTxt = new JTextField();
        highlightTxt.setEditable(false);
        highlightTxt.setFont(font);
        highlightTxt.setBorder(null);

        if(score[2]==1){
            highlightTxt.setBounds(60,30, 300,50);
            highlightTxt.setText("Congratulations you won!!!!");
        }else{
            highlightTxt.setBounds(110,30, 300,50);
            highlightTxt.setText("Sorry You Lost!!!!");
        }

        JTextField myscoreTxt = new JTextField("Your Score = "+score[0]);
        myscoreTxt.setBounds(120,90, 200,50);
        myscoreTxt.setEditable(false);
        myscoreTxt.setFont(font);
        myscoreTxt.setBorder(null);

        JTextField opponentTxt = new JTextField(ClientMain.opponentName+"'s Score = "+score[1]);
        opponentTxt.setBounds(120,150, 200,50);
        opponentTxt.setEditable(false);
        opponentTxt.setFont(font);
        opponentTxt.setBorder(null);

        JButton buttonExit = new JButton("OK!!");
        buttonExit.setBounds(150, 320,100,40);

        JDialog dialog = new JDialog(parentFrame, "Score");
        dialog.setLayout(null);
        dialog.setSize(400, 400);
        dialog.add(highlightTxt);
        dialog.add(myscoreTxt);
        dialog.add(opponentTxt);
        dialog.add(buttonExit);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                parentFrame.dispose();
            }
        });
    }

    public void showNameDialog(){
        Font fontTxt = new Font(Font.SANS_SERIF, Font.BOLD, 20);

        JTextField nameTxt = new JTextField("Enter Your Name");

        nameTxt.setBounds(15,30, 280,50);
        nameTxt.setFont(fontTxt);
        nameTxt.setBorder(null);
        /*Adding Placeholder*/

        nameTxt.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameTxt.getText().equals("Enter Your Name")) {
                    nameTxt.setText("");
                    nameTxt.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (nameTxt.getText().isEmpty()) {
                    nameTxt.setForeground(Color.GRAY);
                    nameTxt.setText("Enter Your Name");
                }
            }
        });



        JButton buttonSubmit = new JButton("Submit!!");
        buttonSubmit.setBounds(90, 210,100,40);

        JDialog dialog = new JDialog(this,"Name");
        dialog.setLayout(null);
        dialog.setSize(300, 300);
        dialog.add(nameTxt);
        dialog.add(buttonSubmit);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.requestFocus();
        //dialog.setResizable(true);
       buttonSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                isNameEntered = true;
                setNameEntered(nameTxt.getText());
                System.out.println(nameEntered);
                dialog.setVisible(false);
            }
        });

    }

}

