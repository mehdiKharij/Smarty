package com.example.smarty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvComfortLevel;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private DatabaseReference databaseReference;
    private CombinedChart combinedChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvComfortLevel = view.findViewById(R.id.tvComfortLevel);
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        combinedChart = view.findViewById(R.id.combinedChart);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("smart-Home").child("zoneComfort");

        // Add ValueEventListener to retrieve data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the value representing the comfort level
                String comfortLevel = dataSnapshot.child("zone de comfort").getValue(String.class);

                // Display the comfort level
                tvComfortLevel.setText( comfortLevel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // Initialize and set up the chart
        setupChart();

        // Retrieve temperature and humidity data
        retrieveTemperatureAndHumidity();

        return view;
    }

    private void setupChart() {
        List<Entry> temperatureEntries = new ArrayList<>();
        temperatureEntries.add(new Entry(0, 25.6f));
        temperatureEntries.add(new Entry(1, 24.5f));
        temperatureEntries.add(new Entry(2, 23.5f));
        temperatureEntries.add(new Entry(3, 26.3f));
        temperatureEntries.add(new Entry(4, 25.7f));

        LineDataSet temperatureDataSet = new LineDataSet(temperatureEntries, "Temperature (°C)");
        int colorPrimary = ContextCompat.getColor(requireContext(), R.color.blue);
        temperatureDataSet.setColor(colorPrimary);

        temperatureDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<BarEntry> humidityEntries = new ArrayList<>();
        humidityEntries.add(new BarEntry(0, 60));
        humidityEntries.add(new BarEntry(1, 63));
        humidityEntries.add(new BarEntry(2, 67));
        humidityEntries.add(new BarEntry(3, 60));
        humidityEntries.add(new BarEntry(4, 57));

        BarDataSet humidityDataSet = new BarDataSet(humidityEntries, "Humidity (%)");
        int colorPrimaryDark = ContextCompat.getColor(requireContext(), R.color.colorPrimary);
        humidityDataSet.setColor(colorPrimaryDark);

        humidityDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        LineData lineData = new LineData(temperatureDataSet);
        BarData barData = new BarData(humidityDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(barData);

        combinedChart.setData(combinedData);
        combinedChart.invalidate(); // refresh
    }

    private void retrieveTemperatureAndHumidity() {
        DatabaseReference roomReference = FirebaseDatabase.getInstance().getReference().child("smart-Home").child("chambre");

        roomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int humidity = dataSnapshot.child("humidity").getValue(Integer.class);
                double temperature = dataSnapshot.child("temperature").getValue(Double.class);

                tvHumidity.setText(humidity + "%");
                tvTemperature.setText(temperature + "°C");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
