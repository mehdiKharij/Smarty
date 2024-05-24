package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView NameTextView, EmailTextView, telTextView;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db = FirebaseFirestore.getInstance();

        NameTextView = view.findViewById(R.id.NameTextView);
        EmailTextView = view.findViewById(R.id.EmailTextView);
        telTextView = view.findViewById(R.id.telTextView);

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

        // Bouton pour ouvrir l'activité de modification de profil
        Button updateButton = view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
