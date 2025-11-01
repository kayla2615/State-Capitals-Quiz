package edu.uga.cs.statecapitalsquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        button1 = findViewById( R.id.button );
        button1.setOnClickListener( new ButtonClickListener() );

        button2 = findViewById( R.id.button2 );
        button2.setOnClickListener( new ButtonClickListener() );
    }

    /**
     * Determines which button was clicked and navigates to the appropriate activity.
     */
    private class ButtonClickListener implements
            View.OnClickListener
    {
        /**
         * Starts the activity for the button that was clicked (QuizActivity or QuizResultsActivity).
         *
         * @param view The view that was clicked
         */
        @Override
        public void onClick( View view ) {
            Intent intent;
            if (view.getId() == R.id.button) {
                // Button1 clicked
                intent = new Intent( view.getContext(), QuizActivity.class );
            } else {
                // Button2 clicked
                intent = new Intent( view.getContext(), QuizResultsActivity.class );
            } 
            startActivity( intent );
        }
    }
}


