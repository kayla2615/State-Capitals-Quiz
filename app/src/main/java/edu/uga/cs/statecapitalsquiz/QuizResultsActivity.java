package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class QuizResultsActivity extends AppCompatActivity {

    private static final String TAG = "QuizResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_results);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        displayQuizHistory();
    }

    private void displayQuizHistory() {
        LinearLayout historyLayout = findViewById(R.id.quizHistoryLayout);

        QuizData quizData = new QuizData(this);
        quizData.open();
        List < Quiz > allQuizzes = quizData.retrieveAllQuizDatas();
        quizData.close();

        if (allQuizzes.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No previous quizzes found.");
            historyLayout.addView(emptyText);
            return;
        }

        for (Quiz q: allQuizzes) {
            TextView quizText = new TextView(this);
            quizText.setText("Date: " + q.getQuizDate() + "  |  Score: " + q.getQuizScore() + "/6");
            quizText.setTextSize(18);
            quizText.setPadding(16, 16, 16, 16);
            historyLayout.addView(quizText);
        }
    }
}