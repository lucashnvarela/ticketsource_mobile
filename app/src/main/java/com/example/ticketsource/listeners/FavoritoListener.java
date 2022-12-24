package com.example.ticketsource.listeners;

import java.util.ArrayList;

import com.example.ticketsource.models.Evento;

public interface FavoritoListener {

	void onAddFavorito();

	void onDeleteFavorito();

	void onCheckFavorito(Boolean isFavorito);

	void onRefreshListaFavoritos(ArrayList<Evento> eventos);
}
