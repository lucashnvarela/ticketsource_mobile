package com.example.ticketsource.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import com.example.ticketsource.R;
import com.example.ticketsource.listeners.FavoritoListener;
import com.example.ticketsource.models.Evento;
import com.example.ticketsource.models.SingletonTicketsource;

public class EventoActivity extends AppCompatActivity implements FavoritoListener {

	public static final String ID = "ID";
	private Evento evento;

	private TextView tvTitulo, tvDescricao, tvCategoria;
	private ImageView ivEvento;
	private String token;
	private Button btnAdicionarFavorito, btnRemoverFavorito;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento);

		int id = getIntent().getIntExtra(ID, -1);
		evento = SingletonTicketsource.getInstance(getApplicationContext()).getEvento(id);

		tvTitulo = findViewById(R.id.tvTitulo);
		tvCategoria = findViewById(R.id.tvCategoria);
		tvDescricao = findViewById(R.id.tvDescricao);
		ivEvento = findViewById(R.id.ivEvento);
		btnAdicionarFavorito = findViewById(R.id.btnAddFav);
		btnRemoverFavorito = findViewById(R.id.btnRemoveFav);

		SharedPreferences sharedPrefUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
		token = sharedPrefUser.getString(MenuMainActivity.TOKEN, null);

		SingletonTicketsource.getInstance(getApplicationContext()).setFavoritoListener(this);

		if (token != null) {
			SingletonTicketsource.getInstance(getApplicationContext()).checkFavoritoAPI(getApplicationContext(), evento, token);

			btnAdicionarFavorito.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
					String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
					SingletonTicketsource.getInstance(getApplicationContext()).addFavoritoAPI(getApplicationContext(), evento, token);
				}
			});

			btnRemoverFavorito.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences sharedPreferencesUser = getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
					String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
					SingletonTicketsource.getInstance(getApplicationContext()).deleteFavoritoAPI(getApplicationContext(), evento, token);
				}
			});
		} else {
			btnAdicionarFavorito.setVisibility(View.INVISIBLE);
			btnRemoverFavorito.setVisibility(View.GONE);
		}

		if (evento != null) {
			Toolbar toolbar = findViewById(R.id.myToolBar);
			setSupportActionBar(toolbar);
			setTitle("Evento : " + evento.getTitulo());
			loadEvento();
		}
	}

	private void loadEvento() {
		tvTitulo.setText(evento.getTitulo());
		tvCategoria.setText(evento.getCategoria());
		tvDescricao.setText(evento.getDescricao());
		Glide.with(this)
				.load(evento.getNomepic())
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(ivEvento);
	}


	@Override
	public void onAddFavorito() {
		Toast.makeText(getApplicationContext(), "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
		btnAdicionarFavorito.setVisibility(View.GONE);
		btnRemoverFavorito.setVisibility(View.VISIBLE);
	}

	@Override
	public void onDeleteFavorito() {
		Toast.makeText(getApplicationContext(), "Removido dos favoritos", Toast.LENGTH_SHORT).show();
		btnAdicionarFavorito.setVisibility(View.VISIBLE);
		btnRemoverFavorito.setVisibility(View.GONE);
	}

	@Override
	public void onCheckFavorito(Boolean isFavorito) {
		if (isFavorito) {
			btnAdicionarFavorito.setVisibility(View.GONE);
			btnRemoverFavorito.setVisibility(View.VISIBLE);
		} else {
			btnAdicionarFavorito.setVisibility(View.VISIBLE);
			btnRemoverFavorito.setVisibility(View.GONE);
		}
	}

	@Override
	public void onRefreshListaFavoritos(ArrayList<Evento> eventos) {

	}
}