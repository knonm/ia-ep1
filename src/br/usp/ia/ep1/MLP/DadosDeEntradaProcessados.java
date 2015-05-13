package br.usp.ia.ep1.MLP;

public class DadosDeEntradaProcessados
{
	private double classe;
	private double[] dadosEntrada;
	
	public DadosDeEntradaProcessados(double classe, double[] dadosDeEntrada)
	{
		this.classe = classe;
		this.dadosEntrada = dadosDeEntrada.clone();
	}
	
	public double getClasse()
	{
		return this.classe;
	}
	
	public double[] getDadosDeEntrada()
	{
		return this.dadosEntrada;
	}
	
	public int getQuantidadeDadosEntrada()
	{
		return this.dadosEntrada.length;
	}
}
