package com.example.ticketsource.listeners;

import java.util.ArrayList;

import com.example.ticketsource.models.Bilhete;

public interface BilheteListener {
	void onRefreshListaBilhetes(ArrayList<Bilhete> listaBilhetes);

	void onCancelarBilhete();

	void onCheckinBilhete();
}
