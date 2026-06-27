package com.example.mindtrack1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Addhomeworkdoctor extends AppCompatActivity {

    AutoCompleteTextView actvPatient;
    TextInputEditText etTitle, etDescription, etDate, etDueDate, etReference, etExtraNotes;
    MaterialButton btnSetTime, btnSave, btnCancel;
    Switch switchRepeat;

    String reminderTime = "";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addhomeworkdoctor);

        db = FirebaseFirestore.getInstance();
        initViews();
        setupPatientDropdown();  // UPDATED
        setupDatePickers();
        setupTimePicker();
        setupButtons();
    }

    private void initViews() {

        actvPatient = findViewById(R.id.actvPatient);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etDueDate = findViewById(R.id.etDueDate);

        etReference = findViewById(R.id.etReference);
        etExtraNotes = findViewById(R.id.etExtraNotes);

        btnSetTime = findViewById(R.id.btnSetTime);
        switchRepeat = findViewById(R.id.switchRepeat);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    // ---------- UPDATED HERE ----------
    private void setupPatientDropdown() {

        ArrayList<String> patientList = new ArrayList<>();
        patientList.add("All Patients");
        patientList.add("Patient A");
        patientList.add("Patient B");
        patientList.add("Patient C");
        patientList.add("Patient D");
        patientList.add("Patient E");
        patientList.add("Patient F");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, patientList);

        actvPatient.setAdapter(adapter);
    }
    // -----------------------------------

    private void setupDatePickers() {

        etDate.setOnClickListener(v -> showDatePicker(etDate));
        etDueDate.setOnClickListener(v -> showDatePicker(etDueDate));
    }

    private void showDatePicker(TextInputEditText field) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    month++;
                    String date = dayOfMonth + "/" + month + "/" + year;
                    field.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void setupTimePicker() {
        btnSetTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    (view, selectedHour, selectedMinute) -> {
                        reminderTime = selectedHour + ":" +
                                String.format("%02d", selectedMinute);

                        Toast.makeText(this, "Reminder set: " + reminderTime, Toast.LENGTH_SHORT).show();
                    },
                    hour,
                    minute,
                    true
            );

            dialog.show();
        });
    }

    private void setupButtons() {

        btnSave.setOnClickListener(v -> {

            String patient = actvPatient.getText().toString();
            String title = etTitle.getText().toString();
            String desc = etDescription.getText().toString();
            String date = etDate.getText().toString();
            String dueDate = etDueDate.getText().toString();
            String reference = etReference.getText().toString();
            String extraNotes = etExtraNotes.getText().toString();
            boolean repeat = switchRepeat.isChecked();

            if (patient.isEmpty() || title.isEmpty() || date.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(this, "Fill all required fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> task = new HashMap<>();
            task.put("patient", patient);
            task.put("title", title);
            task.put("description", desc);
            task.put("date", date);
            task.put("dueDate", dueDate);
            task.put("reminderTime", reminderTime);
            task.put("reference", reference);
            task.put("extraNotes", extraNotes);
            task.put("repeat", repeat);
            task.put("timestamp", System.currentTimeMillis());

            db.collection("Tasks")
                    .add(task)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Task Saved Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
