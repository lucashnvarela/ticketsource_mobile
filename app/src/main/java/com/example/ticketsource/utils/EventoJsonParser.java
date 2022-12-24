package com.example.ticketsource.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.ticketsource.models.Evento;

public class EventoJsonParser {

	public static ArrayList<Evento> parserJsonEvento(JSONArray response) {
		ArrayList<Evento> eventos = new ArrayList<>();

		try {
			for (int i = 0; i < response.length(); i++) {
				JSONObject evento = (JSONObject) response.get(i);

				int Id = evento.getInt("id");
				String Titulo = evento.getString("titulo");
				String Descricao = evento.getString("descricao");
				String Categoria = evento.getString("categoria");
				String Nomepic = evento.getString("nome_pic");

				Evento auxEvento = new Evento(Id, Titulo, Descricao, Categoria, Nomepic);
				eventos.add(auxEvento);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return eventos;
	}

	public static Evento parserJsonEvento(String response) {

		Evento auxEvento = null;
		try {
			JSONObject evento = new JSONObject(response);

			int Id = evento.getInt("id");
			String Titulo = evento.getString("titulo");
			String Descricao = evento.getString("descricao");
			String Categoria = evento.getString("categoria");
			String Nomepic = evento.getString("nome_pic");

			auxEvento = new Evento(Id, Titulo, Descricao, Categoria, Nomepic);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return auxEvento;
	}
}
