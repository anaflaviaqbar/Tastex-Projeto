package com.example.anafl.projetofirebase.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anafl.projetofirebase.Listas.ClickRecyclerViewInterfaceVendedor;
import com.example.anafl.projetofirebase.R;

public class Recomendacoes extends Fragment implements ClickRecyclerViewInterfaceVendedor {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_recomendacoes, container, false);

        return view;
    }

    @Override
    public void onCustomClick(Object object) {

    }
}
