package com.example.anafl.projetofirebase.Entidades;



public class Pedido {

    private String idPedido;
    private String nomeVendedor;
    private String idVendedor;
    private String nomeComprador;
    private String idComprador;
    private String nomePrato;
    private float precoPrato;
    private String descricaoPrato;
    private String uidPrato;
    private String dataPedido;
    private int status; // 0 - pedido pendente, 1 - pedido confirmado, 2 - pedido cancelado

    public Pedido(){

    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(String idComprador) {
        this.idComprador = idComprador;
    }

    public String getNomePrato() {
        return nomePrato;
    }

    public void setNomePrato(String nomePrato) {
        this.nomePrato = nomePrato;
    }

    public float getPrecoPrato() {
        return precoPrato;
    }

    public void setPrecoPrato(float precoPrato) {
        this.precoPrato = precoPrato;
    }

    public String getDescricaoPrato() {
        return descricaoPrato;
    }

    public void setDescricaoPrato(String descricaoPrato) {
        this.descricaoPrato = descricaoPrato;
    }

    public String getUidPrato() {
        return uidPrato;
    }

    public void setUidPrato(String uidPrato) {
        this.uidPrato = uidPrato;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }
}
