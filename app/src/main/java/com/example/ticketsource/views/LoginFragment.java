package com.example.ticketsource.views;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ticketsource.R;
import com.example.ticketsource.listeners.UserListener;
import com.example.ticketsource.models.SingletonTicketsource;
import com.example.ticketsource.models.User;

public class LoginFragment extends Fragment implements UserListener {

    private EditText etUsername, etPassword;
    private FragmentManager fragmentManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        SingletonTicketsource.getInstance(getContext()).setUserListener(this);

        fragmentManager = getFragmentManager();
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonTicketsource.isConnectedInternet(getContext())) {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();

                    if (!isUsernameValid(username)) {
                        etUsername.setError("Nome de utilizador inválido");
                        return;
                    }

                    if (!isPasswordValid(password)) {
                        etPassword.setError("Palavra-passe inválida");
                        return;
                    }

                    SingletonTicketsource.getInstance(getContext()).loginUserAPI(username, password, getContext());
                }
            }
        });

        Button btnRegistar = view.findViewById(R.id.btnSignup);
        btnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignupFragment();
                fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }

    private boolean isUsernameValid(String username) {
        if (username == null) return true;

        return username.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        if (password == null) return true;

        return password.length() > 0;
    }


    @Override
    public void onUserSignup(String response) {

    }

    @Override
    public void onValidateLogin(String token, String username) {
        if(token != null){
            guardarInfoSharedPref(token, username);
            Fragment fragment = new MainFragment();
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
            Toast.makeText(getContext(), "Bem Vindo", Toast.LENGTH_LONG).show();
        } else Toast.makeText(getContext(), "Login Inválido", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErroLogin() {
        Toast.makeText(getContext(), "A sua conta não cumpre os requisitos para que seja possivel iniciar sessão", Toast.LENGTH_LONG).show();
    }

    private void guardarInfoSharedPref(String token, String username) {
        SharedPreferences sharedPreferencesUser = getActivity().getSharedPreferences(MenuMainActivity.INFO_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();

        editor.putString(MenuMainActivity.TOKEN, token);
        editor.putString(MenuMainActivity.USERNAME, username);

        editor.commit();
    }
}