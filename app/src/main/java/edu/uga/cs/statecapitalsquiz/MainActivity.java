package edu.uga.cs.statecapitalsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity is the splash screen of the State Capitals Quiz app.
 * This activity displays the main menu with a description of the quiz and two buttons:
 * One to start a new quiz, and One to view past quiz results
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Button to take users to an activity that starts a new quiz
     */
    private Button button1;

    /**
     * Button to take users to an activity with past quiz results
     */
    private Button button2;

    private ArrayList < Question > questionList;

    private QuestionData questionData = null;

    final String TAG = "CSVReading";

    /**
     * Called when the activity is first created. Initializes the UI components
     * and sets up button click listeners for navigation.
     *
     * @param savedInstanceState this Bundle containsthe data it most
     * recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button1 = findViewById(R.id.button);
        button1.setOnClickListener(new ButtonClickListener());

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new ButtonClickListener());

        questionList = new ArrayList < Question > ();

        questionData = new QuestionData(getApplicationContext());

        questionData.open();

        new questionDBReader().execute();

    }

    /**
     * Determines which button was clicked and navigates to the appropriate activity.
     */
    private class ButtonClickListener implements
            View.OnClickListener {
        /**
         * Starts the activity for the button that was clicked (QuizActivity or QuizResultsActivity).
         *
         * @param view The view that was clicked
         */
        @Override
        public void onClick(View view) {
            Intent intent;
            if (view.getId() == R.id.button) {
                // Button1 clicked
                intent = new Intent(view.getContext(), QuizActivity.class);
            } else {
                // Button2 clicked
                intent = new Intent(view.getContext(), QuizResultsActivity.class);
            }
            startActivity(intent);
        }
    }
    private class questionDBReader extends AsyncTask < Void, List < Question >> {
        // This method will run as a background process to read from db.
        // It returns a list of retrieved JobLead objects.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreate callback (the job leads review activity is started).
        @Override
        protected List < Question > doInBackground(Void...params) {
            List < Question > questionList = questionData.getQuestion();

            return questionList;
        }

        // This method will be automatically called by Android once the db reading
        // background process is finished.  It will then create and set an adapter to provide
        // values for the RecyclerView.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute(List < Question > questionNewList) {
            questionList.addAll(questionNewList);
            if (questionList.isEmpty()) {
                try {
                    // Open the CSV data file in the assets folder
                    InputStream in_s = getAssets().open("state_capitals.csv");

                    // read the CSV data
                    CSVReader reader = new CSVReader(new InputStreamReader(in_s));
                    String[] nextRow;
                    while ((nextRow = reader.readNext()) != null) {
                        Question question = new Question(nextRow[0], nextRow[1], nextRow[2], nextRow[3]);
                        questionData.storeQuestionData(question);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }
}