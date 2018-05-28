package com.example.anafl.projetofirebase.Listas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anafl.projetofirebase.Entidades.Pedido;
import com.example.anafl.projetofirebase.R;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolderPedido> {

    private List<Pedido> dados;
    private static ClickRecyclerViewInterfacePedido clickRecyclerViewInterfacePedido;


    public PedidoAdapter(List<Pedido> dados, ClickRecyclerViewInterfacePedido clickRecyclerViewInterfacePedido){
        this.dados = dados;
        this.clickRecyclerViewInterfacePedido = clickRecyclerViewInterfacePedido;
    }

    @Override
    public PedidoAdapter.ViewHolderPedido onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.lista_pedidos,parent, false);
        ViewHolderPedido viewHolderPedido = new ViewHolderPedido(itemView);

        return viewHolderPedido;
    }

    @Override
    public void onBindViewHolder(PedidoAdapter.ViewHolderPedido holder, int position) {
        Pedido pedido = dados.get(position);

        if(pedido.getStatus() == 0){
            holder.txtStatusPedidoLisPedidos.setText("Pendente");
        }else if(pedido.getStatus() == 1){
            holder.txtStatusPedidoLisPedidos.setText("Confirmado");
        }else if(pedido.getStatus() == 2){
            holder.txtStatusPedidoLisPedidos.setText("Cancelado");
        }

        holder.txtNomePratoLisPedidos.setText(pedido.getNomePrato());
        holder.txtPrecoPratoLisPedidos.setText(pedido.getPrecoPrato() + " R$");
        holder.txtDataPedidoLisPedidos.setText(pedido.getDataPedido());
        holder.txtNomeCompradorLisPedidos.setText(pedido.getNomeComprador());
        holder.txtNomeVendedorLisPedidos.setText(pedido.getNomeVendedor());

    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    public class ViewHolderPedido extends RecyclerView.ViewHolder {

        public TextView txtStatusPedidoLisPedidos;
        public TextView txtNomeCompradorLisPedidos;
        public TextView txtNomeVendedorLisPedidos;
        public TextView txtNomePratoLisPedidos;
        public TextView txtPrecoPratoLisPedidos;
        public TextView txtDataPedidoLisPedidos;


        public ViewHolderPedido(View itemView) {
            super(itemView);

            txtStatusPedidoLisPedidos = (TextView) itemView.findViewById(R.id.txtStatusPedidoLisPedidos);
            txtNomeCompradorLisPedidos = (TextView) itemView.findViewById(R.id.txtNomeCompradorLisPedidos);
            txtNomeVendedorLisPedidos = (TextView) itemView.findViewById(R.id.txtNomeVendedorLisPedidos);
            txtNomePratoLisPedidos = (TextView) itemView.findViewById(R.id.txtNomePratoLisPedidos);
            txtPrecoPratoLisPedidos = (TextView) itemView.findViewById(R.id.txtPrecoPratoLisPedidos);
            txtDataPedidoLisPedidos = (TextView) itemView.findViewById(R.id.txtDataPedidoLisPedidos);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRecyclerViewInterfacePedido.onCustomClick(dados.get(getLayoutPosition()));
                }
            });
        }
    }
}
