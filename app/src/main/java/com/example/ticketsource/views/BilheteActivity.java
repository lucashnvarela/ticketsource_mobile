package com.example.ticketsource.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import com.example.ticketsource.R;
import com.example.ticketsource.listeners.BilheteListener;
import com.example.ticketsource.listeners.UserListener;
import com.example.ticketsource.models.Bilhete;
import com.example.ticketsource.models.SingletonTicketsource;

public class BilheteActivity extends AppCompatActivity implements UserListener, BilheteListener {

	public static final String ID = "ID";
	private Bilhete bilhete;

	private TextView tvUid, tvLugar, tvDisponivel, tvStatus;
	private String token;
	private Button btnCancelar, btnCheckin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bilhete);

		int id = getIntent().getIntExtra(ID, -1);
		bilhete = SingletonTicketsource.getInstance(getApplicationContext()).getBilhete(id);

		tvUid = findViewById(R.id.tvUid);
		tvLugar = findViewById(R.id.tvLugar);
		tvDisponivel = findViewById(R.id.tvDisponivel);
		tvStatus = findViewById(R.id.tvStatus);
		btnCancelar = findViewById(R.id.btnCancelar);
		btnCheckin = findViewById(R.id.btnCheckin);

		SharedPreferences sharedPrefUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
		token = sharedPrefUser.getString(MenuMainActivity.TOKEN, null);

		SingletonTicketsource.getInstance(getApplicationContext()).setUserListener(this);
		SingletonTicketsource.getInstance(getApplicationContext()).setBilheteListener(this);

		SingletonTicketsource.getInstance(getApplicationContext()).checkRoleAPI(getApplicationContext(), token);

		btnCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
				String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
				SingletonTicketsource.getInstance(getApplicationContext()).cancelarBilheteAPI(getApplicationContext(), bilhete, token);
			}
		});

		btnCheckin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
				String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
				SingletonTicketsource.getInstance(getApplicationContext()).checkinBilheteAPI(getApplicationContext(), bilhete);
			}
		});

		if (bilhete != null) {
			Toolbar toolbar = findViewById(R.id.myToolBar);
			setSupportActionBar(toolbar);
			setTitle("Bilhete " + bilhete.getNumeroLugar() + bilhete.getId());
			loadBilhete();
		}
	}

	private void loadBilhete() {
		tvUid.setText(bilhete.getUid());
		tvLugar.setText(bilhete.getNumeroLugar());
		tvDisponivel.setText(bilhete.getDisponivel());
		tvStatus.setText(bilhete.getStatus());
	}

	@Override
	public void onUserSignup(String response) {

	}

	@Override
	public void onValidateLogin(String token, String username) {

	}

	@Override
	public void onCheckRole(Boolean isCliente) {
		if (isCliente) {
			btnCancelar.setVisibility(View.VISIBLE);
			btnCheckin.setVisibility(View.GONE);
		} else {
			btnCancelar.setVisibility(View.GONE);
			btnCheckin.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onErroLogin() {

	}

	@Override
	public void onRefreshListaBilhetes(ArrayList<Bilhete> listaBilhetes) {

	}

	@Override
	public void onCancelarBilhete() {
		Toast.makeText(getApplicationContext(), "Bilhete cancelado com sucesso", Toast.LENGTH_SHORT).show();
		btnCancelar.setVisibility(View.GONE);
		tvDisponivel.setText(bilhete.getDisponivel());
	}

	@Override
	public void onCheckinBilhete() {
		Toast.makeText(getApplicationContext(), "Check-in com sucesso", Toast.LENGTH_SHORT).show();
		btnCheckin.setVisibility(View.GONE);
		tvStatus.setText(bilhete.getStatus());
	}
}