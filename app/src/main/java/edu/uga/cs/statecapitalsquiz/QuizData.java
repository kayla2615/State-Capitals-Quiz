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
 * This class is facilitates storing and restoring job leads stored.
 */
public class QuizData {

    public static final String DEBUG_TAG = "QuizData";

    private SQLiteDatabase db;
    private static SQLiteOpenHelper quizDataDBHelper;
    private static final String[] allColumns = {
            QuizDataDBHelper.QUIZDATA_COLUMN_ID,
            QuizDataDBHelper.QUIZDATA_COLUMN_QUIZDATE,
            QuizDataDBHelper.QUIZDATA_COLUMN_QUIZSCORE,
    };

    public QuizData(Context context) {
        quizDataDBHelper = QuizDataDBHelper.getInstance(context);
    }

    // Open the database
    public void open() {
        db = quizDataDBHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "QuizData: db open");
    }

    // Close the database
    public void close() {
        if (quizDataDBHelper != null) {
            quizDataDBHelper.close();
            Log.d(DEBUG_TAG, "QuizData: db closed");
        }
    }

    public boolean isDBOpen() {
        return db.isOpen();
    }

    public List < Quiz > retrieveAllQuizDatas() {
        ArrayList < Quiz > quizDatas = new ArrayList < > ();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query(QuizDataDBHelper.TABLE_QUIZDATA, allColumns,
                    null, null, null, null, null);

            // collect all quiz results into a List
            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    if (cursor.getColumnCount() >= 3) {

                        // get all attribute values of this quiz result
                        columnIndex = cursor.getColumnIndex(QuizDataDBHelper.QUIZDATA_COLUMN_ID);
                        long id = cursor.getLong(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDataDBHelper.QUIZDATA_COLUMN_QUIZDATE);
                        String quizDate = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDataDBHelper.QUIZDATA_COLUMN_QUIZSCORE);
                        int quizScore = cursor.getInt(columnIndex);

                        // create a new QuizData object and set its state to the retrieved values
                        Quiz quiz = new Quiz(quizDate, quizScore);
                        quiz.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        quizDatas.add(quiz);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved quiz results
        return quizDatas;
    }

    public Quiz storeQuizData(Quiz quiz) {

        ContentValues values = new ContentValues();
        values.put(QuizDataDBHelper.QUIZDATA_COLUMN_QUIZDATE, quiz.getQuizDate());
        values.put(QuizDataDBHelper.QUIZDATA_COLUMN_QUIZSCORE, quiz.getQuizScore());

        long id = db.insert(QuizDataDBHelper.TABLE_QUIZDATA, null, values);

        quiz.setId(id);

        return quiz;
    }

}