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
import com.example.ticketsource.adapters.ListaBilheteAdapter;
import com.example.ticketsource.listeners.BilheteListener;
import com.example.ticketsource.models.Bilhete;
import com.example.ticketsource.models.SingletonTicketsource;

public class ListaBilheteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BilheteListener {
	private ListView lvListaBilhetes;
	private ArrayList<Bilhete> listaBilhetes;
	private SwipeRefreshLayout swipeRefreshLayout;

	public ListaBilheteFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		View view = inflater.inflate(R.layout.fragment_lista_bilhete, container, false);

		lvListaBilhetes = view.findViewById(R.id.lvListaBilhetes);

		SharedPreferences sharedPrefInfoUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
		String token = sharedPrefInfoUser.getString(MenuMainActivity.TOKEN, null);

		SingletonTicketsource.getInstance(getContext()).setBilheteListener(this);

		/*
		lvListaBilhetes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getContext(), BilheteActivity.class);
				intent.putExtra(BilheteActivity.ID, (int) id);
				startActivity(intent);
			}
		});
		 */

		SingletonTicketsource.getInstance(getContext()).getAllBilhetesAPI(getContext(), token);

		swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		SingletonTicketsource.getInstance(getContext()).setBilheteListener(this);
	}

	@Override
	public void onRefresh() {
		SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
		String token = sharedPreferencesUser.getString(MenuMainActivity.TOKEN, null);
		SingletonTicketsource.getInstance(getContext()).getAllBilhetesAPI(getContext(), token);
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onRefreshListaBilhetes(ArrayList<Bilhete> bilhetes) {
		if (bilhetes != null)
			lvListaBilhetes.setAdapter(new ListaBilheteAdapter(getContext(), bilhetes));
	}

	@Override
	public void onCancelarBilhete() {

	}

	@Override
	public void onCheckinBilhete() {

	}
}
