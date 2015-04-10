package br.usp.ia.ep1;

public abstract class Classificador {

	private float[][] dadosTreina;
	private float[][] dadosValida;
	private float[][] dadosTeste;
	
	private void swapAleatorio(float[][] array1, float[][] array2) {
		float[] swapAux;
		int pos1 = ((int)Math.random())%array1.length;
		int pos2 = ((int)Math.random())%array2.length;
		
		swapAux = array1[pos1];
		array1[pos1] = array2[pos2];
		array2[pos2] = swapAux;
	}
	
	public void aleatorizaDados(int limiteItera) {
		int qtdItera = 0;
		
		while(qtdItera < limiteItera) {
			swapAleatorio(dadosTreina, dadosValida);
			swapAleatorio(dadosTreina, dadosTeste);
			swapAleatorio(dadosValida, dadosTeste);
			qtdItera++;
		}
	}
}
