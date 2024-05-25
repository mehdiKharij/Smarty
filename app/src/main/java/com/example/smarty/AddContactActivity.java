package com.example.smarty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {

    private EditText fullNameEditText, professionEditText, phoneEditText;
    private Button saveButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        db = FirebaseFirestore.getInstance();

        fullNameEditText = findViewById(R.id.full_name_edit_text);
        professionEditText = findViewById(R.id.profession_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        saveButton = findViewById(R.id.save_contact_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
    }

    private void saveContact() {
        String fullName = fullNameEditText.getText().toString().trim();
        String profession = professionEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (fullName.isEmpty() || profession.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference contactRef = db.collection("user")
                    .document(user.getEmail())
                    .collection("contacts")
                    .document(); // Note: No need to specify document ID as it will be auto-generated
            Map<String, Object> contactData = new HashMap<>();
            contactData.put("fullName", fullName);
            contactData.put("profession", profession);
            contactData.put("phone", phone);
            contactRef.set(contactData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddContactActivity.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Return to previous activity after adding contact
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddContactActivity.this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
