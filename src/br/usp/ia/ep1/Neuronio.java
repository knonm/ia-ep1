package br.usp.ia.ep1;

public class Neuronio {

	private double bias;
	private double pesos[];
	
	public Neuronio(int quantidadeNeuroniosCamadaAnterior)
	{
		this.pesos = new double[quantidadeNeuroniosCamadaAnterior];
	}
	
	public void setBias(double valor)
	{
		this.bias = valor;
	}
	
}
