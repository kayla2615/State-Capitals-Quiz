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

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase   db;
    private static SQLiteOpenHelper quizDataDBHelper;
    private static final String[] allColumns = {
            QuizDataDBHelper.QUIZDATA_COLUMN_ID,
            QuizDataDBHelper.QUIZDATA_COLUMN_NAME,
            QuizDataDBHelper.QUIZDATA_COLUMN_PHONE,
            QuizDataDBHelper.QUIZDATA_COLUMN_URL,
            QuizDataDBHelper.QUIZDATA_COLUMN_COMMENTS
    };

    public QuizData( Context context ) {
        // QuizDataDBHelper = QuizDataDBHelper.getInstance( context );
        quizDataDBHelper = QuizDataDBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = quizDataDBHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "QuizData: db open" );
    }

    // Close the database
    public void close() {
        if( quizDataDBHelper != null ) {
            quizDataDBHelper.close();
            Log.d(DEBUG_TAG, "QuizData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new QuizData (Java POJO object) instance and add it to the list.
    public List<Quiz> retrieveAllQuizDatas() {
        ArrayList<Quiz> quizDatas = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( QuizDataDBHelper.TABLE_QUIZDATA, allColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( QuizDataDBHelper.QUIZDATA_COLUMN_ID );
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDataDBHelper.QUIZDATA_COLUMN_NAME );
                        String name = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDataDBHelper.QUIZDATA_COLUMN_PHONE );
                        String phone = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDataDBHelper.QUIZDATA_COLUMN_URL );
                        String uri = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( QuizDataDBHelper.QUIZDATA_COLUMN_COMMENTS );
                        String comments = cursor.getString( columnIndex );

                        // create a new QuizData object and set its state to the retrieved values
                        Quiz quiz = new Quiz( name, phone, uri, comments );
                        quiz.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        quizDatas.add( quiz );
                        Log.d(DEBUG_TAG, "Retrieved QuizData: " + quiz);
                    }
                }
            }
            if( cursor != null )
                Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
            else
                Log.d( DEBUG_TAG, "Number of records from DB: 0" );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved job leads
        return quizDatas;
    }

    // Store a new job lead in the database.
    public Quiz storeQuizData( Quiz quiz ) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the QuizData argument.
        // This is how we are providing persistence to a QuizData (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put( QuizDataDBHelper.QUIZDATA_COLUMN_NAME, quiz.getCompanyName());
        values.put( QuizDataDBHelper.QUIZDATA_COLUMN_PHONE, quiz.getPhone() );
        values.put( QuizDataDBHelper.QUIZDATA_COLUMN_URL, quiz.getUrl() );
        values.put( QuizDataDBHelper.QUIZDATA_COLUMN_COMMENTS, quiz.getComments() );

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( QuizDataDBHelper.TABLE_QUIZDATA, null, values );

        // store the id (the primary key) in the QuizData instance, as it is now persistent
        quiz.setId( id );

        Log.d( DEBUG_TAG, "Stored new job lead with id: " + String.valueOf( quiz.getId() ) );

        return quiz;
    }


}
