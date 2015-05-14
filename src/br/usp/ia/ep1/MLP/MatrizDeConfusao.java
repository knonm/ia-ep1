package br.usp.ia.ep1.MLP;

public class MatrizDeConfusao {
	private int[][] matrizDeConfusao;
	private Double[] classes;
	private double quantidadeTestes;

	public MatrizDeConfusao(Double[] classes) {
		matrizDeConfusao = new int[classes.length][classes.length];
		this.classes = classes.clone();			
		}

	public int[][] getMatrizDeConfusao() {
		return matrizDeConfusao;
	}

	public Double[] getClasses() {
		return classes;
	}
	
	public double getQuantidadeTestes() {
		return quantidadeTestes;
	}

	public void adicionarResultado (int real, int predita) {
		matrizDeConfusao[real][predita]++;
		quantidadeTestes++;
	}
	
	public double nCorretos() {
		double nCorretos = 0;
		int linha = 0;
		while (linha < matrizDeConfusao.length) {
			int coluna = linha;
			nCorretos = nCorretos + matrizDeConfusao[linha][coluna];
			linha++;
		}
		return nCorretos;
	}
	
	public double taxaAcuracia() {
		return nCorretos()/quantidadeTestes;
	}
	
	public double nErros() {
		return quantidadeTestes-nCorretos();
	}
	
	public double taxaErro() {
		return nErros()/quantidadeTestes;
	}
}
