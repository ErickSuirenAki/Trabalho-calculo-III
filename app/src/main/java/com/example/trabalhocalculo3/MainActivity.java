package com.example.trabalhocalculo3;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;

import com.example.trabalhocalculo3.mesh.Mesh3D;
import com.example.trabalhocalculo3.mesh.MeshImporter;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private TextView txtDensityLabel;
    private TextView txtResults;
    private double density = 1.0;
    private double volume = 1.0;
    private double mass = 0.0;
    private Button btnImportStl;
    private TextView txtImportStatus;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Mesh3D currentMesh;
    private final ActivityResultLauncher<String[]>
            openStlLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    this::onStlSelected
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnImportStl = findViewById(R.id.btnImport);
        txtImportStatus = findViewById(R.id.textImportStatus);
        SeekBar seekDensity = findViewById(R.id.seekDensity);
        txtDensityLabel = findViewById(R.id.txtDensityLabel);
        txtResults = findViewById(R.id.txtResults);

        btnImportStl.setOnClickListener(v -> openStlLauncher.launch(new String[]{"*/*"}));

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    private void onStlSelected(Uri uri) {
        if (uri == null) {
            txtImportStatus.setText("Seleção cancelada.");
            return;
        }

        txtImportStatus.setText("Importando STL...");
        btnImportStl.setEnabled(false);

        //Parsing fora da main thread.
        executor.execute(() -> {
            try (
                    InputStream inputStream = getContentResolver().openInputStream(uri)
            ) {
                if (inputStream == null) {
                    throw new Exception(
                            "Não foi possível abrir o arquivo."
                    );
                }

                Mesh3D mesh = MeshImporter.importStl(inputStream);
                String debug = "";
                if(mesh.getPositions() == null || mesh.getNormals() == null){
                    debug = "falha";
                    throw new Exception("erro na importação.");
                }
                currentMesh = mesh;
                debug = "sucesso";

                String finalDebug = debug;
                runOnUiThread(() -> {
                    txtImportStatus.setText(finalDebug);
                    btnImportStl.setEnabled(true);

                    /*
                     * SendMeshToRenderer(currentMesh);
                     */
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    txtImportStatus.setText("Falha ao importar STL:\n"
                            + e.getMessage()
                    );
                    btnImportStl.setEnabled(true);
                });
            }
        });
    }
}