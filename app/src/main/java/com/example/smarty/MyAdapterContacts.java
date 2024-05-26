package com.example.smarty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.LinkedList;
import model.Contact;

public class MyAdapterContacts extends RecyclerView.Adapter<MyAdapterContacts.MyViewHolder> {

    private LinkedList<Contact> contacts;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
        void onCallClick(Contact contact);
    }

    public MyAdapterContacts(LinkedList<Contact> contacts, Context context, OnItemClickListener listener) {
        this.contacts = contacts;
        this.context = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact currentContact = contacts.get(position);
        holder.name.setText(currentContact.getFullName());
        holder.phone.setText(currentContact.getPhone()); // Ajoutez cette ligne pour afficher le numéro de téléphone

        // Gestion du clic sur le bouton d'appel
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallClick(currentContact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView phone; // Ajoutez cette ligne pour le champ téléphone
        public ImageView callButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            phone = itemView.findViewById(R.id.contact_phone); // Ajoutez cette ligne pour le champ téléphone
            callButton = itemView.findViewById(R.id.contact_call_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mListener.onItemClick(contacts.get(position));
            }
        }
    }
}
