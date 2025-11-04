package edu.uga.cs.statecapitalsquiz;

/**
 * This class (a POJO) represents a single job lead, including the id, company name,
 * phone number, URL, and some comments.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Quiz {

    private long id;
    private String quizDate;
    private int quizScore;

    public Quiz() {
        this.id = -1;
        this.quizDate = null;
        this.quizScore = -1;
    }

    public Quiz(String quizDate, int quizScore) {
        this.id = -1; // the primary key id will be set by a setter method
        this.quizDate = quizDate;
        this.quizScore = quizScore;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(String quizDate) {
        this.quizDate = quizDate;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public String toString() {
        return id + ": " + quizDate + " " + quizScore;
    }
}