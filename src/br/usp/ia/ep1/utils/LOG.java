package br.usp.ia.ep1.utils;

import java.io.IOException;

import br.usp.ia.ep1.Main;
import br.usp.ia.ep1.MatrizConfusao;
import br.usp.ia.ep1.MLP.DadosDeTeste;

public class LOG {
	private int versao;
	private String tipo;
	private float[][][] pesos;
	private int qtdAcertos;
	private int qtdEpocas;
	private int qtdEpocasParada;
	private int qtdEpocasValida;
	private int qtdNeuronios;
	private int qtdNeuroniosCamadaOculta;
	private float txAprendInicial;
	private float txAprendFinal;
	private MatrizConfusao matrizC;
	private DadosDeTeste[] dadosDeTesteMLP;
	
	private static final int LVQ = 0;
	private static final int MLP = 1;
	
	public LOG(int versao, int qtdEpocasParada, int qtdEpocasValida, int qtdNeuronios, float txAprendInicial, int TIPO){
		this.qtdEpocasParada = qtdEpocasParada;
		this.qtdEpocasValida = qtdEpocasValida;
		this.qtdNeuronios = qtdNeuronios;
		this.txAprendInicial = txAprendInicial;
		if(TIPO == LVQ){
			this.tipo = "LVQ";
		} else {
			this.tipo = "MLP";
		}
	}
	
	public void completaLog(float[][][] pesos, int qtdAcertos, int qtdEpocas, int qtdNeuroniosCamadaOculta, float txAprendFinal, MatrizConfusao matrizC){
		this.pesos = pesos;
		this.qtdAcertos = qtdAcertos;
		this.qtdEpocas = qtdEpocas;
		this.txAprendFinal = txAprendFinal;
		this.matrizC = matrizC;
		if(this.tipo == "MLP"){
			this.qtdNeuroniosCamadaOculta = qtdNeuroniosCamadaOculta;
		}
	}
	
	public void completaLogMLP(int qtdAcertos, int qtdEpocas, int qtdNeuroniosCamadaOculta, MatrizConfusao matrizC, DadosDeTeste[] dados, double txAprendFinal){
		this.qtdAcertos = qtdAcertos;
		this.qtdEpocas = qtdEpocas;
		this.qtdNeuroniosCamadaOculta = qtdNeuroniosCamadaOculta;
		this.matrizC = matrizC;
		this.dadosDeTesteMLP = dados;
		this.txAprendFinal = (float)txAprendFinal;
	}
	
	public void escreveLOG() throws IOException{
		// Caminho para os dados principais da rede resultante.
		String caminho = (Main.DIR_OUTPUT+this.tipo+"-"
				+String.valueOf(this.qtdNeuronios)+"-"
				+String.valueOf(this.txAprendInicial)+"-"
				+String.valueOf(this.versao)+"-"
				+String.valueOf(this.qtdEpocasValida)+"-"
				+String.valueOf(this.qtdEpocasParada)+"-"
				+String.valueOf(this.qtdAcertos)+".out");
		
		// Caminho para os pesos finais.
		String caminhoPesosFinal = (Main.DIR_OUTPUT+this.tipo+"-"
				+String.valueOf(this.qtdNeuronios)+"-"
				+String.valueOf(this.qtdAcertos)+"-"
				+String.valueOf(this.qtdEpocas)+".out");
		
		// Caminho para os pesos da camada escondida.
		String caminhoPesosEscondidaFinal = (Main.DIR_OUTPUT+this.tipo+" (Oculta) "+
				String.valueOf(this.qtdNeuroniosCamadaOculta)+" "+
				String.valueOf(this.qtdAcertos)+" "+
				String.valueOf(this.qtdEpocas)+".out");
		
		// Caminho com respostas para incrementar o relatório.
		String caminhoRespostasTeste = (Main.DIR_OUTPUT+this.tipo+" (Respostas Teste) "+
				String.valueOf(this.qtdNeuronios)+" " +
				String.valueOf(this.qtdNeuroniosCamadaOculta)+"-"+
				String.valueOf(this.qtdAcertos)+" "+
				String.valueOf(this.qtdEpocas)+".out");
		
		String[] aux = new String[13];
		aux[0] = "Tipo: " + this.tipo + "\n";
		aux[1] = "Acuracia: " + String.valueOf(this.matrizC.getAcuracia()) + "\n";
		aux[2] = "Erro: " + String.valueOf(this.matrizC.getErro()) + "\n";
		aux[3] = "Quantidade de Epocas: " + String.valueOf(this.qtdEpocas) + "\n";
		if(this.tipo == "LVQ"){
			aux[4] = "Quantidade de neuronios por classe: " + String.valueOf(this.qtdNeuronios) + "\n";
			aux[5] = "LVQ nao possui camada oculta" + "\n";
			aux[6] = "Taxa de aprendizado inicial: " + String.valueOf(this.txAprendInicial) + " | Taxa de aprendizado final: " + String.valueOf(this.txAprendFinal) + "\n";
		}else{
			aux[4] = "Quantidade de neuronios: " + String.valueOf(this.qtdNeuronios) + "\n";
			aux[5] = "Quantidade de neuronios na camada oculta: " + String.valueOf(this.qtdNeuroniosCamadaOculta) + "\n";
			aux[6] = "Taxa de aprendizado: " + String.valueOf(this.txAprendInicial) + "\n";
		}		
		aux[7] = "Caminho: " + caminho + "\n";
		aux[8] = "\n";
		aux[9] = "	9	8	7	6	5	4	3	2	1	0\n";
		aux[10] = "";

		for(int j = this.matrizC.getMatrizConfusao().length-1; j > -1; j--) {
			aux[10] += j + "	";
			for(int i = this.matrizC.getMatrizConfusao()[0].length-1; i > -1; i--) {
				aux[10] += String.valueOf((this.matrizC.getMatrizConfusao()[j][i]))+ "	";
			}
			aux[10] += "\n";
			
		}

		aux[11] = "\n\n";
		for(int i = this.matrizC.getMatrizConfusao().length-1; i > -1; i--) {
			aux[11] += "Medidas de avaliação da classe " + i + "\n";
			aux[11] += "Taxa de verdadeiros positivos e negativos: " + String.valueOf(this.matrizC.getTaxaVerdadeiros(i)) + "\n";
			aux[11] += "Taxa de falsos positivos: " + String.valueOf(this.matrizC.getTaxaFalsos(i)) + "\n";
			aux[11] += "Precisão: " + String.valueOf(this.matrizC.getPrecisao(i)) + "\n";
			aux[11] += "Taxa de falsas descobertas: " + String.valueOf(this.matrizC.getTaxaFalsasDescobertas(i)) + "\n";
			aux[11] += "\n\n";
		}
		
		aux[12] += "Média do número de instâncias classificadas corretamente: " + this.matrizC.getMedia() + "\n";
		aux[12] += "Desvio padrão do número de instâncias classificadas corretamente: " + this.matrizC.getDesvioPadrao() + "\n";
		
		ES.escreverDados(caminho, aux);
	
		if(this.tipo == "LVQ"){
			MN.criarArquivo((MN.transformaBidimensional(this.pesos, 0)), caminhoPesosFinal);
		}else{
			MN.EscreverRespostaTesteMLP(dadosDeTesteMLP, caminhoRespostasTeste);
		}
	}
	
	/* Método que escreve conteúdos auxiliares para o Relatório */
	public void escreveLogAuxRelatorio(boolean iniAleatoria) throws IOException {
		String caminho = (Main.DIR_OUTPUT+this.tipo+"-TesteDeParametros.out");
		String aux;
		if(tipo == "LVQ"){
			aux = iniAleatoria + "," + this.txAprendInicial + "," + this.txAprendFinal + "," + this.qtdEpocas + "," + this.matrizC.getAcuracia() + "," + this.matrizC.getErro();
		} else {
			aux = iniAleatoria + "," + this.txAprendInicial + "," + this.txAprendFinal + "," + this.qtdNeuroniosCamadaOculta + "," + this.qtdEpocas + "," + this.matrizC.getAcuracia() + "," + this.matrizC.getErro();
		}
		
		ES.escreverDadoAppend(caminho, aux);
	}
}
