package com.example.ticketsource.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FavoritoDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "favoritoDB";
	private static final int DB_VERSION = 1;

	private static final String TABLE_FAVORITO = "favorito";

	private static final String ID_EVENTO = "id_evento";
	private static final String TITULO_EVENTO = "titulo_evento";
	private static final String DESCRICAO_EVENTO = "descricao_evento";
	private static final String CATEGORIA_EVENTO = "categoria_evento";
	private static final String NOMEPIC_EVENTO = "nomepic_evento";

	private final SQLiteDatabase db;

	public FavoritoDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlCreateTableFavorito = "CREATE TABLE " + TABLE_FAVORITO + " (" +
				ID_EVENTO + " INTEGER PRIMARY KEY, " +
				TITULO_EVENTO + " TEXT NOT NULL , " +
				DESCRICAO_EVENTO + " TEXT NOT NULL, " +
				CATEGORIA_EVENTO + " TEXT CHECK( " + CATEGORIA_EVENTO + " IN ('Desporto', 'Música', 'Teatro', 'Festival')) NOT NULL, " +
				NOMEPIC_EVENTO + " TEXT);";
		db.execSQL(sqlCreateTableFavorito);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sqlDropTableFavorito = "DROP TABLE IF EXISTS " + TABLE_FAVORITO;
		db.execSQL(sqlDropTableFavorito);
		this.onCreate(db);
	}

	public void addFavoritoBD(Evento evento) {
		ContentValues values = new ContentValues();
		values.put(ID_EVENTO, evento.getId());
		values.put(TITULO_EVENTO, evento.getTitulo());
		values.put(DESCRICAO_EVENTO, evento.getDescricao());
		values.put(CATEGORIA_EVENTO, evento.getCategoria());
		values.put(NOMEPIC_EVENTO, evento.getNomepic());

		this.db.insert(TABLE_FAVORITO, null, values);
	}

	public boolean deleteFavoritoBD(int id) {
		return this.db.delete(TABLE_FAVORITO, "id=?", new String[]{id + ""}) > 0;
	}

	public void deleteAllFavoritosBD() {
		this.db.delete(TABLE_FAVORITO, null, null);
	}

	public ArrayList<Evento> getAllFavoritosBD() {
		ArrayList<Evento> eventos = new ArrayList<>();
		Cursor cursor = this.db.query(TABLE_FAVORITO, new String[]{ID_EVENTO, TITULO_EVENTO, DESCRICAO_EVENTO, CATEGORIA_EVENTO, NOMEPIC_EVENTO},
				null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				Evento auxEventoFavorito = new Evento(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4));
				eventos.add(auxEventoFavorito);
			} while (cursor.moveToNext());
		}
		return eventos;
	}
}
