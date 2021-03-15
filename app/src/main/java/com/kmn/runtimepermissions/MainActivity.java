package com.kmn.runtimepermissions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /**
     *  Проверка состояния разрешения
     *  запрос разрешения, если его нет
     *  показ объяснения, если ого нужно
     *  обработка ответа на запрос разрешения
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}