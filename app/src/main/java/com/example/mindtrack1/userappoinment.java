package com.example.mindtrack1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class userappoinment extends AppCompatActivity {

    AutoCompleteTextView etGender, etDoctor, etTime;
    TextInputEditText etDate, etName, etAge, etMobile, etReason;
    Button btnSubmit;
    ProgressBar progressBar;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userappoinment);

        db = FirebaseFirestore.getInstance();

        // Find Views
        etGender = findViewById(R.id.et_gender);
        etDoctor = findViewById(R.id.et_doctor);
        etTime = findViewById(R.id.et_time);
        etDate = findViewById(R.id.et_date);

        etName = findViewById(R.id.et_patient_name);
        etAge = findViewById(R.id.et_patient_age);
        etMobile = findViewById(R.id.et_patient_mobile);
        etReason = findViewById(R.id.et_reason);

        btnSubmit = findViewById(R.id.btn_submit_appointment);
        progressBar = findViewById(R.id.progressBar);

        // Dropdown data
        String[] genders = {"Male", "Female", "Transgender"};
        etGender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders));

        String[] doctors = {"Doctor A", "Doctor B", "Doctor C", "Doctor D", "Doctor E", "Doctor F", "Doctor G"};
        etDoctor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, doctors));

        String[] slots = {"09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "03:00 PM", "05:00 PM"};
        etTime.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, slots));

        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Time picker
        etTime.setOnClickListener(v -> showTimePicker());

        // Submit Button Click
        btnSubmit.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE); // show loading
            saveAppointment();
        });
    }

    private void saveAppointment() {
        String name = etName.getText().toString();
        String age = etAge.getText().toString();
        String gender = etGender.getText().toString();
        String mobile = etMobile.getText().toString();
        String doctor = etDoctor.getText().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();
        String reason = etReason.getText().toString();

        if (name.isEmpty() || age.isEmpty() || gender.isEmpty() ||
                mobile.isEmpty() || doctor.isEmpty() || date.isEmpty() ||
                time.isEmpty() || reason.isEmpty()) {

            progressBar.setVisibility(View.GONE); // hide loading
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> appData = new HashMap<>();
        appData.put("name", name);
        appData.put("age", age);
        appData.put("gender", gender);
        appData.put("mobile", mobile);
        appData.put("doctor", doctor);
        appData.put("date", date);
        appData.put("time", time);
        appData.put("reason", reason);
        appData.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()); // Added
        appData.put("status", "Pending"); // Optional default status

        db.collection("appointments")
                .add(appData)
                .addOnSuccessListener(docRef -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Appointment Booked Successfully ✔", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(userappoinment.this, homepatient.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                    // Return to homepatient page
                    Intent i = new Intent(userappoinment.this, homepatient.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE); // hide loading
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    month = month + 1;
                    etDate.setText(day + "-" + month + "-" + year);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);

        new TimePickerDialog(this,
                (v, hour, minute) -> {
                    String ampm = (hour >= 12) ? "PM" : "AM";
                    int formattedHour = (hour == 0 || hour == 12) ? 12 : hour % 12;
                    etTime.setText(formattedHour + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampm);
                }, h, m, false).show();
    }

}
