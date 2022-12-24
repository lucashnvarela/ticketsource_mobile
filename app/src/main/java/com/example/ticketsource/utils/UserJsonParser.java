package com.example.ticketsource.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.ticketsource.models.User;

public class UserJsonParser {
	public static ArrayList<User> parserJsonUser(JSONArray response) {
		ArrayList<User> users = new ArrayList<>();

		try {
			for (int i = 0; i < response.length(); i++) {
				JSONObject user = (JSONObject) response.get(i);

				String Username = user.getString("username");
				String Email = user.getString("email");
				String Password = user.getString("password");

				User auxUser = new User(Username, Email, Password);
				users.add(auxUser);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return users;
	}

	public static User parserJsonUser(String response) {
		User auxUser = null;

		try {
			JSONObject user = new JSONObject(response);
			String Username = user.getString("username");
			String Email = user.getString("email");
			//String Password = user.getString("password");

			auxUser = new User(Username, Email, null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return auxUser;
	}

	public static String parserJsonLogin(String response) {
		String token = null;
		try {
			JSONObject login = new JSONObject(response);
			token = login.getString("verification_token");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return token;
	}

}
