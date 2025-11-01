package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * ResultsFragment displays the quiz results to the user after completing a quiz.
 * It shows the score and provides a button to return to the main menu.
 */
public class ResultsFragment extends Fragment {

    private static final String TAG = "ResultsFragment";

    /**
     * TextView that displays the quiz score
     */
    private TextView score;

    /**
     * Button to navigate back to the main menu
     */
    private Button mainMenuButton;
    
    /**
     * Timestamp when the quiz was submitted
     */
    private String submittedAt;

    /**
     * The score value (number of correct answers)
     */
    private int scoreNum;

    /**
     * Required empty public constructor for fragment instantiation
     */
    public ResultsFragment() {
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
        Log.d( TAG, "ResultsFragment.onCreateView()" );

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    /**
     * Method to create a new instance of ResultsFragment with the provided score
     * and submission timestamp.
     *
     * @param score The number of correct answers (out of 6)
     * @param submittedAt The timestamp when the quiz was submitted
     * @return A new instance of ResultsFragment
     */
    public static ResultsFragment newInstance(int score, String submittedAt ) {
        Log.d(TAG, "ResultsFragment.newInstance: " + score + ", submittedAt=" + submittedAt );

        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putInt( "score", score );
        args.putString( "submittedAt", submittedAt );
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state
     * has been restored into the view. Initializes the UI components and displays
     * the quiz score and submission timestamp.
     *
     * @param view The View returned by onCreateView
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d( TAG, "QuizFragment.onViewCreated()" );
        super.onViewCreated(view, savedInstanceState);

        // Get the score from arguments
        Bundle args = getArguments();
        if (args != null) {
            scoreNum = args.getInt("score", 0);
            submittedAt = args.getString("submittedAt", null);
        }

        score = view.findViewById( R.id.textView5 );
        score.setText(scoreNum + "/6");

        mainMenuButton = view.findViewById( R.id.button3 );
        mainMenuButton.setOnClickListener(new ButtonClickListener());
        if (submittedAt != null) {
            Log.d(TAG, "Quiz submitted at: " + submittedAt);
        }
    }

    /**
     * Handles the main menu button click event.
     */
    private class ButtonClickListener implements View.OnClickListener {
        /**
         * Starts MainActivity to return the user to the main menu.
         *
         * @param view The view that was clicked
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
