package com.example.ticketsource.models;

public class Bilhete {
	private int id, id_sessao, numero_lugar, status;
	private String uid, disponivel;

	public Bilhete(int id, int id_sessao, String uid, int numero_lugar, String disponivel, int status) {

		this.id = id;
		this.id_sessao = id_sessao;
		this.uid = uid;
		this.numero_lugar = numero_lugar;
		this.disponivel = disponivel;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdSessao() {
		return id_sessao;
	}

	public void setIdSessao(int id_sessao) {
		this.id_sessao = id_sessao;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getNumeroLugar() {
		return numero_lugar;
	}

	public void setNumeroLugar(int numero_lugar) {
		this.numero_lugar = numero_lugar;
	}

	public String getDisponivel() {
		return disponivel;
	}

	public void setDisponivel(String disponivel) {
		this.disponivel = disponivel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
