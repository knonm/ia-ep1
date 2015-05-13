package br.usp.ia.ep1.MLP;

public class Neuronio {

	private double bias;
	private double pesos[];
	private double termoDeErro;
	
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
	
	public double getPeso(int index)
	{
		return this.pesos[index];
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
	
	public PesosCalculados ExecutarFeedFoward(DadosDeEntradaProcessados entrada)
	{
		return this.feedFoward(entrada.getDadosDeEntrada());	
	}
	
	public PesosCalculados ExecutarFeedFoward(PesosCalculados[] entrada)
	{
		return this.feedFoward(this.PesosCalculadosToDouble(entrada));
	}
	
	private PesosCalculados feedFoward(double[] entrada)
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
		return (this.FuncaoDeAtivacaoBinariaDeSigmoid(variavel) * (1 - this.FuncaoDeAtivacaoBinariaDeSigmoid(variavel))); 
	}
	
	public void setTermoDeErro(double valor)
	{
		this.termoDeErro = valor;
	}
	
	public double getLocalGradient()
	{
		return this.termoDeErro;
	}
	
	private double gerarNumeroAleatorioEmDadoIntervalo(int valorMaximoDesejado, int valorMinimoDesejado)
	{
		return Math.random() * ((valorMaximoDesejado - valorMinimoDesejado) + 1);
	}
	
	//Converte a classe de apoio PesosCalculados para um formato de array de doubles. Utilizado antes de enviar o array para o feedFoward.
	private double[] PesosCalculadosToDouble(PesosCalculados[] entrada)
	{
		double[] resposta = new double[entrada.length];
		
		for(int i = 0; i < entrada.length; i++)
			resposta[i] = entrada[i].getOutput();
		
		return resposta;
	}
}
