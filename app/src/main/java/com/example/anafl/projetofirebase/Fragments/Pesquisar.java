package com.example.anafl.projetofirebase.Fragments;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anafl.projetofirebase.Listas.SectionsPageAdapter;
import com.example.anafl.projetofirebase.R;

public class Pesquisar extends Fragment {

    private SectionsPageAdapter sectionsPageAdapter;

    private ViewPager viewPager;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar, container, false);

        sectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());

        viewPager = (ViewPager) view.findViewById(R.id.vpPesquisar);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabsPesquisar);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    /*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favoritos);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.vpPesquisar);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsPesquisar);
        tabLayout.setupWithViewPager(viewPager);
    }
    */


    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new Comprar(), "Usuário");
        adapter.addFragment(new ComprarPrato(), "Prato");

        viewPager.setAdapter(adapter);
    }
}
