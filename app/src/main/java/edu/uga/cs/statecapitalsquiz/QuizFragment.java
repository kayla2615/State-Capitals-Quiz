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

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";
    private TextView question;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private int score = 0;
    private boolean answerRecorded = false;

    // Date and time when quiz was submitted
    private String quizSubmissionDateTime;

    private int questionNum = 1;
    
    private Random random = new Random();

    // quizData currently initialized with Fake quiz data. Need to get from database.
    private String[][] quizData = {
            {"Alabama", "Montgomery", "Birmingham", "Mobile", ""},
            {"California", "Sacramento", "Los Angeles", "San Francisco", ""},
            {"Texas", "Austin", "Houston", "San Antonio", ""},
            {"Florida", "Tallahassee", "Jacksonville", "Miami", ""},
            {"New York", "Albany", "Buffalo", "Yonkers", ""},
            {"Illinois", "Springfield", "Chicago", "Aurora", ""}
    };

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        Log.d( TAG, "ChoiceFragment.onCreateView()" );

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }
    
    public static QuizFragment newInstance(int position ) {
        Log.d(TAG, "QuizFragment.newInstance: " + position );

        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt( "questionNum", position );
        fragment.setArguments(args);
        return fragment;
    }

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
     * Check if the user's answer is correct by comparing directly with quizData
     * Updates the quizData last column and correct answers count
     * @return true if correct, false if incorrect
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

    public boolean isAnswerRecorded() {
        return answerRecorded;
    }

    public void setQuizSubmissionDateTime(String quizSubmissionDateTime) {
        this.quizSubmissionDateTime = quizSubmissionDateTime;
    }
}
