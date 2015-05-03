package br.usp.ia.ep1;
import java.util.*;

public class MLP {
	
	float[] pesos;
	Neuronio[] camadaEscondida;
	Neuronio[] camadaDeSaida;

	private void inicializarPesosComValoresAleatorios(){
		for(int i = 0; i < this.pesos.length; i++)
			this.pesos[i] = (float)Math.random();
	}
	
	public MLP(double[][] camadaEscondida, double[][] camadaSaida, double[] biasEscondida, double[] biasSaida)
	{
		this.camadaEscondida = new Neuronio[camadaEscondida.length];
		this.camadaDeSaida = new Neuronio[camadaSaida.length];
		
		CriarNeuroniosDaCamadaEscondida(camadaEscondida[0].length, biasEscondida);
		CriarNeuroniosDaCamadaDeSaida(camadaSaida[0].length, biasSaida);
		
		
	}
	
	private void CriarNeuroniosDaCamadaEscondida(int quantidadeNeuroniosNaCamadaDeEntrada, double[] biasDaCamadaEscondida)
	{
		for(int i = 0; i < this.camadaEscondida.length; i++)
		{
			this.camadaEscondida[i] = new Neuronio(quantidadeNeuroniosNaCamadaDeEntrada);
			this.camadaEscondida[i].setBias(biasDaCamadaEscondida[i]);
		}
	}
	
	private void CriarNeuroniosDaCamadaDeSaida(int quantidadeNeuroniosNaCamadaEscondida, double[] biasDaCamadaDeSaida)
	{
		for(int i = 0; i < this.camadaDeSaida.length; i++)
		{
			this.camadaDeSaida[i] = new Neuronio(quantidadeNeuroniosNaCamadaEscondida);
			this.camadaDeSaida[i].setBias(biasDaCamadaDeSaida[i]);
		}
	}
	
	private void ExecutarFeedFoward(){}
	
	private void ExecutarBackPropagation(){}
	
	private double CalcularFuncaoDeAtivacaoBinariaDeSigmoid(double variavel){
		return 1/(1 + Math.exp(variavel * -1));
	}
	
}
