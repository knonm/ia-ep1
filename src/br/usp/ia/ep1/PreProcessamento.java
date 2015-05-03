package br.usp.ia.ep1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import br.usp.ia.ep1.utils.ES;

/*
 * Depois pensar num nome pra classe
 * Essa classe eh responsavel pelo pre-processamento dos dados
 */
public class PreProcessamento {
	
	public static final String CHR_DELIMIT = ",";
	
	// Quantidade de atributos por instancia no conjunto de dados
	public static final int numeroAtributosPorInstancia = 65;
	
	// Menor valor que um atributo pode assumir no conjunto de dados
	private static final int valorMinimoAtributo = 0;
	
	// Maior valor que um atributo pode assumir no conjunto de dados
	private static final int valorMaximoAtributo = 16;
	
	// Menor valor que um atributo classe pode assumir no conjunto de dados
	public static final int valorMinimoClasse = 0;
	
	// Maior valor que um atributo classe pode assumir no conjunto de dados
	public static final int valorMaximoClasse = 9;
	
	public float[][] transformarArrayStringParaFloat(String[] dados, String chrDelimit) {
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
	
	/*
	 * Esse eh um dos requisitos desejaveis do pre-processamento
	 * Selecao de Atributos
	 * Usar arvore de decisao (?) - Entropia
	 * Attribute Subset Selection
	 * Bom link: http://www.public.asu.edu/~huanliu/papers/tkde05.pdf
	*/
	private float[][] SelecionarAtributos(float[][] dados) {
		return null;
	}
	
	/*
	 * Exclusao de atributos desnecessarios
	 * Precisa dar uma ajeitada nisso aqui
	 * Comeca a iterar partir da segunda dimensao da matriz, nao ficou mto bom
	 */
	private float[][] excluirAtrib(float[][] dados) {
		float[] qtdVlr;
		float limite = dados.length * 0.9F;
		boolean ehAtribOk;
		List<Integer> atribsOk = new ArrayList<Integer>();
		float[][] novosDados;
		Iterator<Integer> iterador;
		int indAtrib, atribSelect;
		
		atribsOk.add(PreProcessamento.numeroAtributosPorInstancia-1);
		qtdVlr = new float[PreProcessamento.valorMaximoAtributo + ((int)Math.pow(0, PreProcessamento.valorMinimoAtributo))];
		for(int j = PreProcessamento.numeroAtributosPorInstancia-2; j > -1; j--) {
			ehAtribOk = true;
			for(int k = qtdVlr.length-1; k > -1; k--) {
				qtdVlr[k] = 0F;
			}
			for(int i = dados.length-1; i > -1; i--) {
				qtdVlr[(int)dados[i][j]] += 1F;
				if(qtdVlr[(int)dados[i][j]] > limite) {
					i = -1;
					ehAtribOk = false;
				}
			}
			if(ehAtribOk) {
				atribsOk.add(j);
			}
		}

		novosDados = new float[dados.length][atribsOk.size()];
		
		iterador = atribsOk.iterator();
		indAtrib = atribsOk.size()-1;
		while(iterador.hasNext()) {
			atribSelect = iterador.next();
			for(int i = dados.length-1; i > -1; i--) {
				novosDados[i][indAtrib] = dados[i][atribSelect];
			}
			indAtrib--;
		}

		return novosDados;
	}
	
	// Normalizacao dos dados
	private void minMaxNormal(float[][] dados, float novoMin, float novoMax) {
		float difAnt = PreProcessamento.valorMaximoAtributo - PreProcessamento.valorMinimoAtributo;
		float difNovo = novoMax - novoMin;
		float difDif = difNovo / difAnt;
		float difAux = (PreProcessamento.valorMinimoAtributo * difNovo + difAnt * novoMin) / difAnt;
		
		for(int i = dados.length-1; i > -1; i--) {
			for(int j = dados[i].length-2; j > -1; j--) {
				dados[i][j] = dados[i][j] * difDif - difAux;
			}
		}
	}
	
	private void zScoreNormal(float[][] dados) {
		float media, desvioPadrao;
		for(int j = dados[0].length-2; j > -1; j--) {
			media = 0F;
			for(int i = dados.length-1; i > -1; i--) {
				media += Math.pow(dados[i][j], 2F);
			}
			media /= dados.length;
			desvioPadrao = 0F;
			for(int i = dados.length-1; i > -1; i--) {
				desvioPadrao += Math.pow(dados[i][j] - media, 2F);
			}
			desvioPadrao = (float) Math.sqrt(desvioPadrao / dados.length);
			for(int i = dados.length-1; i > -1; i--) {
				dados[i][j] = (dados[i][j] - media) / desvioPadrao;
			}
		}
	}
	
	// Preprocessamento dos dados obtidos apos a leitura do arquivo
	private float[][] processarDados(String[] dados) throws FileNotFoundException {
		float[][] dadosProc = transformarArrayStringParaFloat(dados, PreProcessamento.CHR_DELIMIT);
		dadosProc = excluirAtrib(dadosProc);
		//minMaxNormal(dadosProc, 0, 1);
		zScoreNormal(dadosProc);
		SelecionarAtributos(dadosProc); // soh pra tirar o warning
		return dadosProc;
	}

	private float[][][] splitDados(float[][] dados, float[] pcts) {
		float[][][] dadosSplit = new float[pcts.length][][];
		int[] splitLengths = new int[pcts.length];
		int totLength = 0;
		int from = 0;
		
		for(int i = pcts.length-1; i > -1; i--) {
			splitLengths[i] = Math.round(dados.length * Math.abs(pcts[i]));
			if((splitLengths[i]+totLength) > dados.length) {
				splitLengths[i] = dados.length - totLength;
				i = -1;
			} else {
				totLength += splitLengths[i];
			}
		}
		
		for(int i = 0; i < splitLengths.length; i++) {
			dadosSplit[i] = Arrays.copyOfRange(dados, from, from+splitLengths[i]);
			from = splitLengths[i];
		}
		
		return dadosSplit;
	}
	
	/*
	 * Ta demorando pra criar o arquivo. Tentei colocar tudo em uma 
	 * String soh e fazer io apenas uma vez mas tava demorando demais.
	 * Dai fiz um meio termo (soh faz io depois de iterar 10% das linhas da matriz).
	 * Se pensarem em uma maneira mais eficiente implementem ai!
	 * 
	 * Lembrete: o atributo classe esta sendo gravado no arquivo como float
	 */
	private void criarArquivo(float[][] dados, String arq) throws IOException {
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
	
	public float[] descDados(float[][] dados, int posClasse) {
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
	
	public PreProcessamento(String[] arqsDados, String[] arqsSaida, float[] pctsDadosSaida) throws IOException {
		if(arqsSaida.length == pctsDadosSaida.length) {
			float[][][] dadosSet = splitDados(processarDados(ES.lerArquivos(arqsDados)), pctsDadosSaida);

			for(int i = dadosSet.length-1; i > -1; i--) {
				criarArquivo(dadosSet[i], arqsSaida[i]);
			}
		}
	}
}