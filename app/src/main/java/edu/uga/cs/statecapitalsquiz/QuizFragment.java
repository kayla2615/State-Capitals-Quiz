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

import java.util.ArrayList;
import java.util.List;
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
     * The question number
     */
    private int questionNum = 1;

    /**
     * Random number generator for shuffling answer options
     */
    private Random random = new Random();

    /**
     * Required empty public constructor for fragment instantiation
     */
    public QuizFragment() {
        // Required empty public constructor
    }

    private ArrayList < CurrentQuiz > currentQuiz;

    private CurrentQuizData currentQuizData = null;

    private ArrayList < Question > questionList;

    private QuestionData questionData = null;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state
     * @return The View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "ChoiceFragment.onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    /**
     * Method to create a new instance of QuizFragment for a specific question position.
     *
     * @param position The question number (0-based index) for this fragment
     * @return A new instance of QuizFragment configured for the specified question
     */
    public static QuizFragment newInstance(int position) {
        Log.d(TAG, "QuizFragment.newInstance: " + position);

        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt("questionNum", position);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes the UI components, displays the
     * question, and randomly shuffles the answer options.
     *
     * @param view               The View returned by onCreateView
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "QuizFragment.onViewCreated()");

        questionList = new ArrayList < > ();
        questionData = new QuestionData(getActivity());
        currentQuiz = new ArrayList < > ();
        currentQuizData = new CurrentQuizData(getActivity());
        currentQuizData.open();
        questionData.open();

        question = view.findViewById(R.id.questionText);
        radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton2 = view.findViewById(R.id.radioButton2);
        radioButton3 = view.findViewById(R.id.radioButton3);

        // Get question number
        Bundle args = getArguments();
        if (args != null) {
            questionNum = args.getInt("questionNum", 0);
        }

        // Start by loading the questions
        new questionDBReader().execute();
    }

    /**
     * Checks if the user's selected answer is correct by comparing it with the correct
     * capital stored in quizData. Updates the quizData last column with "1" for correct
     * or "0" for incorrect, and increments the score if the answer is correct.
     * This method only records the answer once per question (enforced by answerRecorded flag).
     */
    public void checkAnswer() {
        if (currentQuiz.isEmpty()) {
            Log.e(TAG, "ERROR: currentQuiz is empty when checking answer!");
            return;
        }
        if (questionNum >= currentQuiz.size()) {
            Log.e(TAG, "ERROR: questionNum out of range (" + questionNum + ")");
            return;
        }

        CurrentQuiz quiz = currentQuiz.get(questionNum);

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
            currentQuiz.get(questionNum).setSelectedAnswer(0); // Mark as incorrect in last column
            answerRecorded = true;
            return;
        }

        Question currentQuestion = questionData.getQuestionById(questionList, currentQuiz.get(questionNum).getQuestionid());
        // Put 1 in the data's last column for correct and 0 for incorrect
        if (answer.equals(currentQuestion.getCapitalCity())) {
            score++;
            currentQuiz.get(questionNum).setSelectedAnswer(0);
            answerRecorded = true;
            return;
        } else {
            currentQuiz.get(questionNum).setSelectedAnswer(1);
            answerRecorded = true;
            return;
        }
    }

    /**
     * Get the question number for this fragment
     *
     * @return question number (0-based)
     */
    public int getQuestionNum() {
        return questionNum;
    }

    /**
     * Get the number of correct answers
     *
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

    private class currentQuizDBReader extends AsyncTask < Void, List < CurrentQuiz >> {
        @Override
        protected List < CurrentQuiz > doInBackground(Void...params) {
            return currentQuizData.getCurrentQuiz();
        }

        @Override
        protected void onPostExecute(List < CurrentQuiz > currentQuizNew) {

            if (currentQuizNew.isEmpty()) {
                makeQuiz(6);
            } else {
                currentQuiz = (ArrayList < CurrentQuiz > ) currentQuizNew;
            }

            if (!currentQuiz.isEmpty() && !questionList.isEmpty()) {
                displayQuestion();
            } else {
                Log.e(TAG, "Cannot display question – data not loaded");
            }
        }

        private void makeQuiz(int questions) {
            if (questionList == null || questionList.isEmpty()) return;

            Random rand = new Random();
            List < Question > tempList = new ArrayList < > (questionList); // make a copy to remove from
            for (int i = 0; i < Math.min(questions, tempList.size()); i++) {
                int temp = rand.nextInt(tempList.size());

                Question q = tempList.get(temp);
                currentQuizData.storeQuizData(new CurrentQuiz(q.getId(), 0));

                tempList.remove(temp);
            }
            currentQuiz.clear();
            currentQuiz.addAll(currentQuizData.getCurrentQuiz());
        }

    }
    private void displayQuestion() {
        Question currentQuestion = questionData.getQuestionById(
                questionList,
                currentQuiz.get(questionNum).getQuestionid()
        );

        question.setText("What is the capital of " + currentQuestion.getState() + "?");

        int[] indices = {
                0,
                1,
                2
        };
        List < Integer > shuffled = new ArrayList < > ();
        while (shuffled.size() < 3) {
            int next = random.nextInt(3);
            if (!shuffled.contains(next)) shuffled.add(next);
        }

        radioButton1.setText("A: " + currentQuestion.getCity(shuffled.get(0)));
        radioButton2.setText("B: " + currentQuestion.getCity(shuffled.get(1)));
        radioButton3.setText("C: " + currentQuestion.getCity(shuffled.get(2)));
    }

    private class questionDBReader extends AsyncTask < Void, List < Question >> {
        @Override
        protected List < Question > doInBackground(Void...params) {
            return questionData.getQuestion();
        }

        @Override
        protected void onPostExecute(List < Question > questionNewList) {
            questionList.addAll(questionNewList);

            if (!questionList.isEmpty()) {
                new currentQuizDBReader().execute();
            } else {
                Log.e(TAG, "No questions found in database!");
            }
        }
    }

}