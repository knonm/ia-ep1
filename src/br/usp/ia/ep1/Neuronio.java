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
	
	public void setPeso(int index, double valor)
	{
		this.pesos[index] = valor;
	}
	
	public void InicializarPesosComValoresAleatorios()
	{
		for(int i = 0; i < pesos.length; i++)
			pesos[i] = this.gerarNumeroAleatorioEmDadoIntervalo(1, 0);
	}
	
	public void InicializarBiasComValorAleatoro()
	{
		this.bias = this.gerarNumeroAleatorioEmDadoIntervalo(1, 0);
	}
	
	public void InicializarComPesosEspecificos(double[] pesos)
	{
		for(int i = 0; i < pesos.length; i++)
			this.pesos[i] = pesos[i];
	}
	
	private double gerarNumeroAleatorioEmDadoIntervalo(int valorMaximoDesejado, int valorMinimoDesejado)
	{
		return Math.random() * ((valorMaximoDesejado - valorMinimoDesejado) + 1);
	}
}
