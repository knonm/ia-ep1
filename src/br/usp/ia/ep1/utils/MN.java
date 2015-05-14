package br.usp.ia.ep1.utils;

import java.util.HashSet;
import java.util.Set;

import br.usp.ia.ep1.DescDados;

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
	
	public static void criarArquivo(float[][] dados, String arq) throws IOException {
		String dado = new String();
		int limite = Math.round(dados.length * 0.1F);
		String[] dadosArq = new String[((int)Math.floor(dados.length/limite)) + 1];
		int cnt = 0;
		
		for(int i = dados.length-1; i > -1; i--) {
			for(int j = 0; j < dados[i].length-1; j++) {
				dado += String.valueOf(dados[i][j]) + PreProcessamento.CHR_DELIMIT;
			}
			dado += String.valueOf(dados[i][dados[i].length-1]) + "\n";
			if((i+1)%limite == 0) {
				dadosArq[cnt++] = dado;
				dado = new String();
			}
		}
		
		if(!dado.isEmpty()) {
			dadosArq[cnt] = dado;
		}

		ES.escreverDados(arq, dadosArq);
	}
	
	public static DescDados descDados(float[][] dados, int posClasse) {
		DescDados desc = new DescDados();
		
		float[] minMaxVlrClasses = new float[] { Float.MAX_VALUE, Float.MIN_VALUE };
		float[] minMaxVlrAtribs = new float[] { Float.MAX_VALUE, Float.MIN_VALUE };
		float[] minMaxRef;
		int qtdDados = 0;
		int qtdAtribs = 0;
		boolean qtdAtribOK = true;
		
		Set<Float> qtdVlrAtribs = new HashSet<Float>();
		Set<Float> qtdVlrClasses = new HashSet<Float>();
		
		for(float[] dado : dados) {
			for(int i = dado.length-1; i > -1; i--) {
				if(i == posClasse) {
					minMaxRef = minMaxVlrClasses;
					qtdVlrClasses.add(dado[i]);
				} else {
					minMaxRef = minMaxVlrAtribs;
				}
				if(dado[i] < minMaxRef[0]) {
					minMaxRef[0] = dado[i];
				}
				if(dado[i] > minMaxRef[1]) {
					minMaxRef[1] = dado[i];
				}
			}
			if(qtdAtribOK) {
				if(qtdAtribs == 0) {
					qtdAtribs = dado.length;
				}
				qtdAtribOK = dado.length == qtdAtribs;
				qtdAtribs = dado.length;
			}
			qtdDados++;
		}
		
		if(!qtdAtribOK) {
			qtdAtribs = -1;
		}
		
		desc.setMinVlrAtrib(minMaxVlrAtribs[0]);
		desc.setMaxVlrAtrib(minMaxVlrAtribs[1]);
		desc.setMinVlrClasse(minMaxVlrClasses[0]);
		desc.setMaxVlrClasse(minMaxVlrClasses[1]);
		desc.setQtdVlrAtribs(qtdVlrAtribs.size());
		desc.setQtdVlrClasses(qtdVlrClasses.size());
		desc.setQtdAtribs(qtdAtribs);
		desc.setQtdDados(qtdDados);
		
		return desc;
	}

}
