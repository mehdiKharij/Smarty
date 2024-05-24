package com.example.smarty;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomDetailsActivity extends AppCompatActivity {

    private TextView temperatureTextView, humidityTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        temperatureTextView = findViewById(R.id.temperatureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("chambre");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Utilisez Number pour gérer différents types de données
                    Number temperature = dataSnapshot.child("temperature").getValue(Number.class);
                    Number humidity = dataSnapshot.child("humidity").getValue(Number.class);

                    if (temperature != null) {
                        temperatureTextView.setText("Temperature: " + temperature.doubleValue() + "°C");
                    }
                    if (humidity != null) {
                        humidityTextView.setText("Humidity: " + humidity.longValue() + "%");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs ici
                temperatureTextView.setText("Failed to load data");
                humidityTextView.setText("Failed to load data");
            }
        });
    }
}
