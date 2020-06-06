package common

import java.io.Serializable

class Question(var id: Int, var question: String, var correct_answer: String, var incorrect_answers: Array<String>) : Serializable {
    var difficulty: String? = null
    var category: String? = null
    var type: String? = null

    override fun toString(): String {
        return "ClassPojo [difficulty = $difficulty, question = $question, correct_answer = $correct_answer, incorrect_answers = $incorrect_answers, category = $category, type = $type]"
    }

}