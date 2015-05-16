package br.usp.ia.ep1.MLP;

public class Neuronio {

	private double bias;
	private double pesos[];
	private double termoDeErro;
	
	public Neuronio(int quantidadeNeuroniosCamadaAnterior)
	{
		this.pesos = new double[quantidadeNeuroniosCamadaAnterior];
	}
	
	public Neuronio(double bias, double[] pesos, double termoDeErro)
	{
		this.bias = bias;
		this.pesos = pesos.clone();
		this.termoDeErro = termoDeErro;
	}
	
	public void setBias(double valor)
	{
		this.bias = valor;
	}
	
	public double getBias()
	{
		return this.bias;
	}
	
	public void setPeso(int index, double valor)
	{
		this.pesos[index] = valor;
	}
	
	public double getPeso(int index)
	{
		return this.pesos[index];
	}
	
	public double[] getTodosOsPesos()
	{
		return this.pesos;
	}
	
	public int QuantidadePesos()
	{
		return this.pesos.length;
	}
	
	public void InicializarPesosComValoresAleatorios()
	{
		for(int i = 0; i < pesos.length; i++)
			pesos[i] = this.gerarNumeroAleatorioEmDadoIntervalo(1, 0);
	}
	
	public void InicializarPesosComZeros()
	{
		for(int i = 0; i < this.pesos.length; i++)
			this.pesos[i] = 0;
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
	
	public PesosCalculados FeedFoward(double[] entrada)
	{
		double somatorio = 0;
		
		for(int i = 0; i < this.pesos.length; i++)
		{
			somatorio =+ entrada[i] * this.pesos[i];
		}
		
		somatorio += this.bias;
		
		double output = this.FuncaoDeAtivacaoBinariaDeSigmoid(somatorio);
		
		PesosCalculados resposta = new PesosCalculados(somatorio, output);
		
		return resposta;	
	}
	
	public double FuncaoDeAtivacaoBinariaDeSigmoid(double variavel){
		return 1/(1 + Math.exp(variavel * -1));
	}
	
	/*Derivada apresentada por Laurene Fausett no livro "Fundamentals of Neural Networks"*/
	public double DerivadaFuncaoDeAtivacaoBinariaDeSigmoid(double variavel)
	{
		double valorDeAtivacao = this.FuncaoDeAtivacaoBinariaDeSigmoid(variavel);
		return (valorDeAtivacao * (1 - valorDeAtivacao)); 
	}
	
	public double FuncaoDeAtivacaoBipolarDeSigmoid(double variavel){
		return (2 / (1 + Math.exp(variavel * -1))) -1;
	}
	
	/*Derivada apresentada por Laurene Fausett no livro "Fundamentals of Neural Networks"*/
	public double DerivadaFuncaoDeAtivacaoBipolarDeSigmoid(double variavel)
	{
		double valorDeAtivacao = this.FuncaoDeAtivacaoBipolarDeSigmoid(variavel);
		return  0.5 * (1 + valorDeAtivacao) * (1 - valorDeAtivacao);
	}
	
	public void setTermoDeErro(double valor)
	{
		this.termoDeErro = valor;
	}
	
	public double getTermoDeErro()
	{
		return this.termoDeErro;
	}
	
	private double gerarNumeroAleatorioEmDadoIntervalo(int valorMaximoDesejado, int valorMinimoDesejado)
	{
		return Math.random() * ((valorMaximoDesejado - valorMinimoDesejado) + 1);
	}
}
