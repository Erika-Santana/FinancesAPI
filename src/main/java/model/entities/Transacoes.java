package model.entities;

import java.time.LocalDateTime;

import enumTipos.TipoCategoria;
import enumTipos.TipoTransacao;

public class Transacoes {
	
	private int ID;
	private String descricao;
	private TipoTransacao tipoTransacao;
	private TipoCategoria tipoCategoria;
	private LocalDateTime dateCreation;
	private double valor;
	
	public Transacoes() {
		// TODO Auto-generated constructor stub
	}
	
	public Transacoes(int iD, String descricao, TipoTransacao tipoTransacao, TipoCategoria tipoCategoria,
			LocalDateTime dateCreation, double valor) {
		super();
		ID = iD;
		this.descricao = descricao;
		this.tipoTransacao = tipoTransacao;
		this.tipoCategoria = tipoCategoria;
		this.dateCreation = dateCreation;
		this.valor = valor;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public TipoTransacao getTipoTransacao() {
		return tipoTransacao;
	}
	public void setTipoTransacao(TipoTransacao tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}
	public TipoCategoria getTipoCategoria() {
		return tipoCategoria;
	}
	public void setTipoCategoria(TipoCategoria tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}
	public LocalDateTime getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	
	
}
