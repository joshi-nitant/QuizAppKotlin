package server;

import common.Question;
import database.QuizDb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandQuestions {
    ArrayList<Question> questionArrayList;
    ArrayList<Integer> sendQuestion;
    HashMap<Integer,String> rightAnswer;
    int number = 10;

    RandQuestions(){
        questionArrayList = new ArrayList<>();
        sendQuestion = new ArrayList<>();
        try {
            questionArrayList = new QuizDb().fetchQuestions();
            rightAnswer = new HashMap<>();

            for(int i=0; i<number; i++){
                Random random = new Random();
                int index = 0;
                while(true){
                    index = random.nextInt(questionArrayList.size());
                    if(!sendQuestion.contains(index)){
                        Question question = questionArrayList.get(index);
                        rightAnswer.put(question.getId(), question.getCorrect_answer());
                        sendQuestion.add(index);
                        break;
                    }
                }


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //outputStream.writeObject(SIZE);

    }

    public int compare(HashMap<Integer, String> answerClient){

        int score = 0;
        for(Map.Entry<Integer, String> entry : answerClient.entrySet()){
            String cAns = answerClient.get(entry.getKey());
            String sAns = this.rightAnswer.get(entry.getKey());
            //System.out.println("Right = "+sAns +" Client = "+cAns);
            if(sAns.equals(cAns)){
                score++;
            }
        }
        return score;
    }

}
