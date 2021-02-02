package com.example.grabadoraexterna;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothHeadset;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.AudioRecordingConfiguration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder grabacion;
    private String archivoSalida =null;
    private Button btn_recorder;
    private TextView bit;
    private boolean pausa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_recorder =(Button)findViewById(R.id.btn_rec);
        bit = (TextView)findViewById(R.id.textView2);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void Recorder(View view){
        if (grabacion==null){
            archivoSalida= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Grabacion.mp3";
            grabacion= new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC );
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4 );
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4);
            grabacion.setAudioEncodingBitRate(256);
            grabacion.setAudioSamplingRate(48);
            grabacion.setOutputFile(archivoSalida);
            pausa=true;

            try{
                grabacion.prepare();
                grabacion.start();
            }catch (IOException e){
            }
            btn_recorder.setText("Pausar");
            Toast.makeText(getApplicationContext(),"Grabando...",Toast.LENGTH_SHORT).show();
        }else if (pausa){
            grabacion.pause();
            btn_recorder.setText("Continuar");
            Toast.makeText(getApplicationContext(),"Pausado",Toast.LENGTH_SHORT).show();
            pausa=false;
        }
            else {
            grabacion.resume();
            btn_recorder.setText("Pausar");
            Toast.makeText(getApplicationContext(), "Grabando...", Toast.LENGTH_SHORT).show();
            pausa=true;
        }

    }
    public void parar(View view){
        if (grabacion!=null){
            grabacion.stop();
            grabacion.release();
            grabacion=null;
            btn_recorder.setText("grabar");
            Toast.makeText(getApplicationContext(),"grabacion finalizada",Toast.LENGTH_SHORT).show();
        }
    }
    public void reproducir(View view){
        MediaPlayer mediaPlayer=new MediaPlayer();
        try{
            mediaPlayer.setDataSource(archivoSalida);
            mediaPlayer.prepare();
        }catch (IOException e){
        }
        mediaPlayer.start();
        Toast.makeText(getApplicationContext(),"reproduciendo audio",Toast.LENGTH_SHORT).show();
    }
}