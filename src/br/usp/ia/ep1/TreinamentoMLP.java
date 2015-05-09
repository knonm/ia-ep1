package br.usp.ia.ep1;

public class TreinamentoMLP 
{
	private DadosProcessados[] entrada;
	private EstruturaMLP mlp;
	
	public TreinamentoMLP(int qtdNeuroniosCamadaEscondida, int qtdeNeuroniosCamadaSaida)
	{
		mlp = new EstruturaMLP(qtdNeuroniosCamadaEscondida, qtdeNeuroniosCamadaSaida);
		
				
	}
}
