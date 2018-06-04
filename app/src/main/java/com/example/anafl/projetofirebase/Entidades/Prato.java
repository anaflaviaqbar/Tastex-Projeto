package com.example.anafl.projetofirebase.Entidades;

public class Prato{

    private String idVendedor; // id do prato é o mesmo id do vendedor
    private String nome;
    private float preco;
    private String descricao;
    private String uidPrato;
    private int tipoPrato;  /* 0 - Sem Classificação ,1 - Normal, 2 - Low Carb, 4 - Vegetariano, 5 - Vegano */
    private String imgPratoUrl;

    public Prato (){

    }
    public Prato(String nome){
        this.nome = nome;
        this.preco = (float) 13.5;
        this.descricao = "Arroz e feijão";

    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public String getUidPrato() {
        return uidPrato;
    }

    public void setUidPrato(String uidPrato) {
        this.uidPrato = uidPrato;
    }

    public int getTipoPrato() {
        return tipoPrato;
    }

    public void setTipoPrato(int tipoPrato) {
        this.tipoPrato = tipoPrato;
    }

    public String getImgPratoUrl() {
        return imgPratoUrl;
    }

    public void setImgPratoUrl(String imgPratoUrl) {
        this.imgPratoUrl = imgPratoUrl;
    }
}
