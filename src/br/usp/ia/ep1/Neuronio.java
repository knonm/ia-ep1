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
	
	public double ExecutarFeedFoward(DadosProcessados entrada)
	{
		return this.feedFoward(entrada.getDadosDeEntrada());	
	}
	
	public double ExecutarFeedFoward(double[] entrada)
	{
		return this.feedFoward(entrada);
	}
	
	private double feedFoward(double[] entrada)
	{
		double somatorio = 0;
		
		for(int i = 0; i < this.pesos.length; i++)
			somatorio =+ entrada[i] * this.pesos[i];
		
		somatorio += this.bias;
		
		return this.FuncaoDeAtivacaoBinariaDeSigmoid(somatorio);	
	}
	
	public double FuncaoDeAtivacaoBinariaDeSigmoid(double variavel){
		return 1/(1 + Math.exp(variavel * -1));
	}
	
	/*Derivada apresentada por Laurene Fausett no livro "Fundamentals of Neural Networks"*/
	public double derivadaFuncaoDeAtivacaoBinariaDeSigmoid(double variavel)
	{
		return (this.FuncaoDeAtivacaoBinariaDeSigmoid(variavel) * (1 - this.FuncaoDeAtivacaoBinariaDeSigmoid(variavel))); 
	}
	
	private double gerarNumeroAleatorioEmDadoIntervalo(int valorMaximoDesejado, int valorMinimoDesejado)
	{
		return Math.random() * ((valorMaximoDesejado - valorMinimoDesejado) + 1);
	}
}
