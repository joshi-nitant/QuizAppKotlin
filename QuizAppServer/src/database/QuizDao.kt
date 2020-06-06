package database

import common.Question
import java.sql.SQLException
import java.util.*

interface QuizDao {
    @Throws(SQLException::class)
    fun fetchQuestions(): ArrayList<Question?>

    @Throws(SQLException::class)
    fun addQuestion(questions: Array<Question>): Int
    fun removeQuestion(question: Question?): Int
    fun updateQuestion(question: Question?): Int

    @Throws(SQLException::class)
    fun fetchSpecific(id: Int): Question
}