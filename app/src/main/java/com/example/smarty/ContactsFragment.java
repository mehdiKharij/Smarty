package com.example.smarty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

public class ContactsFragment extends Fragment implements MyAdapterContacts.OnItemClickListener {

    FirebaseFirestore db;
    LinkedList<Contact> contacts;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private Intent callIntent; // Déclaration de la variable callIntent

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        contacts = new LinkedList<>();

        recyclerView = rootView.findViewById(R.id.recycler_view_contacts);
        FloatingActionButton fabAdd = rootView.findViewById(R.id.fab_add_contact);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
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
                                LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
                                recyclerView.setLayoutManager(layoutManager);
                                MyAdapterContacts adapter = new MyAdapterContacts(contacts, requireContext(), ContactsFragment.this);

                                recyclerView.setAdapter(adapter);
                            } else {
                                Log.d("ContactsFragment", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public void onCallClick(Contact contact) {
        String phoneNumber = contact.getPhone();

        callIntent = new Intent(Intent.ACTION_CALL); // Initialisation de callIntent
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Demander la permission si elle n'est pas déjà accordée
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Si la permission est déjà accordée, lancer l'intention d'appel
            startActivity(callIntent);
        }
    }

    @Override
    public void onItemClick(Contact contact) {
        Log.d("ContactsFragment", "Selected contact: " + contact.getFullName() + ", Phone: " + contact.getPhone());
        // Créez une intention pour démarrer ContactsDetailsActivity
        Intent intent = new Intent(requireActivity(), ContactsDetailsActivity.class);
        // Passez les données du contact sélectionné en tant qu'extra à l'intention
        intent.putExtra("selected_contact", contact);
        // Démarrez l'activité des détails du contact
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée, lancer l'intention d'appel
                startActivity(callIntent);
            } else {
                // Permission refusée, afficher un message à l'utilisateur ou gérer le refus
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
