package database;

import common.Question;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QuizDao {
    public ArrayList<Question> fetchQuestions() throws SQLException;
    public int addQuestion(Question questions[]) throws SQLException;
    public int removeQuestion(Question question);
    public int updateQuestion(Question question);
    public Question fetchSpecific(int id) throws SQLException;
}
