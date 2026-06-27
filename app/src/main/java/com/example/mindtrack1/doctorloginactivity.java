package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class doctorloginactivity extends AppCompatActivity {

    private EditText emailEt, passwordEt;
    private Button loginBtn;
    private TextView tvDoctorRegister;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorloginactivity);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Link UI elements
        emailEt = findViewById(R.id.et_doctor_email);
        passwordEt = findViewById(R.id.et_doctor_password);
        loginBtn = findViewById(R.id.btn_doctor_login);
        tvDoctorRegister = findViewById(R.id.tv_doctor_register);

        // Register navigation
        tvDoctorRegister.setOnClickListener(v -> {
            Intent intent = new Intent(doctorloginactivity.this, registerdoctor.class);
            startActivity(intent);
        });

        // Login button click
        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(doctorloginactivity.this, homedoctor.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this,
                                    "Login Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
