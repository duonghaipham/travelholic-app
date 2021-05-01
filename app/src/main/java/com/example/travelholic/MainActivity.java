package com.example.travelholic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map();
//        btnLogin.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, ToursActivity.class);
//            startActivity(intent);
//        });
    }

    private void map() {
        btnLogin = findViewById(R.id.btn_login);
    }
}