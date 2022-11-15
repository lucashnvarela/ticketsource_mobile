package com.example.ticketsource.listeners;

import java.util.ArrayList;

import com.example.ticketsource.models.Evento;

public interface FavoritoListener {

    void onAddEventosFavoritos();

    void onDeleteEventosFavoritos();

    void oncheckEventoFavorito(Boolean favorito);

    void onRefreshListaFavoritosEventos(ArrayList<Evento> eventos);
}
