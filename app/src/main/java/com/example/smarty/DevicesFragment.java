package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DevicesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_devices, container, false);

        // Initialize views
        ImageView clothingImage = rootView.findViewById(R.id.clothingImage);
        ImageView elecImage = rootView.findViewById(R.id.elecImage);
        ImageView homeImage = rootView.findViewById(R.id.homeImage);
        ImageView beautyImage = rootView.findViewById(R.id.beautyImage);

        // Set click listeners for each ImageView
        clothingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RoomDetailsActivity.class));
            }
        });

        elecImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KitchenDetailsActivity.class));
            }
        });

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GarageDetailsActivity.class));
            }
        });

        beautyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BathroomDetailsActivity.class));
            }
        });

        return rootView;
    }
}
