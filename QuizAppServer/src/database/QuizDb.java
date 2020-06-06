package database;

import common.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class QuizDb implements QuizDao{
    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private final String URL = "jdbc:mysql://localhost:3306/quizapp";
    private Connection connection;

    public QuizDb() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Override
    public ArrayList<Question> fetchQuestions() throws SQLException {
        ArrayList<Question> mainArrayList = new ArrayList<>();
        String query = "Select * from questions";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String question = resultSet.getString("questions");
            String option1 = resultSet.getString("option1");
            String option2 = resultSet.getString("option2");
            String option3 = resultSet.getString("option3");
            String option4 = resultSet.getString("option4");
            String answer = resultSet.getString("answer");

            mainArrayList.add(new Question(id, question,answer, new String[]{option1, option2, option3, option4}));
        }
        return mainArrayList;
    }

    @Override
    public int addQuestion(Question questions[]) throws SQLException {
        String query = "Insert into questions (questions,answer,option1, option2, option3, option4) values(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for(Question question:questions){
            preparedStatement.setString(1, question.getQuestion());
            preparedStatement.setString(2, question.getCorrect_answer());

            String options[] = question.getIncorrect_answers();
            int pos = new Random().nextInt(4);
            int k=0;
            for(int i=0; i<4; i++){
                if(i==pos){
                    preparedStatement.setString(i+3, question.getCorrect_answer());
                }else{
                    preparedStatement.setString(i+3, question.getIncorrect_answers()[k]);
                    k++;
                }
            }

            preparedStatement.execute();
        }
        return 0;
    }

    @Override
    public int removeQuestion(Question main) {
        return 0;
    }

    @Override
    public int updateQuestion(Question main) {
        return 0;
    }

    @Override
    public Question fetchSpecific(int id) throws SQLException {
        Question question;
        String query = "Select * from questions where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        String questions = resultSet.getString("questions");
        String option1 = resultSet.getString("option1");
        String option2 = resultSet.getString("option2");
        String option3 = resultSet.getString("option3");
        String option4 = resultSet.getString("option4");
        String answer = resultSet.getString("answer");

        question = new Question(id, questions,answer, new String[]{option1, option2, option3, option4});
        return  question;
    }
}
