package com.example.ticketsource;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvUsername;
    private String getUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tvUsername = findViewById(R.id.tvUsername);

        Intent intent = getIntent();
        
		getUsername = intent.getStringExtra("USERNAME");
        if (getUsername != null) tvUsername.setText(getUsername);
    }


}