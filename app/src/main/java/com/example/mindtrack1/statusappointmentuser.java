package com.example.mindtrack1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class statusappointmentuser extends AppCompatActivity {

    TextView tvName, tvMobile, tvDate, tvTime, tvReason, tvStatus;
    CardView cardStatus;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusappointmentuser);

        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvReason = findViewById(R.id.tvReason);
        tvStatus = findViewById(R.id.tvStatus);
        cardStatus = findViewById(R.id.cardStatus);

        db = FirebaseFirestore.getInstance();

        fetchLatestAppointment();
    }

    private void fetchLatestAppointment() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("appointments")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                    if (!docs.isEmpty()) {
                        DocumentSnapshot latestDoc = docs.get(0);
                        for (DocumentSnapshot doc : docs) {
                            String docDate = doc.getString("date");
                            String latestDate = latestDoc.getString("date");
                            if (docDate != null && latestDate != null && docDate.compareTo(latestDate) > 0) {
                                latestDoc = doc;
                            }
                        }

                        setAppointmentData(latestDoc);

                    } else {
                        showNoAppointment();
                    }
                })
                .addOnFailureListener(e -> showError());
    }

    private void setAppointmentData(DocumentSnapshot doc) {
        // Set patient details
        tvName.setText(doc.getString("name") != null ? doc.getString("name") : "N/A");
        tvMobile.setText("Mobile: " + (doc.getString("mobile") != null ? doc.getString("mobile") : "N/A"));
        tvDate.setText("Date: " + (doc.getString("date") != null ? doc.getString("date") : "N/A"));
        tvTime.setText("Time: " + (doc.getString("time") != null ? doc.getString("time") : "N/A"));
        tvReason.setText("Reason: " + (doc.getString("reason") != null ? doc.getString("reason") : "N/A"));

        // Set status and card color
        String status = doc.getString("status");
        if (status == null || status.isEmpty()) {
            status = "Not Seen";
        }

        // Log the status to see exactly what Firebase returns
        Log.d("StatusDebug", "Fetched status: '" + status + "'");

        // Normalize: remove all extra spaces and line breaks
        String normalizedStatus = status.trim().replace("\n", "").replace("\r", "");
        tvStatus.setText(normalizedStatus);

        switch (normalizedStatus.toLowerCase()) {
            case "accepted":
                cardStatus.setCardBackgroundColor(Color.parseColor("#4CAF50")); // Green
                tvStatus.setTextColor(Color.WHITE);
                break;
            case "rejected":
                cardStatus.setCardBackgroundColor(Color.parseColor("#F44336")); // Red
                tvStatus.setTextColor(Color.WHITE);
                break;
            case "pending":
                cardStatus.setCardBackgroundColor(Color.parseColor("#FFC107")); // Amber
                tvStatus.setTextColor(Color.BLACK);
                break;
            default: // Not Seen or unknown
                cardStatus.setCardBackgroundColor(Color.parseColor("#F44336")); // Red
                tvStatus.setTextColor(Color.WHITE);
                break;
        }
    }

    private void showNoAppointment() {
        tvStatus.setText("No Appointments");
        cardStatus.setCardBackgroundColor(Color.GRAY);
        tvStatus.setTextColor(Color.WHITE);

        tvName.setText("N/A");
        tvMobile.setText("N/A");
        tvDate.setText("N/A");
        tvTime.setText("N/A");
        tvReason.setText("N/A");
    }

    private void showError() {
        tvStatus.setText("Error fetching appointment");
        cardStatus.setCardBackgroundColor(Color.GRAY);
        tvStatus.setTextColor(Color.WHITE);
    }

}
