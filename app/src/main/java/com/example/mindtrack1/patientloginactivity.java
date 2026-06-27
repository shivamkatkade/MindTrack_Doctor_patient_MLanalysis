package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class patientloginactivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientloginactivity);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize views
        etEmail = findViewById(R.id.et_doctor_email);
        etPassword = findViewById(R.id.et_doctor_password);
        btnLogin = findViewById(R.id.btn_doctor_login);
        tvRegister = findViewById(R.id.tv_doctor_register);

        // Login button click
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase login
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(fcmTask -> {   // ✅ changed name here
                                        if (fcmTask.isSuccessful()) {

                                            String token = fcmTask.getResult();
                                            Log.d("PATIENT_TOKEN", token);

                                            // 🔥 SAVE TOKEN IN FIRESTORE
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("token", token);

                                            db.collection("users").document(userId).set(map);
                                        }
                                    });

                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(patientloginactivity.this, homepatient.class);
                            startActivity(intent);
                            finish();


                        } else {
                            Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Register text click → go to registration page
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(patientloginactivity.this, registerpatient.class);
            startActivity(intent);
        });
    }
}
