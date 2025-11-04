package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * QuizActivity manages the quiz experience and stores results into the Quiz database.
 */
public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    private ViewPager2 pager;
    private QuizPagerAdapter adapter;
    private int totalQuestions = 6;
    private boolean userTriedToSwipePastEnd = false;

    private CurrentQuizData currentQuizData;
    private QuizData quizData;

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

        pager = findViewById(R.id.viewpager);
        adapter = new QuizPagerAdapter(getSupportFragmentManager(), getLifecycle());
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setAdapter(adapter);

        currentQuizData = new CurrentQuizData(this);
        currentQuizData.open();

        quizData = new QuizData(this);
        quizData.open();

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position > 0) recordAnswerForPosition(position - 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                int position = pager.getCurrentItem();
                if (position == totalQuestions - 1) {
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        userTriedToSwipePastEnd = true;
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE && userTriedToSwipePastEnd) {
                        userTriedToSwipePastEnd = false;
                        showResultsFragment();
                    }
                } else {
                    userTriedToSwipePastEnd = false;
                }
            }
        });
    }

    private void recordAnswerForPosition(int position) {
        try {
            QuizFragment quizFragment = adapter.getFragment(position);
            if (quizFragment != null) quizFragment.checkAnswer();
        } catch (Exception e) {
            Log.e(TAG, "Error recording answer for position " + position, e);
        }
    }

    private void showResultsFragment() {
        // Ensure last question's answer is recorded
        int last = totalQuestions - 1;
        QuizFragment lastFragment = adapter.getFragment(last);
        if (lastFragment != null && !lastFragment.isAnswerRecorded()) {
            lastFragment.checkAnswer();
        }

        // Calculate total score
        int score = 0;
        for (int i = 0; i < totalQuestions; i++) {
            QuizFragment fragment = adapter.getFragment(i);
            if (fragment != null) {
                score += fragment.getScore();
            }
        }

        // Save the quiz to the database
        String submittedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        QuizData quizData = new QuizData(this);
        quizData.open();
        Quiz newQuiz = new Quiz();
        newQuiz.setQuizDate(submittedAt);
        newQuiz.setQuizScore(score);
        quizData.storeQuizData(newQuiz);
        quizData.close();

        // Clear previous quiz answers
        CurrentQuizData currentQuizData = new CurrentQuizData(this);
        currentQuizData.open();
        List < CurrentQuiz > previousQuiz = currentQuizData.getCurrentQuiz();
        currentQuizData.clear();
        currentQuizData.close();

        // Destroy quiz fragments and release ViewPager
        pager.setAdapter(null);
        adapter.clear();

        // Show results fragment
        ResultsFragment resultsFragment = new ResultsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, resultsFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentQuizData != null && currentQuizData.isDBOpen()) currentQuizData.close();
        if (quizData != null && quizData.isDBOpen()) quizData.close();
    }
}