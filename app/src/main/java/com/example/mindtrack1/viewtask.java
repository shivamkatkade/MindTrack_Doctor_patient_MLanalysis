package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class viewtask extends AppCompatActivity {

    FirebaseFirestore db;
    LinearLayout taskContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtask);

        db = FirebaseFirestore.getInstance();
        taskContainer = findViewById(R.id.taskContainer);

        loadTasks();
    }

    private void loadTasks() {
        db.collection("Tasks")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(query -> {

                    if (query.isEmpty()) {
                        Toast.makeText(this, "No tasks found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    taskContainer.removeAllViews();

                    for (QueryDocumentSnapshot doc : query) {

                        addTaskCard(
                                doc.getId(),  // Pass the document ID
                                doc.getString("title"),
                                doc.getString("description"),
                                doc.getString("date"),
                                doc.getString("dueDate"),
                                doc.getString("patient"),
                                doc.getString("reference"),
                                doc.getString("extraNotes"),
                                doc.getString("reminderTime"),
                                doc.getBoolean("repeat")
                        );
                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void addTaskCard(
            String taskId, // <-- added taskId
            String title, String desc, String date, String dueDate,
            String patient, String reference, String extraNotes,
            String reminderTime, Boolean repeat
    ) {

        View card = LayoutInflater.from(this)
                .inflate(R.layout.task_card_layout, taskContainer, false);

        // Complete Task Button
        card.findViewById(R.id.btnCompleteTask).setOnClickListener(v -> {
            Intent intent = new Intent(viewtask.this, fillhomeworkuser.class);
            intent.putExtra("taskId", taskId);  // Pass the Firestore document ID
            startActivity(intent);
        });

        // IDs must match your XML exactly
        ((TextView) card.findViewById(R.id.tvTitl)).setText(title);
        ((TextView) card.findViewById(R.id.tvDescription)).setText(desc);
        ((TextView) card.findViewById(R.id.tvDate)).setText("Assigned: " + date);
        ((TextView) card.findViewById(R.id.tvDueDate)).setText("Due: " + dueDate);
        ((TextView) card.findViewById(R.id.tvPatient)).setText("For: " + patient);
        ((TextView) card.findViewById(R.id.tvReference)).setText("Reference: " + reference);
        ((TextView) card.findViewById(R.id.tvExtraNotes)).setText("Notes: " + extraNotes);
        ((TextView) card.findViewById(R.id.tvReminder)).setText("Reminder: " + reminderTime);
        ((TextView) card.findViewById(R.id.tvRepeat)).setText("Repeat: " + repeat);

        taskContainer.addView(card);
    }

}
