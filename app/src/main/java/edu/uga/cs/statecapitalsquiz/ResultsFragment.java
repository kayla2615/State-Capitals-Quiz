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

public class ResultsFragment extends Fragment {

    private static final String TAG = "ResultsFragment";

    private TextView score;

    private Button mainMenuButton;
    private String submittedAt;

    private int scoreNum;

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        Log.d( TAG, "ResultsFragment.onCreateView()" );

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    public static ResultsFragment newInstance(int score, String submittedAt ) {
        Log.d(TAG, "ResultsFragment.newInstance: " + score + ", submittedAt=" + submittedAt );

        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putInt( "score", score );
        args.putString( "submittedAt", submittedAt );
        fragment.setArguments(args);
        return fragment;
    }

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

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
