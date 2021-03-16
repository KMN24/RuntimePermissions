package com.kmn.runtimepermissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int WRITE_PERMISSION_RC = 123;
    /**
     *  Проверка состояния разрешения ---- done
     *  запрос разрешения, если его нет ---- done
     *  показ объяснения, если ого нужно ---- done
     *  обработка ответа на запрос разрешения --- done
     **/

    private EditText mInput;
    private Button mWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInput = findViewById(R.id.input);
        mWrite = findViewById(R.id.btn_write);

        mWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToWrite = mInput.getText().toString();
                writeTOFileIfNotEmpty(textToWrite);
            }
        });

    }

    private void writeTOFileIfNotEmpty(String textToWrite) {
        if(TextUtils.isEmpty(textToWrite)){
            Toast.makeText(this, "text is empty", Toast.LENGTH_SHORT).show();
        }else {
            writeToFileWithPermissionRequestIfNeeded(textToWrite);
        }
    }

    private void writeToFileWithPermissionRequestIfNeeded(String textToWrite) {
        if (isWritePermissionGranted() ){
            writeToFile(textToWrite);
        }else {
            requestWritePermission();
        }
    }

    private void requestWritePermission() {

        if( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ){
            // show rationale

            new AlertDialog.Builder(this)
                    .setMessage("Без разрешения невозможно записать текст в файл")
                    .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_RC);

                        }
                    })
                    .show();

        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_RC);
        }

    }

    private void writeToFile(String textToWrite) {
        Toast.makeText(this, textToWrite + " is written to file", Toast.LENGTH_SHORT).show();
    }

    private boolean isWritePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode != WRITE_PERMISSION_RC ) return;
        if(grantResults.length != 1) return;

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            String textToWrite = mInput.getText().toString();
            writeToFile(textToWrite);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Вы можете дать разрешения в настройках устройства.")
                    .setPositiveButton("Понятно", null)
                    .show();
        }

    }
}