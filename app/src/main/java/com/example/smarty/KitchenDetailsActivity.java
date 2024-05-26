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

public class KitchenDetailsActivity extends AppCompatActivity {

    private TextView tvGasDetected;
    private TextView tvGasValue;
    private TextView tvFlameDetected;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_details);

        tvGasDetected = findViewById(R.id.tvGasDetected);
        tvGasValue = findViewById(R.id.tvGasValue);
        tvFlameDetected = findViewById(R.id.tvFlameDetected);

        // Initialisation des références de la base de données
        databaseReference = FirebaseDatabase.getInstance().getReference().child("smart-Home");

        databaseReference.child("Gas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean gasDetected = dataSnapshot.child("gasDetected").getValue(Boolean.class);
                int gasValue = dataSnapshot.child("value").getValue(Integer.class);

                tvGasDetected.setText("Gas Detected: " + (gasDetected ? "Yes" : "No"));
                tvGasValue.setText("Gas Value: " + gasValue);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        databaseReference.child("flame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flameDetected = dataSnapshot.child("flameDetected").getValue(Boolean.class);
                tvFlameDetected.setText("Flame Detected: " + (flameDetected ? "Yes" : "No"));
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
                Intent intent = new Intent(KitchenDetailsActivity.this, DevicesActivity.class);
                startActivity(intent);
            }
        });

    }
}
