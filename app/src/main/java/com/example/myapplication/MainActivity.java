package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.conexao.AppDatabase;
import com.example.myapplication.model.conexao.DatabaseProvider;
import com.example.myapplication.model.entity.Foto;
import com.example.myapplication.model.entity.Trabalho;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.views.overlay.MapEventsOverlay;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LifecycleCameraController cameraController;
    private static final int PERMISSION_CODE = 1;
    private MapView map;
    private Trabalho trabalhoAtual;
    private List<String> fotosTrabalhoAtual = new ArrayList<>();
    private GeoPoint pontoSelecionado;
    private Marker marcadorSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuração OSMdroid recomendada
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(getPackageName());
        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));

        setContentView(R.layout.activity_main);

        if (!temTodasPermissoes()) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, PERMISSION_CODE);
        } else {
            iniciarComponentes();
        }
        Button btnTirarFoto = findViewById(R.id.btnTirarFoto);
        Button btnSalvarTrabalho = findViewById(R.id.btnSalvarTrabalho);
        Button btnVerLocalizacoes = findViewById(R.id.btnVerLocalizacoes);
        Button btnMarcarLocalizacao = findViewById(R.id.btnMarcarLocalizacao);

        btnVerLocalizacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocalizacaoActivity.class);
                startActivity(intent);
            }
        });

        btnMarcarLocalizacao.setOnClickListener(v -> marcarLocalizacaoEscolhida());
        btnTirarFoto.setOnClickListener(v -> tirarFoto());
        btnSalvarTrabalho.setOnClickListener(v -> salvarTrabalho());
    }

    private boolean temTodasPermissoes() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void iniciarComponentes() {
        abrirCamera();
        mostrarMapa();
    }

    private void abrirCamera() {
        PreviewView previewView = findViewById(R.id.previewView);
        cameraController = new LifecycleCameraController(getBaseContext());
        try {
            cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
            cameraController.bindToLifecycle(this);
            previewView.setController(cameraController);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao inicializar a câmera", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean situacaoDaRede() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void mostrarMapa() {
        if (!situacaoDaRede()) {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
            return;
        }
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);
        map.setBuiltInZoomControls(true);
        map.setMinZoomLevel(2.0);
        map.getController().setZoom(3.0);
        map.getController().setCenter(new GeoPoint(0, 0));

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                pontoSelecionado = p;
                if (marcadorSelecionado != null) {
                    map.getOverlays().remove(marcadorSelecionado);
                }
                marcadorSelecionado = new Marker(map);
                marcadorSelecionado.setPosition(p);
                marcadorSelecionado.setTitle("Local Selecionado");
                map.getOverlays().add(marcadorSelecionado);
                map.invalidate();
                Toast.makeText(MainActivity.this, "Localização selecionada!", Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) { return false; }
        };
        MapEventsOverlay overlayEventos = new MapEventsOverlay(mReceive);
        map.getOverlays().add(overlayEventos);
        map.invalidate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE && temTodasPermissoes()) {
            iniciarComponentes();
        } else {
            Toast.makeText(this, "Permissões necessárias negadas", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    private void marcarLocalizacaoEscolhida() {
        if (pontoSelecionado == null) {
            Toast.makeText(this, "Clique no mapa para escolher a localização!", Toast.LENGTH_SHORT).show();
            return;
        }
        trabalhoAtual = new Trabalho();
        trabalhoAtual.latitude = pontoSelecionado.getLatitude();
        trabalhoAtual.longitude = pontoSelecionado.getLongitude();
        trabalhoAtual.dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        fotosTrabalhoAtual.clear();
        Toast.makeText(this, "Localização marcada! Agora você pode tirar fotos.", Toast.LENGTH_SHORT).show();
    }

    private void tirarFoto() {
        if (trabalhoAtual == null) {
            Toast.makeText(this, "Marque a localização antes de tirar fotos!", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getExternalFilesDir(null), "foto_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        cameraController.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        fotosTrabalhoAtual.add(file.getAbsolutePath());
                        Toast.makeText(MainActivity.this, "Foto tirada!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(MainActivity.this, "Erro ao salvar foto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void salvarTrabalho() {
        if (trabalhoAtual == null) {
            Toast.makeText(this, "Marque a localização primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }
        AppDatabase db = DatabaseProvider.getDatabase(this);
        long idTrabalho = db.trabalhoDao().inserir(trabalhoAtual);
        for (String caminho : fotosTrabalhoAtual) {
            Foto foto = new Foto();
            foto.idTrabalho = idTrabalho;
            foto.caminhoArquivo = caminho;
            foto.dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            db.fotoDao().inserir(foto);
        }
        trabalhoAtual = null;
        fotosTrabalhoAtual.clear();
        Toast.makeText(this, "Trabalho salvo com sucesso!", Toast.LENGTH_SHORT).show();
    }
}