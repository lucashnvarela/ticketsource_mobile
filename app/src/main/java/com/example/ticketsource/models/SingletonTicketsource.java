package com.example.ticketsource.models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.ticketsource.listeners.FavoritoListener;
import com.example.ticketsource.listeners.EventoListener;
import com.example.ticketsource.listeners.BilheteListener;
import com.example.ticketsource.listeners.UserListener;
import com.example.ticketsource.utils.EventoJsonParser;
import com.example.ticketsource.utils.BilheteJsonParser;
import com.example.ticketsource.utils.UserJsonParser;

public class SingletonTicketsource {
	private static final String localhost = "http://192.168.1.73:8080";
	private static final int ADICIONAR_BD = 1;
	private static final int REMOVER_BD = 3;

	private static SingletonTicketsource instance = null;
	private User user;
	private Evento evento;
	private ArrayList<Evento> eventos;
	private ArrayList<Bilhete> bilhetes;
	private FavoritoDBHelper favoritoBD;
	private static RequestQueue volleyQueue = null; //static para ser fila unica

	//API
	//User
	private static final String mUrlAPIRegistarUser = localhost + "/v1/user/registo";
	private static final String mUrlAPILoginUser = localhost + "/v1/user/login";
	private static final String mUrlAPIUserCheckRole = localhost + "/v1/user/check/";

	//Evento
	private static final String mUrlAPIEventos = localhost + "/v1/evento/lista";

	//Bilhete
	private static final String mUrlAPIBilhetes = localhost + "/v1/bilhete/lista/";
	private static final String mUrlAPIBilheteCancelar = localhost + "/v1/bilhete/cancelar/";
	private static final String mUrlAPIBilheteCheckin = localhost + "/v1/bilhete/checkin/";

	//Favorito
	private static final String mUrlAPIFavoritos = localhost + "/v1/favorito/lista/";
	private static final String mUrlAPIFavoritoAdd = localhost + "/v1/favorito/adicionar/";
	private static final String mUrlAPIFavoritoDelete = localhost + "/v1/favorito/remover/";
	private static final String mUrlAPIFavoritoCheck = localhost + "/v1/favorito/check/";

	private UserListener userListener;
	protected EventoListener eventoListener;
	protected BilheteListener bilheteListener;
	public FavoritoListener favoritoListener;

	public static synchronized SingletonTicketsource getInstance(Context context) {
		if (instance == null) {
			instance = new SingletonTicketsource(context);
			volleyQueue = Volley.newRequestQueue(context);
		}

		return instance;
	}

	private SingletonTicketsource(Context context) {
		eventos = new ArrayList<>();
		bilhetes = new ArrayList<>();
		favoritoBD = new FavoritoDBHelper(context);
	}

	public void setFavoritoListener(FavoritoListener favoritoListener) {
		this.favoritoListener = favoritoListener;
	}

	public void setUserListener(UserListener userListener) {
		this.userListener = userListener;
	}

	public void setEventoListener(EventoListener eventoListener) {
		this.eventoListener = eventoListener;
	}

	public void setBilheteListener(BilheteListener bilheteListener) {
		this.bilheteListener = bilheteListener;
	}

	public Evento getEvento(int id) {
		for (Evento p : eventos) {
			if (p.getId() == id) return p;
		}
		return null;
	}

	public Bilhete getBilhete(int id) {
		for (Bilhete p : bilhetes) {
			if (p.getId() == id) return p;
		}
		return null;
	}

	/**
	 * Metodos para aceder a BD local
	 */

	public ArrayList<Evento> getFavoritosDB() {
		eventos = favoritoBD.getAllFavoritosBD();

		return eventos;
	}

	public void addFavoritoBD(Evento evento) {
		favoritoBD.addFavoritoBD(evento);
	}

	public void addFavoritosBD(ArrayList<Evento> eventos) {
		favoritoBD.deleteAllFavoritosBD();
		for (Evento p : eventos) addFavoritoBD(p);
	}


	public void deleteFavoritoBD(int id) {
		Evento evento = getEvento(id);
		if (evento != null) if (favoritoBD.deleteFavoritoBD(id)) eventos.remove(id);
	}

	public static boolean isConnectedInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * Métodos de acesso à API - User
	 */

	public void signupUserAPI(final User user, final Context applicationContext) {
		StringRequest request = new StringRequest(Request.Method.POST, mUrlAPIRegistarUser, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (userListener != null) userListener.onUserSignup(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", user.getUsername());
				params.put("email", user.getEmail());
				params.put("password", user.getPassword());

				return params;
			}
		};
		volleyQueue.add(request);
	}

	public void loginUserAPI(final String username, final String password, final Context applicationContext) {
		StringRequest request = new StringRequest(Request.Method.POST, mUrlAPILoginUser, new Response.Listener<String>() {

			public void onResponse(String response) {
				String token = UserJsonParser.parserJsonLogin(response);
				if (userListener != null) userListener.onValidateLogin(token, username);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (userListener != null) userListener.onErroLogin();
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<>();
				params.put("username", username);
				params.put("password", password);
				return params;
			}
		};
		volleyQueue.add(request);
	}

	public void checkRoleAPI(final Context applicationContext, String token) {
		StringRequest request = new StringRequest(Request.Method.GET, mUrlAPIUserCheckRole + token, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				boolean isCliente = response.equals("true");

				if (userListener != null) userListener.onCheckRole(isCliente);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

		volleyQueue.add(request);
	}

	/**
	 * Métodos de acesso à API - Evento
	 */

	public void getAllEventosAPI(final Context applicationContext) {
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIEventos, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				eventos = EventoJsonParser.parserJsonEvento(response);

				if (eventoListener != null) eventoListener.onRefreshListaEventos(eventos);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
		volleyQueue.add(request);
	}

	/**
	 * Métodos de acesso à API - Favoritos
	 */

	public void getAllFavoritosAPI(final Context applicationContext, String token) {
		if (!isConnectedInternet(applicationContext)) {
			Toast.makeText(applicationContext, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
			addFavoritosBD(eventos);

			if (favoritoListener != null) favoritoListener.onRefreshListaFavoritos(eventos);

		} else {
			JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIFavoritos + token, null, new Response.Listener<JSONArray>() {

				@Override
				public void onResponse(JSONArray response) {
					eventos = EventoJsonParser.parserJsonEvento(response);
					addFavoritosBD(eventos);

					if (favoritoListener != null) favoritoListener.onRefreshListaFavoritos(eventos);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(applicationContext, "Não tem nenhum evento adicionado aos favoritos", Toast.LENGTH_SHORT).show();
				}
			});
			volleyQueue.add(request);
		}
	}

	public void addFavoritoAPI(final Context applicationContext, final Evento evento, final String token) {
		StringRequest request = new StringRequest(Request.Method.POST, mUrlAPIFavoritoAdd + evento.getId() + "/" + token, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				addFavoritoBD(evento);
				if (favoritoListener != null) favoritoListener.onAddFavorito();

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		volleyQueue.add(request);
	}

	public void deleteFavoritoAPI(final Context applicationContext, Evento evento, String token) {

		StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPIFavoritoDelete + evento.getId() + "/" + token, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (favoritoListener != null) favoritoListener.onDeleteFavorito();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		volleyQueue.add(request);
	}

	public void checkFavoritoAPI(final Context applicationContext, final Evento evento, String token) {
		StringRequest request = new StringRequest(Request.Method.GET, mUrlAPIFavoritoCheck + evento.getId() + "/" + token, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				boolean isFavorito = response.equals("true");

				if (favoritoListener != null) favoritoListener.onCheckFavorito(isFavorito);

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		volleyQueue.add(request);
	}

	public void onUpdateListaFavoritosBD(Evento evento, int opc) {
		switch (opc) {
			case ADICIONAR_BD:
				addFavoritoBD(evento);
				break;
			case REMOVER_BD:
				deleteFavoritoBD(evento.getId());
				break;
		}
	}

	/**
	 * Métodos de acesso à API - Bilhete
	 */

	public void getAllBilhetesAPI(final Context applicationContext, String token) {
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIBilhetes + token, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				bilhetes = BilheteJsonParser.parserJsonBilhete(response);

				if (bilheteListener != null) bilheteListener.onRefreshListaBilhetes(bilhetes);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
		volleyQueue.add(request);
	}

	public void cancelarBilheteAPI(final Context applicationContext, final Bilhete bilhete, String token) {
		StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPIBilheteCancelar + bilhete.getId() + "/" + token, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (bilheteListener != null) bilheteListener.onCancelarBilhete();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		volleyQueue.add(request);
	}

	public void checkinBilheteAPI(final Context applicationContext, final Bilhete bilhete) {
		StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPIBilheteCheckin + bilhete.getId(), new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (bilheteListener != null) bilheteListener.onCheckinBilhete();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
		volleyQueue.add(request);
	}
}
