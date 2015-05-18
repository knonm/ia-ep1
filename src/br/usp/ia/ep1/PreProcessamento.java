package br.usp.ia.ep1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import br.usp.ia.ep1.utils.*;

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
	
	public float[][][] dados;
	
	/* Esclui o atributo se 90% dos valores dele forem iguais  */
	private float[][] excluirAtrib(float[][] dados) {
		float[] qtdVlr;
		float limite = dados.length * 0.9F; // Determina o limite
		boolean ehAtribOk;
		List<Integer> atribsOk = new ArrayList<Integer>();
		float[][] novosDados;
		Iterator<Integer> iterador;
		int indAtrib, atribSelect;
		
		atribsOk.add(PreProcessamento.numeroAtributosPorInstancia-1);
		qtdVlr = new float[PreProcessamento.valorMaximoAtributo + ((int)Math.pow(0, PreProcessamento.valorMinimoAtributo))];
		
		for(int j = PreProcessamento.numeroAtributosPorInstancia-2; j > -1; j--) 
		{
			ehAtribOk = true;
			for(int k = qtdVlr.length-1; k > -1; k--) 
				qtdVlr[k] = 0F;

			for(int i = dados.length-1; i > -1; i--) 
			{
				qtdVlr[(int)dados[i][j]] += 1F;
				if(qtdVlr[(int)dados[i][j]] > limite) 
				{
					i = -1;
					ehAtribOk = false;
				}
			}
			if(ehAtribOk)
				atribsOk.add(j);
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
	
	/* NORMALIZAÇÃO */
	
	/* Estratégia MinMax, onde os dados ficam determinado entre os valores passados como parametro, iterando
	por colunas, mantendo a proporção para o arquivo todo */
	private void minMaxNormal(float[][] dados, float novoMin, float novoMax) {
		float difNovo = novoMax - novoMin;
		float maxColuna = 0;
		float minColuna = PreProcessamento.valorMaximoAtributo;
		
		for(int i = 0; i < dados[0].length-1; i++){
			for(int j = 0; j < dados.length; j++){ // Encontra o máximo e mínimo.
				if(dados[j][i] > maxColuna) maxColuna = dados[j][i];
				if(dados[j][i] < minColuna) minColuna = dados[j][i];
			}
			
			for(int j = 0; j < dados.length; j++){
				dados[j][i] = (((dados[j][i] - minColuna) / (maxColuna - minColuna)) * difNovo) + novoMin; // Formula do MinMax.
			}
			
			// Reseta os valores de mínimo e máximo das colunas antes de ir para a próxima coluna.
			maxColuna = 0;
			minColuna = PreProcessamento.valorMaximoAtributo;
			
		}
	}
	
	/* Estratédia de zScore, iterando por colunas */
	private void zScoreNormal(float[][] dados){
		for(int i = 0; i < dados[0].length-1; i++){ // Caminha todas as colunas menos a de classes.
			double media = 0;
			for(int j = 0; j < dados.length; j++){ // Pega todos os valores de "i" , por isso percorre e a coluna e não as variaveis.
				media += dados[j][i];
			} media /= dados.length; // Soma todos os valores reais e divide pelo total (média).
						
			double variancia = 0;
			if(media != 0){
				for(int j = 0; j < dados.length; j++){ // Pega todos os valores de "i" , por isso percorre e a coluna e não as variaveis.
					variancia = variancia + ((dados[j][i]-media) * (dados[j][i]-media));
				} variancia = Math.sqrt(variancia / (dados.length-1)); // Acha a variancia (Somatoria[valor-media^2] / (total de dados - 1)).
			}
			
			for(int j = 0; j < dados.length; j++){ // Pega todos os valores de "i" , por isso percorre e a coluna e não as variaveis.
				if(media != 0)	dados[j][i] = (float) ((dados[j][i] - media)/variancia); // zScore = (valor-media)/variancia.
			}
		}		
	}
	
	/* Método que Pré-Processa os dados, chamando os algoritimos auxiliares */
	private float[][][] processarDados(String[] dados, float[] porcentagem) throws FileNotFoundException {
		float[][] dadosCrus = MN.transformarArrayStringParaFloat(dados, PreProcessamento.CHR_DELIMIT);
		dadosCrus = excluirAtrib(dadosCrus); // Exclui os atributos sem importancia.
		
		//zScoreNormal(dadosCrus); // Faz o zScore.
		minMaxNormal(dadosCrus, -1, 1); // Faz o MinMax.
		
		float[][][] dadosParticionados = particaoBalanceada(dadosCrus, porcentagem); // Particiona os dados nos conjuntos, seguindo a determinada porcentagem.
		        
		return dadosParticionados;
	}
	
	/* Particiona os dados seguindo a determinada porcentagem */
	private float[][][] particaoBalanceada(float[][] dados, float[] porcentagem){
		float[][][] dadosSeparados = new float[porcentagem.length][][]; // instancia a matriz tridimencional que vai amazenar os [arquivos][numero][atributos]
		
		int quantidadeTotal = dados.length;
		int[] quantidadeClasse = new int[10]; // array que vai armazenar quanto tem de cada classe no total
		
		for(int i = 0; i < quantidadeTotal; i++){ // acha quanto tem de cada classe e bota no array quantidadeClasse
			int aux = (int) dados[i][dados[i].length-1];
			quantidadeClasse[aux]++;
		}
                
		/* \/\/\/\/\/\/\/\/\/\/ CALCULOS IMPORTANTES \/\/\/\/\/\/\/\/\/\/ */
		int [][] quantidadesFinais = new int [3][10]; // Array que contem quanto pode ter de cada atributo em cada arquivo.
		for (int i = 0; i < quantidadesFinais[0].length; i++){ // Contas para determinar a quantidade de valores para cada arquivo.
			quantidadesFinais[0][i] = (int) Math.round(quantidadeClasse[i]*porcentagem[0]);
			quantidadesFinais[1][i] = quantidadeClasse[i] - quantidadesFinais[0][i] - (int) Math.round(quantidadeClasse[i]*porcentagem[2]);
			quantidadesFinais[2][i] = quantidadeClasse[i] - quantidadesFinais[1][i] - quantidadesFinais[0][i];
		}
		/* /\/\/\/\/\/\/\/\/\/\ CALCULOS IMPORTANTES /\/\/\/\/\/\/\/\/\/\ */
	
		/* Instanciando a matriz bidimensional de dados para cada arquivo */ 
		dadosSeparados[0] = new float[(int) Math.floor(quantidadeTotal*porcentagem[0])][dados[0].length];
		dadosSeparados[1] = new float[(int) Math.floor(quantidadeTotal*porcentagem[1])][dados[0].length];
		dadosSeparados[2] = new float[(int) Math.floor(quantidadeTotal*porcentagem[2])][dados[0].length];
		
		int[] posicoes = new int[3]; // Array auxiliar que armazena quanto cada arquivo ja tem dentro dele.
		
		for (int i = 0; i < dados.length; i++){ // Percorre todos os dados.
		
			int aux = (int) dados[i][dados[i].length-1]; // Valor da classe.
     
			for (int j = 0; j < dadosSeparados.length; j++){
				if(quantidadesFinais[j][aux] != 0 && posicoes[j] < Math.floor(quantidadeTotal*porcentagem[j])){ // Ve se ja foram todos que devem ser no arquivo (arredondado pro chão).
					for(int aux2 = 0; aux2 < dados[i].length; aux2++)	dadosSeparados[j][posicoes[j]][aux2] = dados[i][aux2]; // Copia todos os valores para matriz final.
					posicoes[j]++; // Acrescenta o contador para saber onde inserir o próximo.
					quantidadesFinais[j][aux]--; // Diminui o contador pois acrecentamos um.
					break; // Se teve sucesso quebra o laco pra procurar um espaco válido.
				}
			}	
		}
	
		return dadosSeparados;
	}

	/* Separa arquivos sem balancear */
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
	
	/* Inicia o Pré-Processamento */
	public PreProcessamento(String[] arqsDados, String[] arqsSaida, float[] pctsDadosSaida) throws IOException {
		if(arqsSaida.length == pctsDadosSaida.length) {
			System.out.println("Comecando Pre Processamento...");
			System.out.println(pctsDadosSaida[0]);
			System.out.println(pctsDadosSaida[1]);
			System.out.println(pctsDadosSaida[2]);
			System.out.println(pctsDadosSaida.length);
			
			System.out.println("Normalizando e Separando os conjuntos...");
			
			float[][][] dadosSet = processarDados(ES.lerArquivos(arqsDados), pctsDadosSaida); // Normaliza e separa os dados.
			
			System.out.println("Conjuntos normalizados e separados.");
			
			this.dados = dadosSet;
			
			System.out.println("Escrevendo arquivos...");
			for(int i = dadosSet.length-1; i > -1; i--) {
				MN.criarArquivo(dadosSet[i], arqsSaida[i]);
				System.out.println("Aquivo " + i + " concluido.");
			}
			System.out.println("Arquivos escritos.");
			
		}
	}
}
