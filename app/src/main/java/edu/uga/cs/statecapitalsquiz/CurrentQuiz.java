package edu.uga.cs.statecapitalsquiz;

/**
 * This class represents a single question in the current quiz being taken,
 * including the id, question ID reference, and the selected answer.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class CurrentQuiz {

    private long id;
    private long questionid;
    private int selectedAnswer;

    public CurrentQuiz() {
        this.id = -1;
        this.questionid = -1;
        this.selectedAnswer = -1;
    }

    public CurrentQuiz(long questionid, int selectedAnswer) {
        this.id = -1;
        this.questionid = questionid;
        this.selectedAnswer = selectedAnswer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionid() {
        return questionid;
    }

    public void setQuestionid(long questionid) {
        this.questionid = questionid;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String toString() {
        return id + ": " + questionid + " " + selectedAnswer;
    }
}