package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class registerdoctor extends AppCompatActivity {

    private EditText nameEt, emailEt, passwordEt, repasswordEt, hospitalEt, locationEt, degreeEt;
    private Button registerBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerdoctor);

        // Initialize views
        nameEt = findViewById(R.id.et_doctor_name);
        emailEt = findViewById(R.id.et_doctor_email);
        passwordEt = findViewById(R.id.et_doctor_password);
        repasswordEt = findViewById(R.id.et_doctor_repassword);
        hospitalEt = findViewById(R.id.et_hospital_name);
        locationEt = findViewById(R.id.et_location);
        degreeEt = findViewById(R.id.et_doctor_degree);
        registerBtn = findViewById(R.id.btn_register_doctor);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> {
            String name = nameEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();
            String repassword = repasswordEt.getText().toString().trim();

            // Validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(repassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create user in Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            // Go back to doctor login screen
                            startActivity(new Intent(this, doctorloginactivity.class));
                            finish();
                        } else {
                            Toast.makeText(this,
                                    "Registration Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
