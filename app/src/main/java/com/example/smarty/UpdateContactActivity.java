package com.example.smarty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import model.Contact;

public class UpdateContactActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextProfession, editTextPhone;
    private Button buttonUpdate;
    private Contact selectedContact;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get selected contact data from intent
        selectedContact = (Contact) getIntent().getSerializableExtra("selected_contact");

        // Initialize Views
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextProfession = findViewById(R.id.editTextProfession);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Set current contact data to EditText fields
        editTextFullName.setText(selectedContact.getFullName());
        editTextProfession.setText(selectedContact.getProfession());
        editTextPhone.setText(selectedContact.getPhone());

        // Set onClickListener for Update button
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });
    }

    // Method to update contact details
    private void updateContact() {
        String fullName = editTextFullName.getText().toString().trim();
        String profession = editTextProfession.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        // Check if any field is empty
        if (fullName.isEmpty() || profession.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Get reference to the contact document in Firestore using full name
            db.collection("user")
                    .document(user.getEmail())
                    .collection("contacts")
                    .whereEqualTo("fullName", fullName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Update the first document that matches the full name
                            DocumentReference contactRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("profession", profession);
                            updatedData.put("phone", phone);

                            // Perform update operation
                            contactRef.update(updatedData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(UpdateContactActivity.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                                        finish(); // Close this activity after updating contact
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(UpdateContactActivity.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(UpdateContactActivity.this, "Contact not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(UpdateContactActivity.this, "Failed to update contact: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

}
