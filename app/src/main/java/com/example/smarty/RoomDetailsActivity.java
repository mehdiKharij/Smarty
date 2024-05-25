package com.example.smarty;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RoomDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        DatabaseReference humidityRef = FirebaseDatabase.getInstance().getReference().child("smart-Home").child("chambre").child("humidity");

        humidityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Extract humidity value
                    Number humidityValue = dataSnapshot.getValue(Number.class);

                    if (humidityValue != null) {
                        // Handle humidity value (e.g., update UI)
                        double humidity = humidityValue.doubleValue();
                        Log.d(TAG, "Humidity value is: " + humidity);
                    } else {
                        Log.w(TAG, "Humidity value is null");
                    }
                } else {
                    Log.w(TAG, "Humidity data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read humidity value.", error.toException());
            }
        });
    }
}
