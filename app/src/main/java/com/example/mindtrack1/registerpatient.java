package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class registerpatient extends AppCompatActivity {

    private EditText nameEt, mobileEt, emailEt, passwordEt, repasswordEt, locationEt;
    private Button registerBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpatient);

        // Initialize fields
        nameEt = findViewById(R.id.et_patient_name);
        mobileEt = findViewById(R.id.et_patient_mobile);
        emailEt = findViewById(R.id.et_patient_email);
        passwordEt = findViewById(R.id.et_patient_password);
        repasswordEt = findViewById(R.id.et_patient_repassword);
        locationEt = findViewById(R.id.et_patient_location);
        registerBtn = findViewById(R.id.btn_register_patient);

        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> {
            String name = nameEt.getText().toString().trim();
            String mobile = mobileEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();
            String repassword = repasswordEt.getText().toString().trim();
            String location = locationEt.getText().toString().trim();

            // Validation
            if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(repassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register user with Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, patientloginactivity.class)); // redirect after success
                            finish();
                        } else {
                            Toast.makeText(this, "Registration Failed: " +
                                    task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
