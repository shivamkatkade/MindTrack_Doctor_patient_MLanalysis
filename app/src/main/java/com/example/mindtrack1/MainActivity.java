package com.example.mindtrack1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CardView btnDoctor, btnPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Match the CardView IDs in your XML
        btnDoctor = findViewById(R.id.btn_doctor_login);
        btnPatient = findViewById(R.id.btn_patient_login);

        // Doctor Login Button
        btnDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, doctorloginactivity.class);
            startActivity(intent);
        });

        // Patient Login Button
        btnPatient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, patientloginactivity.class);
            startActivity(intent);
        });
    }
}
