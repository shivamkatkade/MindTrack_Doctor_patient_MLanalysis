package com.example.mindtrack1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

public class homepatient extends AppCompatActivity {

    Button btnLogout;
    CardView cardBookAppointment, cardAppointmentStatus, cardViewTasks, cardMlSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepatient);

        // Logout Button
        btnLogout = findViewById(R.id.btn_logout);

        // Existing Cards
        cardBookAppointment = findViewById(R.id.card_book_appointment);
        cardAppointmentStatus = findViewById(R.id.appst);

        // New Cards
        cardViewTasks = findViewById(R.id.task);
        cardMlSuggestions = findViewById(R.id.ml_suggestion);


        // BOOK APPOINTMENT
        cardBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(homepatient.this, userappoinment.class);
            startActivity(intent);
        });

        // APPOINTMENT STATUS
        cardAppointmentStatus.setOnClickListener(v -> {
            Intent intent = new Intent(homepatient.this, statusappointmentuser.class);
            startActivity(intent);
        });

        // VIEW TASKS
        cardViewTasks.setOnClickListener(v -> {
            Intent intent = new Intent(homepatient.this, viewtask.class);
            startActivity(intent);
        });

        // ML SUGGESTIONS
        cardMlSuggestions.setOnClickListener(v -> {
            Intent intent = new Intent(homepatient.this, mlsuggestionpatient.class);
            startActivity(intent);
        });




        // LOGOUT
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(homepatient.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
