package edu.uga.cs.statecapitalsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Random;

/**
 * QuizFragment represents a single quiz question in the State Capitals Quiz.
 * Each fragment displays a state name and three multiple-choice options (the correct
 * capital and two incorrect capitals). The fragment tracks whether an answer has been
 * recorded and maintains the score for this specific question.
 */
public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";
    
    /**
     * TextView that displays the question text
     */
    private TextView question;
    
    /**
     * First radio button option (A)
     */
    private RadioButton radioButton1;
    
    /**
     * Second radio button option (B)
     */
    private RadioButton radioButton2;
    
    /**
     * Third radio button option (C)
     */
    private RadioButton radioButton3;
    
    /**
     * Score for this question (0 or 1)
     */
    private int score = 0;
    
    /**
     * Flag indicating whether an answer has been recorded for this question
     */
    private boolean answerRecorded = false;

    /**
     * Date and time when the quiz was submitted
     */
    private String quizSubmissionDateTime;

    /**
     * The question number 
     */
    private int questionNum = 1;
    
    /**
     * Random number generator for shuffling answer options
     */
    private Random random = new Random();

    /**
     * Quiz data array containing state names and their capitals.
     * Format: {StateName, CorrectCapital, WrongOption1, WrongOption2, AnswerStatus}
     * AnswerStatus: "1" for correct, "0" for incorrect
     * Note: Currently initialized with hardcoded data. Should be retrieved from database.
     */
    private String[][] quizData = {
            {"Alabama", "Montgomery", "Birmingham", "Mobile", ""},
            {"California", "Sacramento", "Los Angeles", "San Francisco", ""},
            {"Texas", "Austin", "Houston", "San Antonio", ""},
            {"Florida", "Tallahassee", "Jacksonville", "Miami", ""},
            {"New York", "Albany", "Buffalo", "Yonkers", ""},
            {"Illinois", "Springfield", "Chicago", "Aurora", ""}
    };

    /**
     * Required empty public constructor for fragment instantiation
     */
    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state
     * @return The View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        Log.d( TAG, "ChoiceFragment.onCreateView()" );

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }
    
    /**
     * Method to create a new instance of QuizFragment for a specific question position.
     *
     * @param position The question number (0-based index) for this fragment
     * @return A new instance of QuizFragment configured for the specified question
     */
    public static QuizFragment newInstance(int position ) {
        Log.d(TAG, "QuizFragment.newInstance: " + position );

        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt( "questionNum", position );
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes the UI components, displays the
     * question, and randomly shuffles the answer options.
     *
     * @param view The View returned by onCreateView
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d( TAG, "QuizFragment.onViewCreated()" );
        super.onViewCreated(view, savedInstanceState);

        // Get the question number from arguments
        Bundle args = getArguments();
        if (args != null) {
            questionNum = args.getInt("questionNum", 0);
        }

        question = view.findViewById( R.id.questionText );
        radioButton1 = view.findViewById( R.id.radioButton1 );
        radioButton2 = view.findViewById( R.id.radioButton2 );
        radioButton3 = view.findViewById( R.id.radioButton3 );

        question.setText("What is the capital of " + quizData[questionNum][0] + "?");

        int state1;
        int state2;
        int state3;

        // Generate random indices from 1 to 3 
        state1 = random.nextInt(3) + 1; 
        state2 = random.nextInt(3) + 1;
        state3 = random.nextInt(3) + 1;

        while (state1 == state2) {
            state2 = random.nextInt(3) + 1;
        }
        while (state1 == state3 || state2 == state3) {
            state3 = random.nextInt(3) + 1;
        }

        radioButton1.setText("A: " + quizData[questionNum][state1]);
        radioButton2.setText("B: " + quizData[questionNum][state2]);
        radioButton3.setText("C: " + quizData[questionNum][state3]);

    }
    
    /**
     * Checks if the user's selected answer is correct by comparing it with the correct
     * capital stored in quizData. Updates the quizData last column with "1" for correct
     * or "0" for incorrect, and increments the score if the answer is correct.
     * This method only records the answer once per question (enforced by answerRecorded flag).
     */
    public void checkAnswer() {
        if (answerRecorded) {
            return;
        }

        // Get the selected answer
        String answer = "";
        if (radioButton1.isChecked()) {
            answer = radioButton1.getText().toString().substring(3); // Remove "A: " prefix
        } else if (radioButton2.isChecked()) {
            answer = radioButton2.getText().toString().substring(3); // Remove "B: " prefix
        } else if (radioButton3.isChecked()) {
            answer = radioButton3.getText().toString().substring(3); // Remove "C: " prefix
        }
        
        // Incorrect if no answer is selected
        if (answer.isEmpty()) {
            quizData[questionNum][4] = "0"; // Mark as incorrect in last column
            answerRecorded = true;
            return;
        }

        // Put 1 in the data's last column for correct and 0 for incorrect
        if (answer.equals(quizData[questionNum][1])) {
            score++;
            quizData[questionNum][4] = "1"; // Mark as correct
            answerRecorded = true;
            return;
        } else {
            quizData[questionNum][4] = "0"; // Mark as incorrect
            answerRecorded = true;
            return;
        }
    }
    
    /**
     * Get the question number for this fragment
     * @return question number (0-based)
     */
    public int getQuestionNum() {
        return questionNum;
    }

    
    /**
     * Get the number of correct answers
     * @return count of correct answers
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks whether an answer has been recorded for this question.
     *
     * @return true if an answer has been recorded, false otherwise
     */
    public boolean isAnswerRecorded() {
        return answerRecorded;
    }

    /**
     * Sets the date and time when the quiz was submitted.
     *
     * @param quizSubmissionDateTime The timestamp string when the quiz was submitted
     */
    public void setQuizSubmissionDateTime(String quizSubmissionDateTime) {
        this.quizSubmissionDateTime = quizSubmissionDateTime;
    }
}
