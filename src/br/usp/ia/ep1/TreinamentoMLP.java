package br.usp.ia.ep1;

public class TreinamentoMLP 
{
	private DadosProcessados[] entrada;
	private EstruturaMLP mlp;
	private double taxaDeAprendizado;
	
	public TreinamentoMLP(int qtdNeuroniosCamadaEscondida, int qtdeNeuroniosCamadaSaida, DadosProcessados[] dadosEntrada, double taxaAprendizado)
	{
		this.mlp = new EstruturaMLP(qtdNeuroniosCamadaEscondida, qtdeNeuroniosCamadaSaida);		
		this.entrada = new DadosProcessados[dadosEntrada.length];
		this.taxaDeAprendizado = taxaAprendizado;
		
		this.PopularEstruturaDadosDeEntrada(dadosEntrada);			
	}
	
	private void PopularEstruturaDadosDeEntrada(DadosProcessados[] dadosEntrada)
	{
		for(int i = 0; i < this.entrada.length; i++)
			this.entrada[i] = new DadosProcessados(dadosEntrada[i].getClasse(), dadosEntrada[i].getDadosDeEntrada());
	}
	
	public void Treinar(int quantidadeTreinos)
	{
		int intervalosParaExibicao = 100;
		int epocasExecutadas = 0;
		
		for(int treino = 0; treino < quantidadeTreinos/intervalosParaExibicao; treino++)
		{
			for(int epoca = 0; epoca < intervalosParaExibicao; epoca++)
			{
				for(DadosProcessados dado: entrada)
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
	
	private void executarTreino(DadosProcessados dados)
	{
		
	}
}