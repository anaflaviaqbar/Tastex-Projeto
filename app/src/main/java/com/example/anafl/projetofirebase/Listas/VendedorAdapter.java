package com.example.anafl.projetofirebase.Listas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Activity.MainActivity;
import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.R;

import java.util.List;

public class VendedorAdapter extends RecyclerView.Adapter<VendedorAdapter.ViewHolderVendedor> {

    private List<Usuario> dados;
    public static ClickRecyclerViewInterfaceVendedor clickRecyclerViewInterfaceVendedor;


    public VendedorAdapter(List<Usuario> dados, ClickRecyclerViewInterfaceVendedor clickRecyclerViewInterfaceVendedor){
        this.dados = dados;
        this.clickRecyclerViewInterfaceVendedor = clickRecyclerViewInterfaceVendedor;
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

        holder.txtTitulo.setText(user.getNome());
        holder.txtDistancia.setText("100m");  //apenas para teste



    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderVendedor extends RecyclerView.ViewHolder {

        public TextView txtTitulo;
        public TextView txtDistancia;

        public ViewHolderVendedor(View itemView) {
            super(itemView);


            txtTitulo = (TextView) itemView.findViewById(R.id.txtTituloVendedor);
            txtDistancia = (TextView) itemView.findViewById(R.id.txtDistanciaVendedor);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRecyclerViewInterfaceVendedor.onCustomClick(dados.get(getLayoutPosition()));
                }
            });


        }

    }
}
