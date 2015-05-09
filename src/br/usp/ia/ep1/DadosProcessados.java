package br.usp.ia.ep1;

public class DadosProcessados 
{
	private int quantidadeNeuronios;
	private double pesos[];
	
	public void setQuantidadeNeuronios(int quantidade)
	{
		this.quantidadeNeuronios = quantidade;
	}
	
	public void setPesosDosNeuronios(int index, double valor)
	{
		this.pesos[index] = valor;
	}
	
	public double[] getPesos()
	{
		return this.pesos;
	}
	
	public int getQuantidadeDeNeuronios()
	{
		return this.quantidadeNeuronios;
	}
	
	public int getQuantidadePesos()
	{
		return this.pesos.length;
	}
}
