package br.usp.ia.ep1;

import java.util.*;

public class MLP {
	
	float[] pesos;

	private void inicializarPesosComValoresAleatorios(){
		for(int i = 0; i < this.pesos.length; i++)
			this.pesos[i] = (float)Math.random();
	}
	
	private void ExecutarFeedFoward(){}
	
	private void ExecutarBackPropagation(){}
	
	private double CalcularFuncaoDeAtivacaoDeSigmoid(double variavel){
		return 1/(1 + Math.exp(variavel * -1));
	}
	
	public MLP(float[] dadosDeEntrada, float taxaAprendizado, int quantidadeNeuronios, boolean pesosAleatorios)
	{
		pesos = new float[dadosDeEntrada.length];
		
		
		this.inicializarPesosComValoresAleatorios();
		
		
		
	}
}
