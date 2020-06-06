package server

import common.Question
import database.QuizDb
import java.sql.SQLException
import java.util.*

class RandQuestions internal constructor() {
    var questionArrayList: ArrayList<Question?>
    var sendQuestion: ArrayList<Int>
    var rightAnswer: HashMap<Int?, String>? = null
    var number = 10
    fun compare(answerClient: HashMap<Int?, String?>?): Int {
        var score = 0
        for ((key) in answerClient!!) {
            val cAns = answerClient[key]
            val sAns = rightAnswer!![key]
            //System.out.println("Right = "+sAns +" Client = "+cAns);
            if (sAns == cAns) {
                score++
            }
        }
        return score
    }

    init {
        questionArrayList = ArrayList()
        sendQuestion = ArrayList()
        try {
            questionArrayList = QuizDb().fetchQuestions()
            rightAnswer = HashMap()
            for (i in 0 until number) {
                val random = Random()
                var index = 0
                while (true) {
                    index = random.nextInt(questionArrayList.size)
                    if (!sendQuestion.contains(index)) {
                        val question = questionArrayList[index]!!
                        rightAnswer!![question.id] = question.correct_answer
                        sendQuestion.add(index)
                        break
                    }
                }
            }
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        //outputStream.writeObject(SIZE);
    }
}