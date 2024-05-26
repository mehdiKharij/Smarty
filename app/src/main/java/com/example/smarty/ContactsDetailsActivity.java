package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import model.Contact;
public class ContactsDetailsActivity extends AppCompatActivity {

    private Contact selectedContact;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        // Initialiser Firestore
        db = FirebaseFirestore.getInstance();

        // Récupérer les données du contact sélectionné à partir de l'intention
        selectedContact = (Contact) getIntent().getSerializableExtra("selected_contact");

        // Afficher les détails du contact dans les TextViews
        TextView fullNameTextView = findViewById(R.id.text_view_full_name);
        fullNameTextView.setText(selectedContact.getFullName());

        TextView professionTextView = findViewById(R.id.text_view_profession);
        professionTextView.setText(selectedContact.getProfession());

        TextView phoneTextView = findViewById(R.id.text_view_phone);
        phoneTextView.setText(selectedContact.getPhone());
    }

    // Méthode pour gérer le clic sur le bouton "Update"
    public void onUpdateClicked(View view) {
        // Rediriger vers l'activité UpdateContactActivity en passant les données du contact
        Intent intent = new Intent(this, UpdateContactActivity.class);
        intent.putExtra("selected_contact", selectedContact);
        startActivity(intent);
    }

    // Méthode pour gérer le clic sur le bouton "Delete"
    public void onDeleteClicked(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Requête pour trouver le contact par fullName
            Query query = db.collection("user")
                    .document(user.getEmail())
                    .collection("contacts")
                    .whereEqualTo("fullName", selectedContact.getFullName());

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // Parcourir les résultats et supprimer le document
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Afficher un message de succès
                                    Toast.makeText(ContactsDetailsActivity.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Rediriger vers l'activité précédente ou fermer cette activité
                                    finish(); // Fermer cette activité
                                })
                                .addOnFailureListener(e -> {
                                    // Afficher un message d'erreur
                                    Toast.makeText(ContactsDetailsActivity.this, "Failed to delete contact: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(ContactsDetailsActivity.this, "Contact not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(ContactsDetailsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
