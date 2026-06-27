package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class fillhomeworkuser extends AppCompatActivity {

    AutoCompleteTextView dropdownMood;
    EditText etActivityName, etResponse, etDifficulties;
    Button btnComplete;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fillhomeworkuser);

        // Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // FIREBASE
        firestore = FirebaseFirestore.getInstance();

        // FIND VIEWS
        dropdownMood = findViewById(R.id.actMood);
        etActivityName = findViewById(R.id.etActivityName);
        etResponse = findViewById(R.id.etResponse);
        etDifficulties = findViewById(R.id.etDifficulties);
        btnComplete = findViewById(R.id.btnComplete);

        // DISABLE KEYBOARD FOR DROPDOWN
        dropdownMood.setInputType(0);
        dropdownMood.setKeyListener(null);
        dropdownMood.setThreshold(0);

        // DROPDOWN DATA
        String[] moodList = {
                "Happy",
                "Sad",
                "Stressed",
                "Excited",
                "Angry",
                "Relaxed",
                "Neutral"
        };

        // ADAPTER
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                moodList
        );

        dropdownMood.setAdapter(adapter);

        dropdownMood.setOnClickListener(v -> dropdownMood.showDropDown());

        // ⭐ SAVE TO FIREBASE ON CLICK
        btnComplete.setOnClickListener(v -> saveHomework());
    }

    private void saveHomework() {
        String activityName = etActivityName.getText().toString();
        String response = etResponse.getText().toString();
        String mood = dropdownMood.getText().toString();
        String difficulties = etDifficulties.getText().toString();

        if (activityName.isEmpty() || response.isEmpty() || mood.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // DATA MAP
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("response", response);
        map.put("mood", mood);
        map.put("difficulties", difficulties);
        map.put("timestamp", System.currentTimeMillis());
// ⭐ ADD THIS FIELD FOR STATUS
        map.put("status", "complete");
        // SAVE TO FIRESTORE COLLECTION: patienthomework
        firestore.collection("patienthomework")
                .add(map)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Saved successfully. We will inform the doctor!", Toast.LENGTH_LONG).show();

                    // GO BACK TO homepatient
                    Intent intent = new Intent(fillhomeworkuser.this, homepatient.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save. Try again!", Toast.LENGTH_SHORT).show();
                });
    }
}
