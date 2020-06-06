package database

import common.Question
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class QuizDb : QuizDao {
    private val USERNAME = "root"
    private val PASSWORD = "root"
    private val URL = "jdbc:mysql://localhost:3306/quizapp"
    private val connection: Connection

    @Throws(SQLException::class)
    override fun fetchQuestions(): ArrayList<Question?> {
        val mainArrayList = ArrayList<Question?>()
        val query = "Select * from questions"
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val question = resultSet.getString("questions")
            val option1 = resultSet.getString("option1")
            val option2 = resultSet.getString("option2")
            val option3 = resultSet.getString("option3")
            val option4 = resultSet.getString("option4")
            val answer = resultSet.getString("answer")
            mainArrayList.add(Question(id, question, answer, arrayOf(option1, option2, option3, option4)))
        }
        return mainArrayList
    }

    @Throws(SQLException::class)
    override fun addQuestion(questions: Array<Question>): Int {
        val query = "Insert into questions (questions,answer,option1, option2, option3, option4) values(?,?,?,?,?,?)"
        val preparedStatement = connection.prepareStatement(query)
        for (question in questions) {
            preparedStatement.setString(1, question.question)
            preparedStatement.setString(2, question.correct_answer)
            val options = question.incorrect_answers
            val pos = Random().nextInt(4)
            var k = 0
            for (i in 0..3) {
                if (i == pos) {
                    preparedStatement.setString(i + 3, question.correct_answer)
                } else {
                    preparedStatement.setString(i + 3, question.incorrect_answers[k])
                    k++
                }
            }
            preparedStatement.execute()
        }
        return 0
    }

    override fun removeQuestion(main: Question?): Int {
        return 0
    }

    override fun updateQuestion(main: Question?): Int {
        return 0
    }

    @Throws(SQLException::class)
    override fun fetchSpecific(id: Int): Question {
        val question: Question
        val query = "Select * from questions where id = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, id)
        val resultSet = preparedStatement.executeQuery()
        val questions = resultSet.getString("questions")
        val option1 = resultSet.getString("option1")
        val option2 = resultSet.getString("option2")
        val option3 = resultSet.getString("option3")
        val option4 = resultSet.getString("option4")
        val answer = resultSet.getString("answer")
        question = Question(id, questions, answer, arrayOf(option1, option2, option3, option4))
        return question
    }

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)
    }
}