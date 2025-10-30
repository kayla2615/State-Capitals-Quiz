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

public class MainActivity extends AppCompatActivity {
    //button to take users to an activity that starts a new quiz
    private Button button1;
    //button to take users to an activity with past quiz results
    private Button button2;

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

    private class ButtonClickListener implements
            View.OnClickListener
    {
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


