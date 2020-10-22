package com.jatin.recordaudioapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.Permission;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay, btnRecord, btnStop;
    private MediaRecorder recorder;
    private String path = "";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==111){
            if(grantResults.length>0){
                if(grantResults[0] == PERMISSION_GRANTED)
                    Toast.makeText(this, "Recod Audio Permission is Granted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "Sorry! Recod Audio Permission is DENIED again", Toast.LENGTH_LONG).show();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord = findViewById(R.id.btnRecord);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        btnRecord.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "my_recording.3gp";


            if(Build.VERSION.SDK_INT >16 &&
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED )
            {
                Toast.makeText(this, "Permission is Granted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Permission is DENIED", Toast.LENGTH_LONG).show();
                // code to request permission at runtime
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},111);
            }
        }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnRecord: {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(path);
                try {
                    recorder.prepare(); //this can throw IOException
                    recorder.start();
                }
                catch (IOException ioe) {
                    Log.e("jv", "prepare() failed in recording");
                }
                break;
            }
                case R.id.btnStop: {
                    recorder.stop();
                    Toast.makeText(this, "Recording Saved at" + path, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.btnPlay: {
                    MediaPlayer player = new MediaPlayer();
                    try {
                        player.setDataSource(path);
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    }