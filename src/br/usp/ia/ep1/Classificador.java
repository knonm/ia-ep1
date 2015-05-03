package br.usp.ia.ep1;

public abstract class Classificador {

	private float[][] dadosTreinamento;
	private float[][] dadosValidacao;
	private float[][] dadosTeste;
	
	private float txAprend;
	
	private void swapAleatorio(float[][] array1, float[][] array2) { // fazer a validacao classes iguais
		float[] swapAux;
		int pos1 = ((int)Math.random())%array1.length;
		int pos2 = ((int)Math.random())%array2.length;
		
		swapAux = array1[pos1];
		array1[pos1] = array2[pos2];
		array2[pos2] = swapAux;
	}
	
	public void aleatorizaDados(int limiteIteracao) {
		int qtdItera = 0;
		
		while(qtdItera < limiteIteracao) {
			if(((int)Math.random())%2 == 0) {
				swapAleatorio(dadosTreinamento, dadosValidacao);
			}
			if(((int)Math.random())%2 == 0) {
				swapAleatorio(dadosTreinamento, dadosTeste);
			}
			if(((int)Math.random())%2 == 0) {
				swapAleatorio(dadosValidacao, dadosTeste);
			}
			qtdItera++;
		}
	}
}
