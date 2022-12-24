package com.example.ticketsource.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import com.example.ticketsource.R;
import com.example.ticketsource.adapters.ListaFavoritoAdapter;
import com.example.ticketsource.listeners.FavoritoListener;
import com.example.ticketsource.models.Evento;
import com.example.ticketsource.models.SingletonTicketsource;

public class ListaFavoritoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FavoritoListener {
	private ListView lvListaFavoritos;
	private ArrayList<Evento> listaEventos;
	private SwipeRefreshLayout swipeRefreshLayout;

	public ListaFavoritoFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.fragment_lista_favorito, container, false);

		lvListaFavoritos = view.findViewById(R.id.lvListaFavoritos);

		SharedPreferences sharedPrefInfoUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
		String token = sharedPrefInfoUser.getString(MenuMainActivity.TOKEN, null);

		SingletonTicketsource.getInstance(getContext()).setFavoritoListener(this);

		lvListaFavoritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getContext(), EventoActivity.class);
				intent.putExtra(EventoActivity.ID, (int) id);
				startActivity(intent);
			}
		});

		SingletonTicketsource.getInstance(getContext()).getAllFavoritosAPI(getContext(), token);

		swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		SingletonTicketsource.getInstance(getContext()).setFavoritoListener(this);
	}

	@Override
	public void onRefresh() {
		SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
		String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
		SingletonTicketsource.getInstance(getContext()).getAllFavoritosAPI(getContext(), token);
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onRefreshListaFavoritos(ArrayList<Evento> eventos) {
		if (eventos != null)
			lvListaFavoritos.setAdapter(new ListaFavoritoAdapter(getContext(), eventos));
	}

	@Override
	public void onAddFavorito() {

	}

	@Override
	public void onDeleteFavorito() {

	}

	@Override
	public void onCheckFavorito(Boolean isFavorito) {

	}
}
