package com.example.mindtrack1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class mlsuggestionpatient extends AppCompatActivity {

    private ImageView imgLast7Days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlsuggestionpatient);

        // Get root view of the activity
        View rootView = findViewById(android.R.id.content);

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize image view
        imgLast7Days = findViewById(R.id.imgLast7Days);

        // Set image dynamically
        imgLast7Days.setImageResource(R.drawable.mood_daily_composition_7d);
    }
}
