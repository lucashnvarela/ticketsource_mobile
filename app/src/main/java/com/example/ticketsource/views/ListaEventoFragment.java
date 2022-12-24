package com.example.ticketsource.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import com.example.ticketsource.R;
import com.example.ticketsource.adapters.ListaEventoAdapter;
import com.example.ticketsource.listeners.EventoListener;
import com.example.ticketsource.models.Evento;
import com.example.ticketsource.models.SingletonTicketsource;

public class ListaEventoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, EventoListener {
	private ListView lvListaEventos;
	private ArrayList<Evento> listaEventos;
	private SwipeRefreshLayout swipeRefreshLayout;

	public ListaEventoFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.fragment_lista_evento, container, false);

		lvListaEventos = view.findViewById(R.id.lvListaEventos);

		SingletonTicketsource.getInstance(getContext()).setEventoListener(this);

		lvListaEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getContext(), EventoActivity.class);
				intent.putExtra("ID", (int) id);
				startActivity(intent);
			}
		});

		SingletonTicketsource.getInstance(getContext()).getAllEventosAPI(getContext());

		swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		SingletonTicketsource.getInstance(getContext()).setEventoListener(this);
	}

	@Override
	public void onRefresh() {
		SingletonTicketsource.getInstance(getContext()).getAllEventosAPI(getContext());
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onRefreshListaEventos(ArrayList<Evento> eventos) {
		if (eventos != null)
			lvListaEventos.setAdapter(new ListaEventoAdapter(getContext(), eventos));
	}
}