package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

public class homedoctor extends AppCompatActivity {
    CardView cardAddTask, cardViewAppointments, cardPatientAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homedoctor);

        // Apply padding for system bars to the root layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.headerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find CardViews
        cardAddTask = findViewById(R.id.cardAddTask);
        cardViewAppointments = findViewById(R.id.cardViewAppointments);
        cardPatientAnalysis = findViewById(R.id.cardPatientAnalysis);

        // Optional: Toasts on long click
        cardAddTask.setOnLongClickListener(v -> {
            Toast.makeText(this, "Add a new task for patients", Toast.LENGTH_SHORT).show();
            return true;
        });

        cardViewAppointments.setOnLongClickListener(v -> {
            Toast.makeText(this, "View all requested appointments", Toast.LENGTH_SHORT).show();
            return true;
        });

        cardPatientAnalysis.setOnLongClickListener(v -> {
            Toast.makeText(this, "Analyze patient data", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Navigate to appoinmentstatusdoctor on click
        cardViewAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(homedoctor.this, appoinmentstatusdoctor.class);
            startActivity(intent);
        });

        // Navigate to appoinmentstatusdoctor on click
        cardAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(homedoctor.this, Addhomeworkdoctor.class);
            startActivity(intent);
        });
// Navigate to appoinmentstatusdoctor on click
        cardPatientAnalysis.setOnClickListener(v -> {
            Intent intent = new Intent(homedoctor.this, patientanalysisdoctor.class);
            startActivity(intent);
        });
    }

}
