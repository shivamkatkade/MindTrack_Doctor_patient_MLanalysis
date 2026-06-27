package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class patientanalysisdoctor extends AppCompatActivity {

    TextView tvCompletedCount, tvRemainingCount, tvTotalPatients;
    ProgressBar progressBar;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientanalysisdoctor);

        // IDs from XML
        tvCompletedCount = findViewById(R.id.tvCompletedCount);
        tvRemainingCount = findViewById(R.id.tvRemainingCount);
        tvTotalPatients = findViewById(R.id.tvTotalPatients);
        progressBar = findViewById(R.id.progressBar);

        firestore = FirebaseFirestore.getInstance();

        loadTaskCounts();
    }

    private void loadTaskCounts() {

        progressBar.setVisibility(View.VISIBLE); // Show progress bar

        CollectionReference ref = firestore.collection("patienthomework");

        int totalFixed = 30; // fixed total

        ref.get().addOnCompleteListener(task -> {

            progressBar.setVisibility(View.GONE); // Hide progress bar

            if (!task.isSuccessful()) return;

            int completed = task.getResult().size();  // number of documents
            int remaining = totalFixed - completed;

            if (remaining < 0) remaining = 0;

            // Update UI
            tvCompletedCount.setText(String.valueOf(completed));
            tvRemainingCount.setText(String.valueOf(remaining));
            tvTotalPatients.setText(String.valueOf(totalFixed));
        });
        findViewById(R.id.btnViewCompleted).setOnClickListener(v -> {
            startActivity(new Intent(patientanalysisdoctor.this, ViewCompletedTasksActivitydoctor.class));
        });
        findViewById(R.id.commanproblem).setOnClickListener(v -> {
            startActivity(new Intent(patientanalysisdoctor.this, commanproblemanalysisdoctor.class));
        });
        findViewById(R.id.additionaltask).setOnClickListener(v -> {
            startActivity(new Intent(patientanalysisdoctor.this, additionalanalysisdoctor.class));
        });

        findViewById(R.id.completed).setOnClickListener(v -> {
            startActivity(new Intent(patientanalysisdoctor.this, mlanalysisdoctor.class));
        });

    }
}
