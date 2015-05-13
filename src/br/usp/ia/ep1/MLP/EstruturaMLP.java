package br.usp.ia.ep1.MLP;
import java.util.*;

public class EstruturaMLP {
	
	private Neuronio[] camadaEscondida;
	private Neuronio[] camadaDeSaida;

	public EstruturaMLP(){ }
	
	/*O construtor inicializará a estrutura da MLP com os valores de peso e a quantidade de neurônios tanto da camada
	 * de entrada quanto a de saída, assim como os valores de bias de ambas as camadas, especificados pelo usuário.
	 * A classe 'DadosProcessados' é onde estes valores serão armazenados para uma melhor visibiliade*/	
	public EstruturaMLP(InformacoesDaCamada camadaEscondida, InformacoesDaCamada camadaSaida, double[] biasCamadaEscondida, double[] biasCamadaSaida)
	{
		this.camadaEscondida = new Neuronio[camadaEscondida.getQuantidadeDeNeuronios()];
		this.camadaDeSaida = new Neuronio[camadaSaida.getQuantidadeDeNeuronios()];
		
		criarNeuroniosCamadaEscondida(camadaEscondida, biasCamadaEscondida);
		criarNeuroniosCamadaSaida(camadaSaida, biasCamadaSaida);	
	}
	
	public EstruturaMLP(int qtdeNeuroniosCamadaEscondida, int qtdeNeuroniosCamadaSaida)
	{
		this.camadaEscondida = new Neuronio[qtdeNeuroniosCamadaEscondida];
		this.camadaDeSaida = new Neuronio[qtdeNeuroniosCamadaSaida];
		
		criarNeuroniosCamadaEscondidaTreinamento(qtdeNeuroniosCamadaEscondida);
		criarNeuroniosCamadaSaidaTreinamento(qtdeNeuroniosCamadaSaida);
	}
	
	public int getTamanhoCamadaEscondida()
	{
		return this.camadaEscondida.length;
	}
	
	public int getTamanhoCamadaSaida()
	{
		return this.camadaDeSaida.length;
	} 
	
	public Neuronio getNeuronioCamadaEscondida(int indexNeuronio)
	{
		return this.camadaEscondida[indexNeuronio];
	}
	
	public Neuronio getNeuronioCamadaSaida(int indexNeuronio)
	{
		return this.camadaDeSaida[indexNeuronio];
	}
	
	public PesosCalculados ExecutarFeedFoward(DadosDeEntradaProcessados entrada)
	{
		return this.camadaEscondida[0].FeedFoward(entrada.getDadosDeEntrada());	
	}
	
	public PesosCalculados ExecutarFeedFoward(PesosCalculados[] entrada)
	{
		return this.camadaEscondida[0].FeedFoward(this.PesosCalculadosToDouble(entrada));
	}
	
	private void criarNeuroniosCamadaEscondida(InformacoesDaCamada camadaEscondida, double[] biasDaCamadaEscondida)
	{
		for(int i = 0; i < this.camadaEscondida.length; i++)
		{
			this.camadaEscondida[i] = new Neuronio(camadaEscondida.getQuantidadeDeNeuronios());
			this.camadaEscondida[i].setBias(biasDaCamadaEscondida[i]);
			this.camadaEscondida[i].InicializarComPesosEspecificos(camadaEscondida.getPesos());;
		}
	}
	
	private void criarNeuroniosCamadaSaida(InformacoesDaCamada camadaSaida, double[] biasDaCamadaDeSaida)
	{
		for(int i = 0; i < this.camadaDeSaida.length; i++)
		{
			this.camadaDeSaida[i] = new Neuronio(camadaSaida.getQuantidadeDeNeuronios());
			this.camadaDeSaida[i].setBias(biasDaCamadaDeSaida[i]);
			this.camadaDeSaida[i].InicializarComPesosEspecificos(camadaSaida.getPesos());;
		}
	}
	
	private void criarNeuroniosCamadaEscondidaTreinamento(int qtdeNeuroniosCamadaEscondida)
	{
		for(int i = 0; i < this.camadaEscondida.length; i++)
		{
			this.camadaEscondida[i] = new Neuronio(qtdeNeuroniosCamadaEscondida);
			this.camadaEscondida[i].InicializarBiasComValorAleatoro();
			this.camadaEscondida[i].InicializarPesosComValoresAleatorios();
		}
	}
	
	private void criarNeuroniosCamadaSaidaTreinamento(int qtdeNeuroniosCamadaSaida)
	{
		for(int i = 0; i < this.camadaDeSaida.length; i++)
		{
			this.camadaDeSaida[i] = new Neuronio(qtdeNeuroniosCamadaSaida);
			this.camadaDeSaida[i].InicializarBiasComValorAleatoro();
			this.camadaDeSaida[i].InicializarPesosComValoresAleatorios();
		}
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
