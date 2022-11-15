package com.example.ticketsource.listeners;

import java.util.ArrayList;

import com.example.ticketsource.models.Evento;

public interface EventoListener {

        void onRefreshListaEventos (ArrayList<Evento> listaEventos);

        void onRefreshInfo();

        void onLoadInfo(Evento evento);

}
