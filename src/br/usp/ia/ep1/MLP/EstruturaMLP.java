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
		
		criarNeuroniosCamadaEscondidaPesosComValoresAleatorios(qtdeNeuroniosCamadaEscondida);
		criarNeuroniosCamadaSaidaPesosComValoresAleatorios(qtdeNeuroniosCamadaSaida);
	}
	
	public int getTamanhoCamadaEscondida()
	{
		return this.camadaEscondida.length;
	}
	
	public int getTamanhoCamadaSaida()
	{
		return this.camadaDeSaida.length;
	}
	
	public int getTamanhoCamadaEntrada()
	{
		return this.camadaEscondida[0].QuantidadePesos();
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
	
	public PesosCalculados ExecutarFeedFoward(double[] entrada)
	{
		return this.camadaEscondida[0].FeedFoward(entrada);
	}
	
	public double DerivadaFuncaoBinariaSigmoid(double valor)
	{
		return this.camadaEscondida[0].DerivadaFuncaoDeAtivacaoBinariaDeSigmoid(valor);
	}
	
	public int ExecutarRede(DadosDeEntradaProcessados[] dados)
	{
		double[] saidasCamEscondida = new double[this.getTamanhoCamadaEscondida()];
		double[] saidasDaRede = new double[this.getTamanhoCamadaSaida()];
		
		for(int i = 0; i < this.getTamanhoCamadaEscondida(); i++)
			for(int j = 0; j < dados.length; j++)
				saidasCamEscondida[i] += this.ExecutarFeedFoward(dados[j]).getOutput();
				
		for(int i = 0; i < this.getTamanhoCamadaEscondida(); i++)
			saidasDaRede[i] = this.ExecutarFeedFoward(saidasCamEscondida).getOutput();
		
		
		return extrairMaiorValorDoArray(saidasDaRede);
	}
	
	public int extrairMaiorValorDoArray(double[] saidas)
	{
		int index = 0;
		double maiorValor = -100;
		int classe = 0;
		
		for(double saida: saidas)
		{
			if(saida > maiorValor)
			{
				maiorValor = saida;
				classe = index;
			}
			index++;
		}	
		
		return classe;
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
	
	private void criarNeuroniosCamadaEscondidaPesosComValoresAleatorios(int qtdeNeuroniosCamadaEscondida)
	{
		for(int i = 0; i < this.camadaEscondida.length; i++)
		{
			this.camadaEscondida[i] = new Neuronio(qtdeNeuroniosCamadaEscondida);
			this.camadaEscondida[i].InicializarBiasComValorAleatoro();
			this.camadaEscondida[i].InicializarPesosComValoresAleatorios();
		}
	}
	
	private void criarNeuroniosCamadaSaidaPesosComValoresAleatorios(int qtdeNeuroniosCamadaSaida)
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
