package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class DevicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        // Récupérer les vues correspondant aux "boxes"
        ImageView kitchenBox = findViewById(R.id.clothingCard);
        ImageView bedroomBox = findViewById(R.id.elecImage);
        ImageView garageBox = findViewById(R.id.homeImage);
        ImageView bathroomBox = findViewById(R.id.beautyImage);

        // Ajouter un écouteur de clic pour la "box" de la cuisine
        kitchenBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer l'activité KitchenDetailsActivity lorsque la "box" de la cuisine est cliquée
                Intent intent = new Intent(DevicesActivity.this, KitchenDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Ajouter un écouteur de clic pour la "box" de la chambre à coucher
        bedroomBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer l'activité BedroomDetailsActivity lorsque la "box" de la chambre à coucher est cliquée
                Intent intent = new Intent(DevicesActivity.this, RoomDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Ajouter un écouteur de clic pour la "box" du garage
        garageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer l'activité GarageDetailsActivity lorsque la "box" du garage est cliquée
                Intent intent = new Intent(DevicesActivity.this, GarageDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Ajouter un écouteur de clic pour la "box" de la salle de bains
        bathroomBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer l'activité BathroomDetailsActivity lorsque la "box" de la salle de bains est cliquée
                Intent intent = new Intent(DevicesActivity.this, BathroomDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
