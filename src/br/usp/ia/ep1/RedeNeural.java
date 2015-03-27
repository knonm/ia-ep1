package br.usp.ia.ep1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Depois pensar num nome pra classe
 * Essa classe eh responsavel pelo pre-processamento dos dados
 */
public class RedeNeural {
	
	// Quantidade de atributos por instancia no conjunto de dados
	public static final int NUM_ATRIBS = 65;
	
	// Posicao no vetor de atributos em que o atributo de classe esta localizado
	//private static final int POS_ATRIB_CLASSE = 64;
	
	// Menor valor que um atributo pode assumir no conjunto de dados
	private static final int MIN_VLR_ATRIB = 0;
	
	// Maior valor que um atributo pode assumir no conjunto de dados
	private static final int MAX_VLR_ATRIB = 16;
	
	// Menor valor que um atributo classe pode assumir no conjunto de dados
	public static final int MIN_VLR_CLASSE = 0;
	
	// Maior valor que um atributo classe pode assumir no conjunto de dados
	public static final int MAX_VLR_CLASSE = 9;
	
	/*
	 * Esse eh um dos requisitos desejaveis do pre-processamento
	 * Selecao de Atributos
	 * Usar arvore de decisao (?) - Entropia
	 * Attribute Subset Selection
	 * Bom link: http://www.public.asu.edu/~huanliu/papers/tkde05.pdf
	*/
	private float[][] attribSelect(float[][] dados) {
		return null;
	}
	
	// Leitura do arquivo de dados
	private String[] lerArquivo(String arquivo) throws FileNotFoundException {
		List<String> dados = new ArrayList<String>();
		Scanner s = new Scanner(new File(arquivo));
		while(s.hasNext()) {
			dados.add(s.nextLine());
		}
		s.close();
		return dados.toArray(new String[0]);
	}
	
	/*
	 * Exclusao de atributos desnecessarios
	 * Precisa dar uma ajeitada nisso aqui
	 * Comeca a iterar partir da segunda dimensao da matriz, nao ficou mto bom
	 */
	private float[][] excluirAtrib(float[][] dados) {
		Map<Float, Integer> qtdVlr;
		float limite = dados.length * 0.9F;
		boolean ehAtribOk;
		List<Integer> atribsOk = new ArrayList<Integer>();
		float[][] novosDados;
		
		for(int j = RedeNeural.NUM_ATRIBS-2; j > -1; j--) {
			ehAtribOk = true;
			qtdVlr = new HashMap<Float, Integer>();
			for(int i = dados.length-1; i > -1; i--) {
				qtdVlr.put(dados[i][j], (qtdVlr.get(dados[i][j]) == null ? 1 : qtdVlr.get(dados[i][j]) + 1));
				if(qtdVlr.get(dados[i][j]) > limite) {
					i = -1;
					ehAtribOk = false;
				}
			}
			if(ehAtribOk) {
				atribsOk.add(j);
			}
		}
		
		novosDados = new float[dados.length][atribsOk.size()];
		Iterator<Integer> it = atribsOk.iterator();
		int indAtrib = 0;
		int atribSelect;
		while(it.hasNext()) {
			atribSelect = it.next();
			for(int i = dados.length-1; i > -1; i--) {
				novosDados[i][indAtrib] = dados[i][atribSelect];
			}
			indAtrib++;
		}

		return novosDados;
	}
	
	// Normalizacao dos dados
	private float[][] minMaxNormal(float[][] dados, float novoMin, float novoMax) {
		float difAnt = RedeNeural.MAX_VLR_ATRIB - RedeNeural.MIN_VLR_ATRIB;
		float difNovo = novoMax - novoMin;
		float difDif = difNovo / difAnt;
		float difAux = (RedeNeural.MIN_VLR_ATRIB * difNovo + difAnt * novoMin) / difAnt;
		
		for(int i = dados.length-1; i > -1; i--) {
			for(int j = dados[i].length-2; j > -1; j--) {
				dados[i][j] = dados[i][j] * difDif - difAux;
			}
		}
		
		return dados;
	}
	
	// Preprocessamento dos dados obtidos apos a leitura do arquivo
	private float[][] processarDados(String[] dados) throws FileNotFoundException {
		float[][] dadosProc = new float[dados.length][RedeNeural.NUM_ATRIBS];
		int i, j;
		i = 0;
		for(String linha : dados) {
			j = 0;
			for(String atrib : linha.split(",")) {
				dadosProc[i][j++] = Integer.valueOf(atrib);
			}
			i++;
		}
		dadosProc = excluirAtrib(dadosProc);
		minMaxNormal(dadosProc, 0, 1);
		attribSelect(dadosProc); // soh pra tirar o warning
		return dadosProc;
	}
	
	/*
	 * Ta demorando pra criar o arquivo. Tentei colocar tudo em uma 
	 * String soh e fazer io apenas uma vez mas tava demorando demais.
	 * Dai fiz um meio termo (soh faz io depois de iterar 10% das linhas da matriz).
	 * Se pensarem em uma maneira mais eficiente implementem ai!
	 * 
	 * Lembrete: o atributo classe esta sendo gravado no arquivo como float
	 */
	private void criarArquivo(float[][] dados) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./res/preproc_optdigits.tra")));
		String dadosArq = new String();
		
		for(int i = dados.length-1; i > -1; i--) {
			for(int j = dados[i].length-2; j > -1; j--) {
				dadosArq += String.valueOf(dados[i][j]) + ",";
			}
			dadosArq += String.valueOf(dados[i][dados[i].length-1]) + "\n";
			if(i % (Math.round((dados.length-1)*0.1)) == 0) {
				bw.write(dadosArq);
				dadosArq = new String();
			}
		}
		
		bw.close();
	}
	
	public RedeNeural() throws IOException {
		float[][] dados = processarDados(lerArquivo("./res/optdigits.tra"));
		criarArquivo(dados);
	}
}