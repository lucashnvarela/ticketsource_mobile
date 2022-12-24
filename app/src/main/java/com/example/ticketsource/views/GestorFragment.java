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
import com.example.ticketsource.listeners.BilheteListener;
import com.example.ticketsource.models.Bilhete;
import com.example.ticketsource.models.SingletonTicketsource;

import java.util.ArrayList;

public class GestorFragment extends Fragment implements BilheteListener {

    private EditText etUsername, etPassword;
    private FragmentManager fragmentManager;

    public GestorFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_gestor, container, false);

        SingletonTicketsource.getInstance(getContext()).setBilheteListener(this);

        Button btnScan = view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onRefreshListaBilhetes(ArrayList<Bilhete> listaBilhetes) {

    }

    @Override
    public void onCancelarBilhete() {

    }

    @Override
    public void onCheckinBilhete() {

    }
}