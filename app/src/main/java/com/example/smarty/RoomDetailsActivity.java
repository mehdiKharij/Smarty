package com.example.smarty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomDetailsActivity extends AppCompatActivity {

    private TextView tvHumidity;
    private TextView tvLightState;
    private TextView tvTemperature;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        tvHumidity = findViewById(R.id.tvHumidity);
        tvLightState = findViewById(R.id.tvLightState);
        tvTemperature = findViewById(R.id.tvTemperature);

        // Initialisation de la référence de la base de données pour la chambre
        databaseReference = FirebaseDatabase.getInstance().getReference().child("smart-Home").child("chambre");

        // Ajout d'un ValueEventListener pour obtenir les données
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int humidity = dataSnapshot.child("humidity").getValue(Integer.class);
                String lightState = dataSnapshot.child("lightState").getValue(String.class);
                double temperature = dataSnapshot.child("temperature").getValue(Double.class);

                tvHumidity.setText( humidity + "%");
                tvLightState.setText("Light State: " + lightState);
                tvTemperature.setText(temperature + "°C");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetailsActivity.this, DevicesActivity.class);
                startActivity(intent);
            }
        });
    }
}
