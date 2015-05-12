package br.usp.ia.ep1.utils;

public class MN {
	
	public static float[][] transformarArrayStringParaFloat(String[] dados, String chrDelimit) {
		float[][] dadosFloat = new float[dados.length][];
		float[] dadoFloat;
		String[] atribs;
		int i = 0;
		for(String linha : dados) {
			atribs = linha.split(chrDelimit);
			dadoFloat = new float[atribs.length];
			for(int j = dadoFloat.length-1; j > -1; j--) {
				dadoFloat[j] = Float.valueOf(atribs[j]);
			}
			dadosFloat[i] = dadoFloat;
			i++;
		}	
		return dadosFloat;
	}
	
	public static int valorMaximoClasse(float[][] dados){
		int valorMaximo = 0;
		for (int i = 0; i < dados.length; i++){
			if(dados[i][dados[i].length-1] > valorMaximo) valorMaximo = (int) dados[i][dados[i].length-1];
		}		
		return valorMaximo;
	}
	
	public static int valorMinimoClasse(float[][] dados){
		int valorMinimo = 0;
		for (int i = 0; i < dados.length; i++){
			if(dados[i][dados[i].length-1] < valorMinimo) valorMinimo = (int) dados[i][dados[i].length-1];
		}		
		return valorMinimo;
	}
	
	// transforma uma matriz tridimencional em bidimencional, em relacao a qual arquivo escolhido
	public static float[][] transformaBidimensional(float[][][] tridimencional, int arquivo){
			float[][] aux = new float[tridimencional[arquivo].length][tridimencional[arquivo][0].length];
		
            //System.out.println("Transformando Bidimencional");
			//System.out.println(aux.length);
			//System.out.println(aux[0].length);
			for(int i = 0; i < aux.length; i++){
				for( int j = 0; j < aux[i].length; j++){
					aux[i][j] = tridimencional [arquivo][i][j]; // passa o valor de um para o outro
				}
			}
		
		return aux;
	}
	
	// transforma tres matrizes tridimencionais em uma tridimencional
	public static float[][][] transformaTridimensional(float[][] primeira, float[][] segunda, float[][] terceira){
			float[][][] aux = new float[3][][]; // instancia a auxiliar (a terceira dimencao, que e a dos arquivos)
		
			aux[0] = new float[primeira.length][primeira[0].length]; // instancia a dimencao dos dados dos valores
			aux[1] = new float[segunda.length][segunda[0].length];
			aux[2] = new float[terceira.length][terceira[0].length];
		
			for(int i = 0; i < primeira.length; i++){ // copia o valor para a auxiliar
				for( int j = 0; j < primeira[i].length; j++){
					aux[0][i][j] = primeira[i][j];
				}
			}
			for(int i = 0; i < segunda.length; i++){
				for( int j = 0; j < segunda[i].length; j++){
					aux[1][i][j] = segunda[i][j];
				}
			}
			for(int i = 0; i < terceira.length; i++){
				for( int j = 0; j < terceira[i].length; j++){
					aux[2][i][j] = terceira[i][j];
				}
			}	
		
		return aux;
	}
	
	public static float[] descDados(float[][] dados, int posClasse) {
		float[] minMaxClasse = new float[] { Float.MAX_VALUE, Float.MIN_VALUE };
		float[] minMaxAtrib = new float[] { Float.MAX_VALUE, Float.MIN_VALUE };
		float[] minMaxRef;
		float qtdAtrib = 0F;
		float qtdDados = 0F;
		boolean qtdAtribOK = true;
		
		for(float[] dado : dados) {
			for(int i = dado.length-1; i > -1; i--) {
				if(i == posClasse) {
					minMaxRef = minMaxClasse;
				} else {
					minMaxRef = minMaxAtrib;
				}
				if(dado[i] < minMaxRef[0]) {
					minMaxRef[0] = dado[i];
				}
				if(dado[i] > minMaxRef[1]) {
					minMaxRef[1] = dado[i];
				}
			}
			if(qtdAtribOK) {
				if(qtdAtrib == 0) {
					qtdAtrib = dado.length;
				}
				qtdAtribOK = dado.length == qtdAtrib;
				qtdAtrib = dado.length;
			}
			qtdDados++;
		}
		
		if(!qtdAtribOK) {
			qtdAtrib = -1F;
		}
		
		return new float[] { minMaxAtrib[0], minMaxAtrib[1], minMaxClasse[0], minMaxClasse[1], qtdAtrib, qtdDados };
	}

}
