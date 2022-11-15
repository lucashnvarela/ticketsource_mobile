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
import com.example.ticketsource.listeners.EventosListener;
import com.example.ticketsource.listeners.UserListener;
import com.example.ticketsource.utils.EventoJsonParser;
import com.example.ticketsource.utils.UtilizadoresParserJson;

public class SingletonTicketsource {

    private static final int ADICIONAR_BD = 1;

    private static final int REMOVER_BD = 3;

    private static SingletonTicketsource instance = null;
    private User utilizador;
    private ArrayList<Evento> eventos;
    private Evento evento;
    private EventosFavoritosDBHelper eventosFavoritosBD;
    private static RequestQueue volleyQueue = null; //static para ser fila unica
    private static final String mUrlAPIRegistarUser = "http://192.168.1.77:8080/v1/user/registo";
    private static final String mUrlAPIUserLogin = "http://192.168.1.77:8080/v1/user/login";
    private static final String mUrlAPIEditarRegistoUser = "http://192.168.1.77:8080/v1/user/editar";
    private static final String mUrlAPIApagarUser = "http://192.168.1.77:8080/v1/user/apagar";
    private static final String mUrlAPIUserDetalhes = "http://192.168.1.77:8080/v1/user/detalhes";
    private static final String mUrlAPIEventos = "http://192.168.1.77:8080/v1/evento/all";
    private static final String mUrlAPIEventoPesquisa = "http://192.168.1.77:8080/v1/evento/pesquisa";
    private static final String mUrlAPIEventosFavoritos = "http://192.168.1.77:8080/v1/favorito/info";
    private static final String mUrlAPIEventosFavoritosAdicionar = "http://192.168.1.77:8080/v1/favorito/add";
    private static final String mUrlAPIEventosFavoritosEliminar = "http://192.168.1.77:8080/v1/favorito/delete";
    private static final String mUrlAPIEventosFavoritosCheck = "http://192.168.1.77:8080/v1/favorito/check";

    private UserListener userListener;
    protected EventosListener eventosListener;
    public FavoritoListener favoritoListener;

    public static synchronized SingletonTicketsource getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonTicketsource(context);
            volleyQueue = Volley.newRequestQueue(context); //Cria apenas uma fila de pedidos
        }
        return instance;
    }

    private SingletonTicketsource(Context context) {
        eventos = new ArrayList<>();
        eventosFavoritosBD = new EventosFavoritosDBHelper(context);

    }

    public void setFavoritosListener(FavoritoListener favoritosListener){
        this.favoritoListener = favoritosListener;
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }

    public void setEventosListener(EventoListener eventosListener) {
        this.eventoListener = eventosListener;
    }

    public Evento getEvento(int id){
        for(Evento p: eventos){
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }

    /*********** Metodos para aceder a BD local ************/

    public ArrayList<Evento> getEventosFavoritosDB() {
        eventos = eventosFavoritosBD.getAllEventosFavoritosBD();

        return eventos;
    }

    public void addEventoFavoritoBD(Evento eventoFavorito){
        eventosFavoritosBD.addEventoFavoritoBD(eventoFavorito);
    }

    public void addEventosFavoritosBD(ArrayList<Evento> eventos){
        eventosFavoritosBD.deleteAlleventosFavoritosBD();
        for(Evento p : eventos)
            addEventoFavoritoBD(p);
    }


    public void deleteEventoFavoritoBD(int codigo_evento){
        Evento evento = getEvento(codigo_evento);
        if(evento!=null){
            if (eventosFavoritosBD.deleteEventoFavoritoBD(codigo_evento)){
                eventos.remove(codigo_evento);
            }
        }
    }

    public static boolean isConnectedInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /********* Métodos de acesso à API - Utilizador****/
    /**
     * Registar User API
     */

    public void signupUserAPI(final User utilizador, final Context context) {
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIRegistarUser, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (userListener != null) {
                    userListener.onUserSignup(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", utilizador.getUsername());
                params.put("email", utilizador.getEmail());
                params.put("password", utilizador.getPassword());

                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void getUserAPI(final Context context, String token) {

            StringRequest req = new StringRequest(Request.Method.GET, mUrlAPIUserDetalhes + "/" + token, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    utilizador = UserParserJson.parserJsonUtilizador(response);

                    if (userListener != null)
                        userListener.onLoadEditarRegisto(utilizador);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
    }


    public void editarUtilizadorAPI(final Utilizador utilizador, final Context context, final String username) {
        StringRequest req = new StringRequest(Request.Method.PUT, mUrlAPIEditarRegistoUser + "/" + username, new Response.Listener<String>() {

            public void onResponse(String response) {
                if (userListener != null) {
                    userListener.onRefreshDetalhes(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", utilizador.getUsername());
                params.put("email", utilizador.getEmail());
                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void loginUserAPI(final String username, final String password, final Context context) {
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIUserLogin, new Response.Listener<String>() {

            public void onResponse(String response) {
                String token = UtilizadoresParserJson.parserJsonLogin(response);
                if (userListener != null) {
                    userListener.onValidateLogin(token, username);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (userListener != null) {
                    userListener.onErroLogin();
                }
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
        volleyQueue.add(req);
    }


    public void apagarContaAPI(String username, final Context context) {
        StringRequest req = new StringRequest(Request.Method.PATCH, mUrlAPIApagarUser + "/" + username, new Response.Listener<String>() {

            public void onResponse(String response) {
                if (userListener != null) {
                    userListener.onDeleteUser(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    public void getAllEventosAPI(final Context context) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIEventos, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                eventos = EventoJsonParser.parserJsonEventos(response);

                if(eventosListener != null) eventosListener.onRefreshListaEventos(eventos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        volleyQueue.add(request);
    }

    public void getEventoPesquisa(String pesquisa, final Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, mUrlAPIEventoPesquisa + "/" + pesquisa, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                evento = EventoJsonParser.parserJsonEvento(response);

                if(eventosListener != null) {
                    eventosListener.onRefreshListaEventos(eventos);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        volleyQueue.add(request);
    }

    /********* Métodos de acesso à API - Favoritos****/
    /**
     *
     */

    public void getAllEventosFavoritosAPI(final Context context, String token) {
        if(!isConnectedInternet(context)){
            Toast.makeText(context, "Não tem ligação à internet!", Toast.LENGTH_SHORT).show();
            addeventosFavoritosBD(eventos);
            if (favoritoListener != null) {
                favoritoListener.onRefreshListaFavoritosEventos(eventos);
            }
        }else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIEventosFavoritos + "/" + token, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    eventos = EventoJsonParser.parserJsonEventos(response);
                    addeventosFavoritosBD(eventos);
                    if (favoritoListener != null) {
                        favoritoListener.onRefreshListaFavoritosEventos(eventos);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Não tem nenhum evento adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void addEventoFavoritoAPI(final Context context, final Evento evento, final String token) {
        StringRequest req = new StringRequest(Request.Method.POST, mUrlAPIEventosFavoritosAdicionar + "/" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addEventoFavoritoBD(evento);
                if (favoritoListener != null) {
                    favoritoListener.onAddEventosFavoritos();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("codigo_evento", evento.getCodigo_evento() + "");
                params.put("token", token);
                    /*
                    JSONObject param = new JSONObject(params);
                    Log.e("MAP:", param+"");*/

                return params;
            }
        };
        volleyQueue.add(req);
    }

    public void deleteEventoFavoritoAPI(final Context applicationContext, Evento evento, String token) {

        StringRequest req = new StringRequest(Request.Method.DELETE, mUrlAPIEventosFavoritosEliminar + "/" + evento.getCodigo_evento() + "/" + token, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (favoritoListener != null) {
                    favoritoListener.onDeleteEventosFavoritos();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    public void checkFavoritoAPI(final Context applicationContext, final Evento evento, String token) {
        StringRequest req = new StringRequest(Request.Method.GET, mUrlAPIEventosFavoritosCheck + "/" + evento.getId() + "/" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean favorito = response.equals("true") ? true : false;

                if (favoritoListener != null)
                    favoritoListener.oncheckEventoFavorito(favorito);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(applicationContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(req);
    }

    public void OnUpdateListaFavoritosBD(Evento evento, int operacao) {
        switch (operacao) {
            case ADICIONAR_BD:
                addEventoFavoritoBD(evento);
                break;
            case REMOVER_BD:
                deleteEventoFavoritoBD(evento.getId());
                break;
        }
    }
}
