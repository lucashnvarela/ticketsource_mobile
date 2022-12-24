package com.example.ticketsource.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import com.example.ticketsource.R;
import com.example.ticketsource.models.Bilhete;

public class ListaBilheteAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Bilhete> bilhetes;

	public ListaBilheteAdapter(Context context, ArrayList<Bilhete> bilhetes) {
		this.context = context;
		this.bilhetes = bilhetes;
	}

	@Override
	public int getCount() {
		return bilhetes.size();
	}

	@Override
	public Object getItem(int position) {
		return bilhetes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return bilhetes.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	/*
		if (inflater == null)
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.item_lista_bilhete, null);

		ViewHolderLista viewHolderLista = (ViewHolderLista) convertView.getTag();
		if (viewHolderLista == null) {
			viewHolderLista = new ViewHolderLista(convertView);
			convertView.setTag(viewHolderLista);
		}
		viewHolderLista.update(bilhetes.get(position));

		*/
		return convertView;
	}

	private class ViewHolderLista {
		/*
		private TextView tvUid, tvNumeroLugar, tvStatus;

		public ViewHolderLista(View view) {
			tvTitulo = view.findViewById(R.id.tvUid);
			tvNumeroLugar = view.findViewById(R.id.tvNumeroLugar);
			tvStatus = view.findViewById(R.id.tvStatus);
		}

		public void update(Bilhete bilhete) {
			tvUid.setText(bilhete.getUid());
			tvNumeroLugar.setText(bilhete.getNumeroLugar());
			tvStatus.setText(bilhete.getStatus());
		}
		*/
	}
}
