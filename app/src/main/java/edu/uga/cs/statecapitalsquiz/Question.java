package edu.uga.cs.statecapitalsquiz;

/**
 * This class (a POJO) represents a single job lead, including the id, company name,
 * phone number, URL, and some comments.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Question {

    private long id;
    private String state;
    private String capitalCity;
    private String secondCity;
    private String thirdCity;

    public Question() {
        this.id = -1;
        this.state = null;
        this.capitalCity = null;
        this.secondCity = null;
        this.thirdCity = null;
    }

    public Question(String state, String capitalCity, String secondCity, String thirdCity) {
        this.id = -1; // the primary key id will be set by a setter method
        this.state = state;
        this.capitalCity = capitalCity;
        this.secondCity = secondCity;
        this.thirdCity = thirdCity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getSecondCity() {
        return secondCity;
    }

    public void setSecondCity(String secondCity) {
        this.secondCity = secondCity;
    }

    public String getThirdCity() {
        return thirdCity;
    }

    public void setThirdCity(String thirdCity) {
        this.thirdCity = thirdCity;
    }

    public String toString() {
        return id + ": " + state + " " + capitalCity + secondCity + thirdCity;
    }

    public String getCity(int number) {
        switch (number) {
            case 0:
                return getCapitalCity();
            case 1:
                return getSecondCity();
            case 2:
                return getThirdCity();
            default:
                return getThirdCity();
        }
    }
}