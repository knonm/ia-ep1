package br.usp.ia.ep1.dados;


public class IteratorDados {

	private Dados dados;
	private int index;
	
	public float getClasseCorrente() {
		return dados.getDado(index)[Dados.POS_ATRIB_CLASSE];
	}
	
	public float[] next() {
		return dados.getDado(index++);
	}
	
	public IteratorDados(Dados dados) {
		this.dados = dados;
		this.index = 0;
	}
}
