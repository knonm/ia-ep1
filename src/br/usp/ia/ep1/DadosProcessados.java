package br.usp.ia.ep1;

public class DadosProcessados 
{
	private double classe;
	private double[] dadosDeEntrada;
	
	public DadosProcessados(double classe, double[] dadosDeEntrada)
	{
		this.classe = classe;
		this.dadosDeEntrada = dadosDeEntrada.clone();
	}
	
	public double getClasse()
	{
		return this.classe;
	} 
	
	public double[] getDadosDeEntrada()
	{
		return this.dadosDeEntrada;
	}
	
	public double getValorEspecifico(int index)
	{
		return this.dadosDeEntrada[index];
	}
}
