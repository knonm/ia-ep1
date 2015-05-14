package br.usp.ia.ep1.utils;

import java.io.IOException;

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
	
	public void completaLog(float[][][] pesos, int qtdAcertos, int qtdEpocas, int qtdNeuroniosCamadaOculta, float txAprendFinal){
		this.pesos = pesos;
		this.qtdAcertos = qtdAcertos;
		this.qtdEpocas = qtdEpocas;
		this.txAprendFinal = txAprendFinal;
		if(this.tipo == "MLP"){
			this.qtdNeuroniosCamadaOculta = qtdNeuroniosCamadaOculta;
		}
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
		
		String[] aux = new String[7];
		aux[0] = "Tipo: " + this.tipo;
		aux[1] = "Quantidade de Acertos: " + String.valueOf(this.qtdAcertos);
		aux[2] = "Quantidade de Epocas: " + String.valueOf(this.qtdEpocas);
		if(this.tipo == "LVQ"){
			aux[3] = "Quantidade de neuronios por classe: " + String.valueOf(this.qtdNeuronios);
			aux[4] = "LVQ nao possui camada oculta";
		}else{
			aux[3] = "Quantidade de neuronios: " + String.valueOf(this.qtdNeuronios);
			aux[4] = "Quantidade de neuronios na camada oculta: " + String.valueOf(this.qtdNeuroniosCamadaOculta);
		}
		aux[5] = "Taxa de aprendizado inicial: " + String.valueOf(this.txAprendInicial) + " | Taxa de aprendizado final: " + String.valueOf(this.txAprendFinal);
		aux[6] = "Caminho: " + caminho;
		
		ES.escreverDados(caminho, aux);
		
		if(this.tipo == "LVQ"){
			MN.criarArquivo((MN.transformaBidimensional(this.pesos, 0)), caminhoPesosFinal);
		}else{
			MN.criarArquivo((MN.transformaBidimensional(this.pesos, 0)), caminhoPesosEscondidaFinal);
			MN.criarArquivo((MN.transformaBidimensional(this.pesos, 1)), caminhoPesosFinal);
		}
	}
}
