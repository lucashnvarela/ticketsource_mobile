package com.example.ticketsource.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.ticketsource.R;
import com.example.ticketsource.listeners.UserListener;
import com.example.ticketsource.models.User;


public class UserFragment extends Fragment implements UserListener {

    private EditText etUsername, etEmail, etPassword;
    private FragmentManager fragmentManager;
    private User utilizador;

    private Pattern pattern;
    private Matcher matcher;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onUserSignup(String response) {
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