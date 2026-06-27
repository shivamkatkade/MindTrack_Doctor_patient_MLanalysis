package com.example.mindtrack1;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class mlanalysisdoctor extends AppCompatActivity {

    ImageView moodPieImage, weeklyBarImage, difficultyPieImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlanalysisdoctor);

        // Find views
        moodPieImage = findViewById(R.id.moodPieImage);
        weeklyBarImage = findViewById(R.id.weeklyBarImage);
        difficultyPieImage = findViewById(R.id.difficultyPieImage);

        // Set chart images (you can put chart images in drawable folder)
        moodPieImage.setImageResource(R.drawable.piechart1);
        weeklyBarImage.setImageResource(R.drawable.patient_weekly_activity_result);
       difficultyPieImage.setImageResource(R.drawable.linechart);
    }
}
