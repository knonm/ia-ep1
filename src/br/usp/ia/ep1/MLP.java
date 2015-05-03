package br.usp.ia.ep1;

import java.util.*;

public class MLP {
	
	float[] pesos;

	private void inicializarPesosComValoresAleatorios(){
		for(int i = 0; i < this.pesos.length; i++)
			this.pesos[i] = (float)Math.random();
	}
	
	public MLP(float[] dadosDeEntrada, float taxaAprendizado, int quantidadeNeuronios, boolean pesosAleatorios)
	{
		pesos = new float[dadosDeEntrada.length];
		
		
		this.inicializarPesosComValoresAleatorios();
		
		
		
	}
}
