package com.example.trabalhocalculo3;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView txtDensityLabel;
    private TextView txtResults;
    private double density = 1.0;
    private double volume = 0.0;
    private double mass = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnImport = findViewById(R.id.btnImport);
        SeekBar seekDensity = findViewById(R.id.seekDensity);
        txtDensityLabel = findViewById(R.id.txtDensityLabel);
        txtResults = findViewById(R.id.txtResults);

        btnImport.setOnClickListener(v -> {
            // abrir SAF (Storage Access Framework) para escolher .stl
            // parsear malha e calcular volume via decomposição em tetraedros
            volume = 1.0; 
            updateResults();
        });

        seekDensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                density = progress / 100.0;
                txtDensityLabel.setText("Densidade (kg/m³): " + density);
                updateResults();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateResults() {
        mass = volume * density;
        txtResults.setText(
            "Volume: " + volume + " m³\n" +
            "Massa: " + mass + " kg\n" +
            "Centro de massa: --"
        );
    }
}