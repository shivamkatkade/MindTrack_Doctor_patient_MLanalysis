package com.example.mindtrack1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class appoinmentstatusdoctor extends AppCompatActivity {

    RecyclerView appointmentsRecycler;
    List<Appointment> appointmentList;
    AppointmentAdapter adapter;

    FirebaseFirestore db;
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinmentstatusdoctor);

        db = FirebaseFirestore.getInstance();

        appointmentsRecycler = findViewById(R.id.appointmentsRecycler);
        appointmentsRecycler.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList);
        appointmentsRecycler.setAdapter(adapter);

        loadAppointments();
    }

    private void loadAppointments() {
        listenerRegistration = db.collection("appointments")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        appointmentList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            String id = doc.getId();
                            String name = doc.getString("name");
                            String mobile = doc.getString("mobile");
                            String date = doc.getString("date");
                            String time = doc.getString("time");
                            String reason = doc.getString("reason");
                            String status = doc.getString("status");
                            if (status == null) status = "Pending";

                            // Avoid null fields
                            if (name == null) name = "Unknown";
                            if (mobile == null) mobile = "N/A";
                            if (date == null) date = "N/A";
                            if (time == null) time = "N/A";
                            if (reason == null) reason = "N/A";

                            appointmentList.add(new Appointment(id, name, mobile, date, time, reason, status));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) listenerRegistration.remove();
    }

    // Appointment Model
    public static class Appointment {
        private String docId, patientName, mobile, date, time, reason, status;

        public Appointment(String docId, String patientName, String mobile,
                           String date, String time, String reason, String status) {
            this.docId = docId;
            this.patientName = patientName;
            this.mobile = mobile;
            this.date = date;
            this.time = time;
            this.reason = reason;
            this.status = status;
        }

        public String getDocId() { return docId; }
        public String getPatientName() { return patientName; }
        public String getMobile() { return mobile; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getReason() { return reason; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // RecyclerView Adapter
    public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

        private List<Appointment> appointments;

        public AppointmentAdapter(List<Appointment> appointments) {
            this.appointments = appointments;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_appointment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Appointment appointment = appointments.get(position);

            holder.tvPatientName.setText(appointment.getPatientName());
            holder.tvPatientMobile.setText("Mobile: " + appointment.getMobile());
            holder.tvAppointmentDate.setText("Date: " + appointment.getDate());
            holder.tvAppointmentTime.setText("Time: " + appointment.getTime());
            holder.tvReason.setText("Reason: " + appointment.getReason());
            holder.tvPatientStatus.setText(appointment.getStatus());

            updateStatusColor(holder, appointment.getStatus());

            holder.btnAccept.setOnClickListener(v -> updateAppointmentStatus(appointment, "Accepted", holder));
            holder.btnReject.setOnClickListener(v -> updateAppointmentStatus(appointment, "Rejected", holder));
            holder.btnPending.setOnClickListener(v -> updateAppointmentStatus(appointment, "Pending", holder));
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        private void updateAppointmentStatus(Appointment appointment, String newStatus, ViewHolder holder) {
            db.collection("appointments").document(appointment.getDocId())
                    .update("status", newStatus)
                    .addOnSuccessListener(aVoid -> {
                        appointment.setStatus(newStatus);
                        holder.tvPatientStatus.setText(newStatus);
                        updateStatusColor(holder, newStatus);
                    })
                    .addOnFailureListener(e -> Toast.makeText(appoinmentstatusdoctor.this,
                            "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        private void updateStatusColor(ViewHolder holder, String status) {
            switch (status) {
                case "Accepted":
                    holder.tvPatientStatus.setTextColor(0xFF4CAF50); // green
                    break;
                case "Rejected":
                    holder.tvPatientStatus.setTextColor(0xFFF44336); // red
                    break;
                default:
                    holder.tvPatientStatus.setTextColor(0xFFFFC107); // amber
                    break;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvPatientName, tvPatientMobile, tvAppointmentDate, tvAppointmentTime, tvReason, tvPatientStatus;
            Button btnAccept, btnReject, btnPending;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvPatientName = itemView.findViewById(R.id.tvPatientName);
                tvPatientMobile = itemView.findViewById(R.id.tvPatientMobile);
                tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
                tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
                tvReason = itemView.findViewById(R.id.tvReason);
                tvPatientStatus = itemView.findViewById(R.id.tvPatientStatus);

                btnAccept = itemView.findViewById(R.id.btnAccept);
                btnReject = itemView.findViewById(R.id.btnReject);
                btnPending = itemView.findViewById(R.id.btnPending);
            }
        }
    }
}
