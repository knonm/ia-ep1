package br.usp.ia.ep1;
import java.util.*;

public class MLP {
	
	private Neuronio[] camadaEscondida;
	private Neuronio[] camadaDeSaida;

	public MLP(){ }
	
	public MLP(double[] teste)
	{
		
	}
		
	public void CriarCamadasEscondidasEDeSaida(double[][] camadaEscondida, double[][] camadaSaida, double[] biasCamadaEscondida, double[] biasCamadaSaida)
	{
		this.camadaEscondida = new Neuronio[camadaEscondida.length];
		this.camadaDeSaida = new Neuronio[camadaSaida.length];
		
		CriarNeuroniosDaCamadaEscondida(camadaEscondida[0].length, biasCamadaEscondida);
		CriarNeuroniosDaCamadaDeSaida(camadaSaida[0].length, biasCamadaSaida);		
	}
	
	private void CriarNeuroniosDaCamadaEscondida(int quantidadeNeuroniosNaCamadaDeEntrada, double[] biasDaCamadaEscondida)
	{
		for(int i = 0; i < this.camadaEscondida.length; i++)
		{
			this.camadaEscondida[i] = new Neuronio(quantidadeNeuroniosNaCamadaDeEntrada);
			this.camadaEscondida[i].setBias(biasDaCamadaEscondida[i]);
			this.camadaEscondida[i].InicializarPesosAleatoriamente();
		}
	}
	
	private void CriarNeuroniosDaCamadaDeSaida(int quantidadeNeuroniosNaCamadaEscondida, double[] biasDaCamadaDeSaida)
	{
		for(int i = 0; i < this.camadaDeSaida.length; i++)
		{
			this.camadaDeSaida[i] = new Neuronio(quantidadeNeuroniosNaCamadaEscondida);
			this.camadaDeSaida[i].setBias(biasDaCamadaDeSaida[i]);
			this.camadaDeSaida[i].InicializarPesosAleatoriamente();
		}
	}
	
	private void ExecutarFeedFoward(){}
	
	private void ExecutarBackPropagation(){}
	
	private double CalcularFuncaoDeAtivacaoBinariaDeSigmoid(double variavel){
		return 1/(1 + Math.exp(variavel * -1));
	}
	
}
