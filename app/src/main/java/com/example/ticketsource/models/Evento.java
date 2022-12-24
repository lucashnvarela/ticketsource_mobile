package com.example.ticketsource.models;

public class Evento {
	private int id;
	private String titulo, descricao, categoria, nomepic;

	public Evento(int id, String titulo, String descricao, String categoria, String nomepic) {

		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.categoria = categoria;
		this.nomepic = nomepic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNomepic() {
		return nomepic;
	}

	public void setNomepic(String nome_pic) {
		this.nomepic = nomepic;
	}
}
