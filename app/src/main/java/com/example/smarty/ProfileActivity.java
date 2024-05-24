package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {

    private TextView NameTextView, EmailTextView, telTextView;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();

        NameTextView = findViewById(R.id.NameTextView);
        EmailTextView = findViewById(R.id.EmailTextView);
        telTextView = findViewById(R.id.telTextView);

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Récupérer les informations de l'utilisateur à partir de Firestore
            db.collection("user")
                    .document(user.getEmail())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String email = user.getEmail();
                            String tel = documentSnapshot.getString("tel");

                            // Afficher les informations de l'utilisateur dans les TextView correspondants
                            NameTextView.setText(name);
                            EmailTextView.setText(email);
                            telTextView.setText(tel);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Gérer les erreurs de récupération des informations de l'utilisateur
                    });
        }
        // Dans votre méthode onCreate de ProfileActivity
        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });




    }



}
