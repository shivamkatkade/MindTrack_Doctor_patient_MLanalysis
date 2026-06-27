package com.example.mindtrack1;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewCompletedTasksActivitydoctor extends AppCompatActivity {

    LinearLayout container;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_completed_tasks_activitydoctor);

        container = findViewById(R.id.taskContainer);
        firestore = FirebaseFirestore.getInstance();

        loadCompletedTasks();
    }

    private void loadCompletedTasks() {

        firestore.collection("patienthomework")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        String activityName = doc.getString("activityName");
                        String response = doc.getString("response");
                        String mood = doc.getString("mood");
                        String difficulty = doc.getString("difficulties");

                        // --- BEAUTIFUL CARD ---
                        TextView tv = new TextView(this);
                        tv.setPadding(35, 35, 35, 35);
                        tv.setTextSize(17);
                        tv.setBackgroundColor(0xFFF1F8E9);   // Softer Green Card
                        tv.setElevation(12);

                        // LABEL COLORS (same line)
                        String activity = "<font color='#1B5E20'><b>📝 Activity:</b></font> " + activityName;
                        String resp = "<font color='#0D47A1'><b>💬 Response:</b></font> " + response;
                        String md = "<font color='#E65100'><b>🙂 Mood:</b></font> " + mood;
                        String diff = "<font color='#B71C1C'><b>⚠️ Difficulty Faced:</b></font> " + difficulty;

                        // SET TEXT (same line format)
                        tv.setText(android.text.Html.fromHtml(
                                activity + "<br><br>" +
                                        resp + "<br><br>" +
                                        md + "<br><br>" +
                                        diff
                        ));

                        tv.setTextColor(0xFF212121);

                        // CARD MARGIN
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 25, 0, 0);
                        tv.setLayoutParams(params);

                        container.addView(tv);
                    }
                });
    }
}
