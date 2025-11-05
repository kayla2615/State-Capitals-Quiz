package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class facilitates storing and restoring quiz questions in the database.
 */
public class QuestionData {

    public static final String DEBUG_TAG = "Question";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private static SQLiteOpenHelper questionDataDBHelper;
    private static final String[] allColumns = {
            QuestionDataDBHelper.QUESTIONDATA_COLUMN_ID,
            QuestionDataDBHelper.QUESTIONDATA_COLUMN_STATE,
            QuestionDataDBHelper.QUESTIONDATA_COLUMN_CAPITALCITY,
            QuestionDataDBHelper.QUESTIONDATA_COLUMN_SECONDCITY,
            QuestionDataDBHelper.QUESTIONDATA_COLUMN_THIRDCITY
    };

    public QuestionData(Context context) {
        // QuizDataDBHelper = QuizDataDBHelper.getInstance( context );
        questionDataDBHelper = QuestionDataDBHelper.getInstance(context);
    }

    // Open the database
    public void open() {
        db = questionDataDBHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "CurrentQuizData: db open");
    }

    // Close the database
    public void close() {
        if (questionDataDBHelper != null) {
            questionDataDBHelper.close();
            Log.d(DEBUG_TAG, "CurrentQuizData: db closed");
        }
    }

    public boolean isDBOpen() {
        return db.isOpen();
    }

    public Question getQuestionById(List < Question > questionList, long id) {
        for (Question question: questionList) {
            if (question.getId() == id) {
                return question;
            }
        }
        return questionList.get(0);
    }

    public List < Question > getQuestion() {
        ArrayList < Question > questionData = new ArrayList < > ();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query(QuestionDataDBHelper.TABLE_QUESTIONDATA, allColumns,
                    null, null, null, null, null);

            // collect all questions into a List
            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    if (cursor.getColumnCount() >= 5) {

                        // get all attribute values of this question
                        columnIndex = cursor.getColumnIndex(QuestionDataDBHelper.QUESTIONDATA_COLUMN_ID);
                        long id = cursor.getLong(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuestionDataDBHelper.QUESTIONDATA_COLUMN_STATE);
                        String state = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuestionDataDBHelper.QUESTIONDATA_COLUMN_CAPITALCITY);
                        String capitalCity = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuestionDataDBHelper.QUESTIONDATA_COLUMN_SECONDCITY);
                        String secondCity = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuestionDataDBHelper.QUESTIONDATA_COLUMN_THIRDCITY);
                        String thirdCity = cursor.getString(columnIndex);
                        // create a new QuizData object and set its state to the retrieved values
                        Question question = new Question(state, capitalCity, secondCity, thirdCity);
                        question.setId(id);
                        questionData.add(question);
                    }
                }
            }
            if (cursor != null)
                Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
            else
                Log.d(DEBUG_TAG, "Number of records from DB: 0");
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved questions
        return questionData;
    }

    /**
     * Stores a new question in the database.
     *
     * @param question The Question object to store
     * @return The Question object with its ID set to the database primary key
     */
    public Question storeQuestionData(Question question) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the QuizData argument.
        // This is how we are providing persistence to a QuizData (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put(QuestionDataDBHelper.QUESTIONDATA_COLUMN_STATE, question.getState());
        values.put(QuestionDataDBHelper.QUESTIONDATA_COLUMN_CAPITALCITY, question.getCapitalCity());
        values.put(QuestionDataDBHelper.QUESTIONDATA_COLUMN_SECONDCITY, question.getSecondCity());
        values.put(QuestionDataDBHelper.QUESTIONDATA_COLUMN_THIRDCITY, question.getThirdCity());
        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert(QuestionDataDBHelper.TABLE_QUESTIONDATA, null, values);

        question.setId(id);

        return question;
    }

}