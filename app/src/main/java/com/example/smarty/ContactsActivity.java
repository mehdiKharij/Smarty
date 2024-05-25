package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

import model.Contact;

public class ContactsActivity extends AppCompatActivity implements MyAdapterContacts.OnItemClickListener {

    FirebaseFirestore db;
    LinkedList<Contact> contacts;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private ImageView menuImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        contacts = new LinkedList<>();

        recyclerView = findViewById(R.id.recycler_view_contacts);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_contact);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContacts();
    }

    void getContacts(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference docRef = db.collection("user").document(user.getEmail());
            docRef.collection("contacts").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                contacts.clear(); // Clear existing contacts
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Contact contact = document.toObject(Contact.class);
                                    contacts.add(contact);
                                }
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ContactsActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                MyAdapterContacts adapter = new MyAdapterContacts(contacts, ContactsActivity.this, ContactsActivity.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Log.d("ContactsActivity", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public void onItemClick(Contact contact) {
        // Ajoutez ici le code pour gérer le clic sur un contact
    }
}
