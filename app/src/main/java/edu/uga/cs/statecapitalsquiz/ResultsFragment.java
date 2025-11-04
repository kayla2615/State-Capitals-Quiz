package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * ResultsFragment displays a scrollable history of past quiz results.
 */
public class ResultsFragment extends Fragment {

    private static final String TAG = "ResultsFragment";

    private LinearLayout quizHistoryLayout;
    private QuizData quizData;

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "ResultsFragment.onCreateView()");
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizHistoryLayout = view.findViewById(R.id.quizHistoryLayout);

        quizData = new QuizData(getActivity());
        quizData.open();

        displayQuizHistory();
    }

    private void displayQuizHistory() {
        List < Quiz > quizzes = quizData.retrieveAllQuizDatas();
        Log.d(TAG, "Number of quizzes retrieved: " + quizzes.size());

        if (quizzes.isEmpty()) {
            TextView noDataText = new TextView(getActivity());
            noDataText.setText("No quizzes taken yet.");
            quizHistoryLayout.addView(noDataText);
            return;
        }

        for (Quiz quiz: quizzes) {
            TextView quizText = new TextView(getActivity());
            quizText.setText(quiz.getQuizDate() + " - Score: " + quiz.getQuizScore() + "/6");
            quizText.setTextSize(18);
            quizText.setPadding(10, 10, 10, 10);
            quizHistoryLayout.addView(quizText);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (quizData != null && quizData.isDBOpen()) quizData.close();
    }
}