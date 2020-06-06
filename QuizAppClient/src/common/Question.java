package common;

import java.io.Serializable;

public class Question implements Serializable
{
    private String difficulty;
    private int id;
    private String question;

    private String correct_answer;

    private String[] incorrect_answers;

    private String category;

    private String type;

    public Question(int id, String question, String correct_answer, String[] incorrect_answers) {
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDifficulty ()
    {
        return difficulty;
    }

    public void setDifficulty (String difficulty)
    {
        this.difficulty = difficulty;
    }

    public String getQuestion ()
    {
        return question;
    }

    public void setQuestion (String question)
    {
        this.question = question;
    }

    public String getCorrect_answer ()
    {
        return correct_answer;
    }

    public void setCorrect_answer (String correct_answer)
    {
        this.correct_answer = correct_answer;
    }

    public String[] getIncorrect_answers ()
    {
        return incorrect_answers;
    }

    public void setIncorrect_answers (String[] incorrect_answers)
    {
        this.incorrect_answers = incorrect_answers;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [difficulty = "+difficulty+", question = "+question+", correct_answer = "+correct_answer+", incorrect_answers = "+incorrect_answers+", category = "+category+", type = "+type+"]";
    }
}
