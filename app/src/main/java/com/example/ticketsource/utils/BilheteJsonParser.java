package com.example.ticketsource.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.ticketsource.models.Bilhete;

public class BilheteJsonParser {

	public static ArrayList<Bilhete> parserJsonBilhete(JSONArray response) {
		ArrayList<Bilhete> bilhetes = new ArrayList<>();

		try {
			for (int i = 0; i < response.length(); i++) {
				JSONObject bilhete = (JSONObject) response.get(i);

				int Id = bilhete.getInt("id");
				int Id_sessao = bilhete.getInt("id_sessao");
				String Uid = bilhete.getString("uid");
				int Numero_lugar = bilhete.getInt("numero_lugar");
				String Disponivel = bilhete.getString("disponivel");
				int Status = bilhete.getInt("status");

				Bilhete auxBilhete = new Bilhete(Id, Id_sessao, Uid, Numero_lugar, Disponivel, Status);
				bilhetes.add(auxBilhete);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bilhetes;
	}

	public static Bilhete parserJsonBilhete(String response) {
		Bilhete auxBilhete = null;

		try {
			JSONObject bilhete = new JSONObject(response);

			int Id = bilhete.getInt("id");
			int Id_sessao = bilhete.getInt("id_sessao");
			String Uid = bilhete.getString("uid");
			int Numero_lugar = bilhete.getInt("numero_lugar");
			String Disponivel = bilhete.getString("disponivel");
			int Status = bilhete.getInt("status");

			auxBilhete = new Bilhete(Id, Id_sessao, Uid, Numero_lugar, Disponivel, Status);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return auxBilhete;
	}
}