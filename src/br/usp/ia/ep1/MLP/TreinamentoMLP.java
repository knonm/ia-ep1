package br.usp.ia.ep1.MLP;

public class TreinamentoMLP 
{
	private DadosDeEntradaProcessados[] entrada;
	private EstruturaMLP mlp;
	private double taxaDeAprendizado;
	
	public TreinamentoMLP(int qtdNeuroniosCamadaEscondida, int qtdeNeuroniosCamadaSaida, DadosDeEntradaProcessados[] dadosEntrada, double taxaAprendizado)
	{
		this.mlp = new EstruturaMLP(qtdNeuroniosCamadaEscondida, qtdeNeuroniosCamadaSaida);		
		this.entrada = new DadosDeEntradaProcessados[dadosEntrada.length];
		this.taxaDeAprendizado = taxaAprendizado;
		
		this.PopularEstruturaDadosDeEntrada(dadosEntrada);			
	}
	
	private void PopularEstruturaDadosDeEntrada(DadosDeEntradaProcessados[] dadosEntrada)
	{
		for(int i = 0; i < this.entrada.length; i++)
			this.entrada[i] = new DadosDeEntradaProcessados(dadosEntrada[i].getClasse(), dadosEntrada[i].getDadosDeEntrada());
	}
	
	public void Treinar(int quantidadeTreinos)
	{
		int intervalosParaExibicao = 100;
		int epocasExecutadas = 0;
		
		for(int treino = 0; treino < quantidadeTreinos/intervalosParaExibicao; treino++)
		{
			for(int epoca = 0; epoca < intervalosParaExibicao; epoca++)
			{
				for(DadosDeEntradaProcessados dado: entrada)
				{
					this.executarTreino(dado);
				}				
				epocasExecutadas += epoca;
			}
			
			System.out.println("Epocas executadas: " + epocasExecutadas);
			System.out.println("Taxa de aprendizado: " + this.taxaDeAprendizado);
			//System.out.println("Taxa de erro: "+errosTreinamento());
			System.out.println();
		}
	}
	
	//M�todo que executa o treino da rede para cada epoca corrente no m�todo Treinar()
	private void executarTreino(DadosDeEntradaProcessados dados)
	{
		PesosCalculados[] resultCamEscondida = new PesosCalculados[this.mlp.getTamanhoCamadaEscondida()];
		PesosCalculados[] resultCamSaida = new PesosCalculados[this.mlp.getTamanhoCamadaSaida()];
		
		//Executa FeedFoward para cada neur�nio da camada escondida (por isso, metodo foi guardado dentro do neur�nio)
		for(int neuronioEscondido = 0; neuronioEscondido < this.mlp.getTamanhoCamadaEscondida(); neuronioEscondido++)
			resultCamEscondida[neuronioEscondido] = this.mlp.getNeuronioDaCamadaEscondida(neuronioEscondido).ExecutarFeedFoward(dados);
		
				
		//Executa FeedFoward para cada neur�nio da camada de sa�da (por isso, metodo foi guardado dentro do neur�nio)
		for(int neuronioSaida = 0; neuronioSaida < this.mlp.getTamanhoCamadaSaida(); neuronioSaida++)
			resultCamSaida[neuronioSaida] = this.mlp.getNeuronioDaCamadaDeSaida(neuronioSaida).ExecutarFeedFoward(resultCamEscondida);
		
		//Executa BackPropagation diretamente no objeto MLP
		this.mlp.ExecutarBackPropagation(resultCamEscondida, resultCamSaida, dados, this.taxaDeAprendizado);	
	}
}
