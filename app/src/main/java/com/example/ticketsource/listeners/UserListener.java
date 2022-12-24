package com.example.ticketsource.listeners;

public interface UserListener {
	void onUserSignup(String response);

	void onValidateLogin(String token, String username);

	void onCheckRole(Boolean isCliente);

	void onErroLogin();
}
