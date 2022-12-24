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
import com.example.ticketsource.models.Evento;

public class ListaEventoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Evento> eventos;

	public ListaEventoAdapter(Context context, ArrayList<Evento> eventos) {
		this.context = context;
		this.eventos = eventos;
	}

	@Override
	public int getCount() {
		return eventos.size();
	}

	@Override
	public Object getItem(int position) {
		return eventos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return eventos.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null)
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.item_lista_evento, null);

		ViewHolderLista viewHolderLista = (ViewHolderLista) convertView.getTag();
		if (viewHolderLista == null) {
			viewHolderLista = new ViewHolderLista(convertView);
			convertView.setTag(viewHolderLista);
		}
		viewHolderLista.update(eventos.get(position));

		return convertView;
	}

	private class ViewHolderLista {
		private TextView tvTitulo, tvCategoria;
		private ImageView ivEvento;

		public ViewHolderLista(View view) {
			tvTitulo = view.findViewById(R.id.tvTitulo);
			tvCategoria = view.findViewById(R.id.tvCategoria);
			ivEvento = view.findViewById(R.id.ivEvento);
		}

		public void update(Evento evento) {
			tvTitulo.setText(evento.getTitulo());
			tvCategoria.setText(evento.getCategoria());
			Glide.with(context)
					.load(evento.getNomepic())
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(ivEvento);
		}
	}
}
