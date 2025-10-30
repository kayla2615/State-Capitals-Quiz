package edu.uga.cs.statecapitalsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private ViewPager2 pager;
    private QuizPagerAdapter adapter;
    private int currentScore = 0;
    private int totalQuestions = 6;
    private boolean userTriedToSwipePastEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pager = findViewById( R.id.viewpager );
        adapter = new QuizPagerAdapter(getSupportFragmentManager(), getLifecycle() );
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL );
        pager.setAdapter( adapter );
        
        // Add page change listener to detect swipes and record answers
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "Page selected: " + position);
                
                // Record answer for the previous page when user swipes to next page
                if (position > 0) {
                    recordAnswerForPosition(position - 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                int position = pager.getCurrentItem();
                if (position == totalQuestions - 1) {
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        // User started dragging on the last page; if they drag left, this will become an overscroll
                        userTriedToSwipePastEnd = true;
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE && userTriedToSwipePastEnd) {
                        // Drag ended while still on last page → treat as attempt to go past end
                        userTriedToSwipePastEnd = false;
                        showResultsFragment();
                    }
                } else {
                    userTriedToSwipePastEnd = false;
                }
            }
        });
    }
    
    /**
     * Record the answer for a specific question position
     * @param position the question position (0-based)
     */
    private void recordAnswerForPosition(int position) {
        try {
            // Get the fragment for the specified position using the adapter
            QuizFragment quizFragment = adapter.getFragment(position);
            quizFragment.checkAnswer();
        } catch (Exception e) {
            Log.e(TAG, "Error recording answer for position " + position, e);
        }
    }
    
    /**
     * Get the current score
     * @return current score
     */
    public int getCurrentScore() {
        return currentScore;
    }
    
    /**
     * Get the total number of questions
     * @return total questions
     */
    public int getTotalQuestions() {
        return totalQuestions;
    }

    private void showResultsFragment() {
        // Ensure last question's answer is recorded
        int last = totalQuestions - 1;
        QuizFragment lastFragment = adapter.getFragment(last);
        if (lastFragment != null && !lastFragment.isAnswerRecorded()) {
            lastFragment.checkAnswer();
        }

        int score = 0;
        for (int i = 0; i < totalQuestions; i++) {
            QuizFragment fragment = adapter.getFragment(i);
            if (fragment != null) {
                score += fragment.getScore();
            }
        }

        // Destroy quiz fragments and release ViewPager adapter
        pager.setAdapter(null);
        adapter.clear();

        String submittedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        for (int i = 0; i < totalQuestions; i++) {
            QuizFragment fragment = adapter.getFragment(i);
            if (fragment != null) {
                fragment.setQuizSubmissionDateTime(submittedAt);
            }
        }
        ResultsFragment resultsFragment = ResultsFragment.newInstance(score, submittedAt);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, resultsFragment);
        transaction.commit();
    }
}