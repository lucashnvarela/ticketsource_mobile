package com.example.ticketsource.views;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.ticketsource.R;
import com.example.ticketsource.listeners.UserListener;
import com.example.ticketsource.models.SingletonTicketsource;
import com.example.ticketsource.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment implements UserListener {

    private EditText etUsername, etEmail, etPassword;
    Button btnSignup;
    public UserListener userListener;

    private User utilizador;
    private Pattern pattern;
    private Matcher matcher;
    private FragmentManager fragmentManager;


    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        fragmentManager = getFragmentManager();
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_signup, container, false);

       SingletonTicketsource.getInstance(getContext()).setUserListener(this);

        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);


        btnSignup = view.findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingletonTicketsource.isConnectedInternet(getContext())) {
                    String mUsername = etUsername.getText().toString();
                    String mEmail = etEmail.getText().toString();
                    String mPassword = etPassword.getText().toString();

                    if (!isUsernameValid(mUsername)) {
                        etUsername.setError("Nome de utilizador inválido");
                        return;
                    }

                    if (!isEmailValid(mEmail)) {
                        etEmail.setError("Email inválido");
                        return;
                    }

                    if (!isPasswordValid(mPassword)) {
                        etPassword.setError("Palavra-passe inválida, é necessário ter mais de 8 caractéres");
                        return;
                    }

                    utilizador = new User(mUsername, mEmail, mPassword);
                    SingletonTicketsource.getInstance(getContext()).registarUserAPI(utilizador, getContext());

                } else Toast.makeText(getContext(), "Sem ligação à Internet", Toast.LENGTH_LONG).show();
            }

        });

        return view;
    }

    private boolean isUsernameValid(String username) {
        if (username == null)  return true;

        return username.length() > 0;
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        if (password == null) return true;

        return password.length() > 7;
    }

    @Override
    public void onUserSignup(String response) {
        Log.e("resposta", response);
        if(response.equals("true")){
            Fragment fragment = new LoginFragment();
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).addToBackStack(null).commit();
            Toast.makeText(getContext(), "Bem Vindo(a), a sua conta foi registada com sucesso", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValidateLogin(String token, String username) {

    }

    @Override
    public void onErroLogin() {

    }
}