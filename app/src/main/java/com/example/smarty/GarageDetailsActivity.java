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

public class GarageDetailsActivity extends AppCompatActivity {

    private TextView tvMovementDetected;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_details);

        tvMovementDetected = findViewById(R.id.tvMovementDetected);

        // Initialisation de la référence de la base de données pour le garage
        databaseReference = FirebaseDatabase.getInstance().getReference().child("smart-Home").child("movement");

        // Ajout d'un ValueEventListener pour obtenir les données
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean movementDetected = dataSnapshot.child("movementDetecte").getValue(Boolean.class);
                tvMovementDetected.setText("Mouvement : " + movementDetected);
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
                Intent intent = new Intent(GarageDetailsActivity.this, RoomDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
