package edu.uga.cs.statecapitalsquiz;

/**
 * This class (a POJO) represents a single job lead, including the id, company name,
 * phone number, URL, and some comments.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class CurrentQuiz {

    private long   id;
    private long questionid;
    private int selectedAnswer;

    public CurrentQuiz()
    {
        this.id = -1;
        this.questionid = -1;
        this.selectedAnswer = -1;
    }

    public CurrentQuiz(long questionid, int selectedAnswer) {
        this.id = -1;  // the primary key id will be set by a setter method
        this.questionid = -1;
        this.selectedAnswer = -1;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getQuestionid()
    {
        return questionid;
    }

    public void setQuestionid(long questionid)
    {
        this.questionid = questionid;
    }

    public int getSelectedAnswer()
    {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer)
    {
        this.selectedAnswer = selectedAnswer;
    }



    public String toString()
    {
        return id + ": " + questionid + " " + selectedAnswer;
    }
}
