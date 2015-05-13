package br.usp.ia.ep1.MLP;

public class PesosCalculados 
{
	private double somatorioPesos;
	private double saidaNeuronio;
	
	public PesosCalculados()
	{ 
		this.somatorioPesos = 0;
		this.saidaNeuronio = 0;
	}
	
	public PesosCalculados(double somatorio, double saidaDoNeuronio)
	{
		this.somatorioPesos = somatorio;
		this.saidaNeuronio = saidaDoNeuronio;
	}
	
	public double getSomatorioPeso()
	{
		return this.somatorioPesos;
	}
	
	public double getOutput()
	{
		return this.saidaNeuronio;
	}
	
	public void setSomatorioPeso(double valor)
	{
		this.somatorioPesos += valor;
	}
	
	public void setSaidaNeuronio(double valor)
	{
		this.saidaNeuronio += valor;
	}
}
