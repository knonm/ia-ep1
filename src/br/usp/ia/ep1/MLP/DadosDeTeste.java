package br.usp.ia.ep1.MLP;

public class DadosDeTeste
{
	private double classe;
	private double[] dadosDeTeste;
	
	public DadosDeTeste(double[] dadosDeTeste)
	{
		this.dadosDeTeste = dadosDeTeste.clone();
	}
	
	public double getClasse()
	{
		return this.classe;
	}
	
	public void setClasse(double classe) {
		this.classe = classe;
	}

	public double[] getDadosDeTeste()
	{
		return this.dadosDeTeste;
	}
	
	public int getQuantidadeAtributos()
	{
		return this.dadosDeTeste.length;
	}
}
