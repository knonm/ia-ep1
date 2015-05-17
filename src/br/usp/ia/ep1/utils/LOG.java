package br.usp.ia.ep1.utils;

import java.io.IOException;

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
	
	public void completaLogMLP(int qtdAcertos, int qtdEpocas, int qtdNeuroniosCamadaOculta, MatrizConfusao matrizC, DadosDeTeste[] dados){
		this.qtdAcertos = qtdAcertos;
		this.qtdEpocas = qtdEpocas;
		this.qtdNeuroniosCamadaOculta = qtdNeuroniosCamadaOculta;
		this.matrizC = matrizC;
		this.dadosDeTesteMLP = dados;		
	}
	
	public void escreveLOG() throws IOException{
		String caminho = ("./out/"+this.tipo+"-"
				+String.valueOf(this.qtdNeuronios)+"-"
				+String.valueOf(this.txAprendInicial)+"-"
				+String.valueOf(this.versao)+"-"
				+String.valueOf(this.qtdEpocasValida)+"-"
				+String.valueOf(this.qtdEpocasParada)+"-"
				+String.valueOf(this.qtdAcertos)+".out");
		
		String caminhoPesosFinal = ("./out/"+this.tipo+"-"
				+String.valueOf(this.qtdNeuronios)+"-"
				+String.valueOf(this.qtdAcertos)+"-"
				+String.valueOf(this.qtdEpocas)+".out");
		
		String caminhoPesosEscondidaFinal = ("./out/"+this.tipo+" (Oculta) "+
				String.valueOf(this.qtdNeuroniosCamadaOculta)+" "+
				String.valueOf(this.qtdAcertos)+" "+
				String.valueOf(this.qtdEpocas)+".out");
		
		String caminhoRespostasTeste = ("./out/"+this.tipo+" (Respostas Teste) "+
				String.valueOf(this.qtdNeuronios)+" " +
				String.valueOf(this.qtdNeuroniosCamadaOculta)+"-"+
				String.valueOf(this.qtdAcertos)+" "+
				String.valueOf(this.qtdEpocas)+".out");
		
		String[] aux = new String[11];
		aux[0] = "Tipo: " + this.tipo + "\n";
		aux[1] = "Acuracia: " + String.valueOf(this.qtdAcertos) + "\n";
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
		float[][] matrizAux = new float[this.matrizC.getMatrizConfusao().length][this.matrizC.getMatrizConfusao()[0].length];
		for(int j = this.matrizC.getMatrizConfusao().length-1; j > -1; j--) {
			for(int i = this.matrizC.getMatrizConfusao()[0].length-1; i > -1; i--) {
				matrizAux[j][i] = (float) this.matrizC.getMatrizConfusao()[j][i];
			}
		}
		
		
		ES.escreverDados(caminho, aux);
		
		MN.criarArquivo(matrizAux.clone(), "./out/MC "+this.qtdAcertos+".out");
		
		if(this.tipo == "LVQ"){
			MN.criarArquivo((MN.transformaBidimensional(this.pesos, 0)), caminhoPesosFinal);
		}else{
			//MN.criarArquivo((MN.transformaBidimensional(this.pesos, 0)), caminhoPesosEscondidaFinal);
			//MN.criarArquivo((MN.transformaBidimensional(this.pesos, 1)), caminhoPesosFinal);
			MN.EscreverRespostaTesteMLP(dadosDeTesteMLP, caminhoRespostasTeste);
		}
	}
}
