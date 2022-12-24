package com.example.ticketsource.views;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ticketsource.models.SingletonTicketsource;
import com.google.android.material.navigation.NavigationView;

import com.example.ticketsource.listeners.UserListener;

import com.example.ticketsource.R;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserListener {
	public static final String USERNAME = "USERNAME";
	public static final String TOKEN = "TOKEN";
	public static final String INFO_USER = "INFO_USER";
	private FragmentManager fragmentManager;
	private String username = "";
	private String token;
	private Toolbar toolbar;
	private NavigationView navigationView;
	private DrawerLayout drawer;

	Fragment fragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPrefInfoUser = getSharedPreferences(INFO_USER, Context.MODE_PRIVATE);
		token = sharedPrefInfoUser.getString(TOKEN, null);

		SingletonTicketsource.getInstance(getApplicationContext()).setUserListener(this);
		SingletonTicketsource.getInstance(getApplicationContext()).checkRoleAPI(getApplicationContext(), token);

		setContentView(R.layout.activity_menu_cliente);

		fragmentManager = getSupportFragmentManager();

		loadFragment();
	}

	@Override
	public void onUserSignup(String response) {

	}

	@Override
	public void onValidateLogin(String token, String username) {

	}

	@Override
	public void onCheckRole(Boolean isCliente) {
		if (isCliente)
			setContentView(R.layout.activity_menu_cliente);
		else
			setContentView(R.layout.activity_menu_gestor);

		Toolbar toolbar = findViewById(R.id.myToolBar);
		setSupportActionBar(toolbar);

		navigationView = findViewById(R.id.nav_view);

		drawer = findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose);
		toggle.syncState();
		drawer.addDrawerListener(toggle);

		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public void onErroLogin() {

	}

	private void loadFragment() {
		SharedPreferences sharedPrefInfoUser = getSharedPreferences(INFO_USER, Context.MODE_PRIVATE);
		token = sharedPrefInfoUser.getString(TOKEN, null);

		if (token != null) {
			fragment = new ListaEventoFragment();
			setTitle("ticketsource");
			if (fragment != null)
				fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
		} else {
			fragment = new LoginFragment();
			setTitle("Login");
			if (fragment != null)
				fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
		}

		/*
		SharedPreferences sharedPreferencesUser = getSharedPreferences(INFO_USER, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferencesUser.edit();
		editor.clear();
		editor.commit();
		fragment = new LoginFragment();
		fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
		setTitle("Login");
		 */
	}

	public void loadHeader() {
		SharedPreferences sharedPrefInfoUser = getSharedPreferences(INFO_USER, Context.MODE_PRIVATE);
		username = sharedPrefInfoUser.getString(USERNAME, "");
		View hView = navigationView.getHeaderView(0);
		TextView tvUsername = hView.findViewById(R.id.tvUsername);
		tvUsername.setText(username);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		loadHeader();

		SharedPreferences sharedPrefInfoUser = getSharedPreferences(INFO_USER, Context.MODE_PRIVATE);
		token = sharedPrefInfoUser.getString(TOKEN, null);

		switch (menuItem.getItemId()) {
			case R.id.nav_eventos:
				fragment = new ListaEventoFragment();
				setTitle(menuItem.getTitle());
				break;
			case R.id.nav_favoritos:
				if (token != null) {
					fragment = new ListaFavoritoFragment();
					setTitle(menuItem.getTitle());
					break;
				} else {
					Toast.makeText(getApplicationContext(), "Não tem login efectuado para aceder aos favoritos", Toast.LENGTH_LONG).show();
					fragment = new ListaEventoFragment();
					setTitle("Eventos");
					break;
				}
			case R.id.nav_bilhetes:
				if (token != null) {
					fragment = new ListaBilheteFragment();
					setTitle(menuItem.getTitle());
					break;
				} else {
					Toast.makeText(getApplicationContext(), "Não tem login efectuado para aceder aos seus bilhetes", Toast.LENGTH_LONG).show();
					fragment = new ListaEventoFragment();
					setTitle("Eventos");
					break;
				}
			case R.id.nav_gestor:
				fragment = new GestorFragment();
				setTitle(menuItem.getTitle());
				break;
			case R.id.nav_logout:
				if (token != null) {
					SharedPreferences sharedPreferencesUser = getSharedPreferences(INFO_USER, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferencesUser.edit();
					editor.clear();
					editor.commit();
					fragment = new LoginFragment();
					fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();

					Toast.makeText(getApplicationContext(), "Terminou a sessão com sucesso", Toast.LENGTH_LONG).show();
					break;
				} else {
					Toast.makeText(getApplicationContext(), "Não tem login efectuado para terminar sessão", Toast.LENGTH_LONG).show();
					fragment = new LoginFragment();
					setTitle("Login");
					break;
				}
		}

		if (fragment != null)
			fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onBackPressed() {
		loadFragment();
		return;
	}
}