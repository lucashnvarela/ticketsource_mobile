package com.example.ticketsource;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
	private static final int PASSWORD_MIN = 4;
    private EditText etUsername ,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
    }

	public void login(View view) {
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();

		if (username.isEmpty()) {
			etUsername.setError("Necessário preencher o nome de utilizador");
			etUsername.requestFocus();
			return;
		}

		if (password.isEmpty()) {
			etPassword.setError("Necessário preencher a palavra-passe");
			etPassword.requestFocus();
			return;
		}
		else if (isPasswordValid(password)) {
			etPassword.setError("A palavra-passe deve conter pelo menos 4 caracteres");
			etPassword.requestFocus();
			return;
		}

		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		intent.putExtra("USERNAME", username);
		startActivity(intent);
	}

	/*
    public static boolean isUsernameValid(String username){
		return username != null ? Patterns.EMAIL_ADDRESS.matcher(username).matches() : false;
    }
	*/

    public static boolean isPasswordValid(String password){
		return password != null ? password.length() >= PASSWORD_MIN : false;
    }

}
