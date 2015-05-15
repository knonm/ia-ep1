package br.usp.ia.ep1.MLP;

public class DadosDeTeste
{
	private double classeReal;
	private double classePredita;
	private double[] dadosDeTeste;
	
	public DadosDeTeste(double classeReal, double[] dadosDeTeste)
	{
		this.dadosDeTeste = dadosDeTeste.clone();
		this.classeReal = classeReal;
	}

	public double getClasseReal() {
		return classeReal;
	}

	public void setClasseReal(double classeReal) {
		this.classeReal = classeReal;
	}

	public double getClassePredita() {
		return classePredita;
	}

	public void setClassePredita(double classePredita) {
		this.classePredita = classePredita;
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
