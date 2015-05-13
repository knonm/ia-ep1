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
	
	// Normalizacao dos dados
	private void minMaxNormal(float[][] dados, float novoMin, float novoMax) {
		float difAnt = PreProcessamento.valorMaximoAtributo - PreProcessamento.valorMinimoAtributo;
		float difNovo = novoMax - novoMin;
		
		for(int i = dados.length-1; i > -1; i--) {
			for(int j = dados[i].length-2; j > -1; j--) {
				dados[i][j] = (((dados[i][j] - PreProcessamento.valorMinimoAtributo) / difAnt) * difNovo) + novoMin;
			}
		}
		
	}
	
	private void zScoreNormal(float[][] dados){
		for(int i = 0; i < dados[0].length-1; i++){ // caminha todas as colunas menos a de classes
			double media = 0;
			for(int j = 0; j < dados.length; j++){ // pega todos os valores de "i" , por isso percorre e a coluna e não as variaveis
				media += dados[j][i];
			} media /= dados.length; // soma todos os valores reais e divide pelo total ( media )
						
			double variancia = 0;
			if(media != 0){
				for(int j = 0; j < dados.length; j++){ // pega todos os valores de "i" , por isso percorre e a coluna e não as variaveis
					variancia = variancia + (Math.pow((dados[j][i]-media), 2));
				} variancia = Math.sqrt(variancia / (dados.length-1)); // acha a variancia ( Somatoria[valor-media^2] / (total de dados - 1))
			}
			
			for(int j = 0; j < dados.length; j++){ // pega todos os valores de "i" , por isso percorre e a coluna e não as variaveis
				if(media != 0)	dados[j][i] = (float) ((dados[j][i] - media)/variancia); // zScore = (valor-media)/variancia
			}
		}		
	}
	
	// Preprocessamento dos dados obtidos apos a leitura do arquivo
	private float[][][] processarDados(String[] dados, float[] porcentagem) throws FileNotFoundException {
		float[][] dadosCrus = MN.transformarArrayStringParaFloat(dados, PreProcessamento.CHR_DELIMIT);
		dadosCrus = excluirAtrib(dadosCrus);
		
		zScoreNormal(dadosCrus);
		//minMaxNormal(dadosCrus, 0, 1);
		
		float[][][] dadosParticionados = particaoBalanceada(dadosCrus, porcentagem);
		        
		return dadosParticionados;
	}
	
	//Particao balanceada de arquivos
	private float[][][] particaoBalanceada(float[][] dados, float[] porcentagem){
		float[][][] dadosSeparados = new float[porcentagem.length][][]; // instancia a matriz tridimencional que vai amazenar os [arquivos][numero][atributos]
		
		int quantidadeTotal = dados.length;
		int[] quantidadeClasse = new int[10]; // array que vai armazenar quanto tem de cada classe no total
		
		for(int i = 0; i < quantidadeTotal; i++){ // acha quanto tem de cada classe e bota no array quantidadeClasse
			int aux = (int) dados[i][dados[i].length-1];
			quantidadeClasse[aux]++;
		}
                
		/* \/\/\/\/\/\/\/\/\/\/ NAO FUTUCA NISSO AQUI QUE VAI DA MERDA \/\/\/\/\/\/\/\/\/\/ */
		int [][] quantidadesFinais = new int [3][10]; // array que contem quanto pode ter de cada atributo em cada arquivo
		for (int i = 0; i < quantidadesFinais[0].length; i++){ // contas para determinar a quantidade de valores para cada arquivo
			quantidadesFinais[0][i] = (int) Math.round(quantidadeClasse[i]*porcentagem[0]);
			quantidadesFinais[1][i] = quantidadeClasse[i] - quantidadesFinais[0][i] - (int) Math.round(quantidadeClasse[i]*porcentagem[2]);
			quantidadesFinais[2][i] = quantidadeClasse[i] - quantidadesFinais[1][i] - quantidadesFinais[0][i];
		}
		/* /\/\/\/\/\/\/\/\/\/\ NAO FUTUCA NISSO AQUI QUE VAI DA MERDA /\/\/\/\/\/\/\/\/\/\ */
	
		// instanciando a matriz bidimensional de dados para cada arquivo
		dadosSeparados[0] = new float[(int) Math.floor(quantidadeTotal*porcentagem[0])][dados[0].length];
		dadosSeparados[1] = new float[(int) Math.floor(quantidadeTotal*porcentagem[1])][dados[0].length];
		dadosSeparados[2] = new float[(int) Math.floor(quantidadeTotal*porcentagem[2])][dados[0].length];
		
		int[] posicoes = new int[3]; // array auxiliar que armazena quanto cada arquivo ja tem dentro dele
		
		for (int i = 0; i < dados.length; i++){ // percorre todos os dados
		
			int aux = (int) dados[i][dados[i].length-1]; // valor da classe
     
			for (int j = 0; j < dadosSeparados.length; j++){
				if(quantidadesFinais[j][aux] != 0 && posicoes[j] < Math.floor(quantidadeTotal*porcentagem[j])){ // ve se ja foram todos que devem ser no arquivo de treino (arredondado pro chao [?]) so deixa q ta funcionando
					for(int aux2 = 0; aux2 < dados[i].length; aux2++)	dadosSeparados[j][posicoes[j]][aux2] = dados[i][aux2]; // copia todos os valores pra matriz final
					posicoes[j]++; // acrecenta o contador para saber onde inserir o proximo
					quantidadesFinais[j][aux]--; // diminui o contador pois acrecentamos um
					break; // se teve sucesso quebra o laco pra procurar espaco valido
				}
			}	
		}
	
		return dadosSeparados;
	}

	// Separa arquivos sem balancear
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

		//System.out.println(dadosArq[0]);
		//System.out.println(dadosArq[dadosArq.length-1]);
		
		
		ES.escreverDados(arq, dadosArq);
	}
	
	public PreProcessamento(String[] arqsDados, String[] arqsSaida, float[] pctsDadosSaida) throws IOException {
		if(arqsSaida.length == pctsDadosSaida.length) {
			
			//System.out.println(pctsDadosSaida[0]);
			//System.out.println(pctsDadosSaida[1]);
			//System.out.println(pctsDadosSaida[2]);
			//System.out.println(pctsDadosSaida.length);
			
			float[][][] dadosSet = processarDados(ES.lerArquivos(arqsDados), pctsDadosSaida);
			//float[][][] dadosSet = splitDados(processarDados(ES.lerArquivos(arqsDados)), pctsDadosSaida);

			this.dados = dadosSet;
			
			//for(int i = dadosSet.length-1; i > -1; i--) {
			//	criarArquivo(dadosSet[i], arqsSaida[i]);
			//}
			
			
		}
	}
}
