package br.usp.ia.ep1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RedeNeural {
	
	private static final int NUM_ATRIBUTOS = 65;
	
	// Um dos tipos de normalizacao que existem no livro de data mining.
	// Acho melhor fazer com outra normalizacao, essa aqui vai ficar ruim com os valores que tem no arquivo.
	private float[][] decimalScaling(float[][] dados) {
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
		decimalScaling(dados);
		for(float fl : dados[2]) {
			System.out.println(fl);
		}
	}
}