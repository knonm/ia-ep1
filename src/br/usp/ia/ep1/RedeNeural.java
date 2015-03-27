package br.usp.ia.ep1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RedeNeural {
	
	private static final int NUM_ATRIBUTOS = 65;
	private static final float MIN_ATRIB = 0;
	private static final float MAX_ATRIB = 16;
	
	// Um dos tipos de normalizacao que existem no livro de data mining.
	// Acho melhor fazer com outra normalizacao, essa aqui vai ficar ruim com os valores que tem no arquivo.
	private float[][] decimalScalingNormal(float[][] dados) {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		float decimalAux, decimal;
		
		for(float[] instancia : dados) {
			for(int j = instancia.length-2; j > -1; j--) {
				min = instancia[j] < min ? instancia[j] : min;
				max = instancia[j] > max ? instancia[j] : max;
			}
		}
		
		min = Math.abs(min);
		max = Math.abs(max);
		
		decimal = 1;
		decimalAux = min < max ? max : min;
		while(decimalAux > 1F) {
			decimal *= 10;
			decimalAux /= 10;
		}
		
		for(int i = dados.length-1; i > -1; i--) {
			// dados[i].length-2 faz ignorar o atributo de classe, que eh o da ultima posicao.
			for(int j = dados[i].length-2; j > -1; j--) {
				dados[i][j] /= decimal;
			}
		}
		
		return dados;
	}
	
	private float[][] minMaxNormal(float[][] dados, float novoMin, float novoMax) {
		float difAnt = RedeNeural.MAX_ATRIB - RedeNeural.MIN_ATRIB;
		float difNovo = novoMax - novoMin;
		float difDif = difNovo / difAnt;
		float difAux = (RedeNeural.MIN_ATRIB * difNovo + difAnt * novoMin) / difAnt;
		
		for(int i = dados.length-1; i > -1; i--) {
			for(int j = dados[i].length-2; j > -1; j--) {
				dados[i][j] = dados[i][j] * difDif - difAux;
			}
		}
		
		return dados;
	}
	
	/*
	  Selecao de Atributos
	  Usar arvore de decisao
	  Attribute Subset Selection
	  Bom link: http://www.public.asu.edu/~huanliu/papers/tkde05.pdf
	*/
	private float[][] attribSelect(float[][] dados) {
		//float[][] contador = new float[9][Math.rou];
		return null;
	}
	
	private float[][] excluiAtrib(float[][] dados) {
		Map<Float, Integer> qtdVlr = new HashMap<Float, Integer>();
		int numReg = dados.length;
		
		for(int i = dados.length-1; i > -1; i--) {
			for(int j = dados[i].length-2; i > -1; i--) {
				qtdVlr.put(dados[i][j], (qtdVlr.get(dados[i][j]) == null ? 1 : qtdVlr.get(dados[i][j]) + 1));
			}
		}
		
		return dados;
	}
	
	// Quantas linhas tem no arquivo. Assim nao precisa usar List.
	private String[] lerArquivo(String arquivo) throws FileNotFoundException {
		List<String> dados = new ArrayList<String>();
		Scanner s = new Scanner(new File(arquivo));
		while(s.hasNext()) {
			dados.add(s.nextLine());
		}
		s.close();
		return dados.toArray(new String[0]);
	}
	
	// Pega o conteudo do arquivo e preenche uma matriz com ele.
	private float[][] processarDados(String[] dados) throws FileNotFoundException {
		float[][] dadosProc = new float[dados.length][NUM_ATRIBUTOS];
		int i, j;
		i = 0;
		for(String linha : dados) {
			j = 0;
			for(String atrib : linha.split(",")) {
				dadosProc[i][j++] = Integer.valueOf(atrib);
			}
			i++;
		}
		return dadosProc;
	}
	
	public RedeNeural() throws FileNotFoundException {
		float[][] dados = processarDados(lerArquivo("./res/optdigits.tra"));
		for(float fl : dados[2]) {
			System.out.println(fl);
		}
		System.out.println();
		minMaxNormal(dados, 0, 1);
		for(float fl : dados[2]) {
			System.out.println(fl);
		}
	}
}