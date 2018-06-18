package com.example.anafl.projetofirebase.Listas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anafl.projetofirebase.Activity.MainActivity;
import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.R;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VendedorAdapter extends RecyclerView.Adapter<VendedorAdapter.ViewHolderVendedor> {

    private List<Usuario> dados;
    public static ClickRecyclerViewInterfaceVendedor clickRecyclerViewInterfaceVendedor;


    public VendedorAdapter(List<Usuario> dados, ClickRecyclerViewInterfaceVendedor clickRecyclerViewInterfaceVendedor){
        this.dados = dados;
        this.clickRecyclerViewInterfaceVendedor = clickRecyclerViewInterfaceVendedor;
    }
    public VendedorAdapter(List<Usuario> dados){
        this.dados = dados;
        //this.clickRecyclerViewInterfaceVendedor = clickRecyclerViewInterfaceVendedor;
    }




    @Override
    public VendedorAdapter.ViewHolderVendedor onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.lista_vendedores, parent, false);

        ViewHolderVendedor holderVendedor = new ViewHolderVendedor(itemView);

        return holderVendedor;
    }

    @Override
    public void onBindViewHolder(VendedorAdapter.ViewHolderVendedor holder, int position) {

        Usuario user = dados.get(position);
        double distancia = user.getDistancia();
        String textoDistancia;

        if(distancia < 1000.0){
            textoDistancia = String.valueOf(Math.round(distancia)) + " metros";
        }
        else{
            textoDistancia = String.valueOf(Math.round(distancia / 1000.0)) + " km";
        }

        holder.txtTitulo.setText(user.getNome());
        holder.txtDistancia.setText(textoDistancia);  //apenas para teste

        Glide.with(holder.imgPerfilVendedor.getContext()).load(user.getImagemPerfil()).into(holder.imgPerfilVendedor);



    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderVendedor extends RecyclerView.ViewHolder {

        public TextView txtTitulo;
        public TextView txtDistancia;
        public ImageView imgPerfilVendedor;

        public ViewHolderVendedor(View itemView) {
            super(itemView);


            txtTitulo = (TextView) itemView.findViewById(R.id.txtTituloVendedor);
            txtDistancia = (TextView) itemView.findViewById(R.id.txtDistanciaVendedor);
            imgPerfilVendedor = (ImageView) itemView.findViewById(R.id.imgPerfilVendedor);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRecyclerViewInterfaceVendedor.onCustomClick(dados.get(getLayoutPosition()));
                }
            });


        }

    }
}
