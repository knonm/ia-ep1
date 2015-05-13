package br.usp.ia.ep1.MLP;
import java.util.*;

public class EstruturaMLP {
	
	private Neuronio[] camadaEscondida;
	private Neuronio[] camadaDeSaida;

	public EstruturaMLP(){ }
	
	/*O construtor inicializar� a estrutura da MLP com os valores de peso e a quantidade de neur�nios tanto da camada
	 * de entrada quanto a de sa�da, assim como os valores de bias de ambas as camadas, especificados pelo usu�rio.
	 * A classe 'DadosProcessados' � onde estes valores ser�o armazenados para uma melhor visibiliade*/	
	public EstruturaMLP(InformacoesDaCamada camadaEscondida, InformacoesDaCamada camadaSaida, double[] biasCamadaEscondida, double[] biasCamadaSaida)
	{
		this.camadaEscondida = new Neuronio[camadaEscondida.getQuantidadeDeNeuronios()];
		this.camadaDeSaida = new Neuronio[camadaSaida.getQuantidadeDeNeuronios()];
		
		criarNeuroniosCamadaEscondida(camadaEscondida, biasCamadaEscondida);
		criarNeuroniosCamadaSaida(camadaSaida, biasCamadaSaida);	
	}
	
	public EstruturaMLP(int qtdeNeuroniosCamadaEscondida, int qtdeNeuroniosCamadaSaida)
	{
		this.camadaEscondida = new Neuronio[qtdeNeuroniosCamadaEscondida];
		this.camadaDeSaida = new Neuronio[qtdeNeuroniosCamadaSaida];
		
		criarNeuroniosCamadaEscondidaTreinamento(qtdeNeuroniosCamadaEscondida);
		criarNeuroniosCamadaSaidaTreinamento(qtdeNeuroniosCamadaSaida);
	}
	
	public int getTamanhoCamadaEscondida()
	{
		return this.camadaEscondida.length;
	}
	
	public int getTamanhoCamadaSaida()
	{
		return this.camadaDeSaida.length;
	} 
	
	public Neuronio getNeuronioDaCamadaEscondida(int indexNeuronio)
	{
		return this.camadaEscondida[indexNeuronio];
	}
	
	public Neuronio getNeuronioDaCamadaDeSaida(int indexNeuronio)
	{
		return this.camadaDeSaida[indexNeuronio];
	}
	
	public void ExecutarBackPropagation(PesosCalculados[] outputCamEntrada, PesosCalculados[] outputCamSaida, 
										DadosDeEntradaProcessados dados, double aprendizado)
	{
		//Equivale ao target pattern, Tk, especificado no livro de Laurene Fausett, "Fundamentals of Neural Networks"
		double resultadoEsperado;
		
		//Equivalente ao DeltaWjk (que ser� usado para, mais tarde, atualizar os Wjk, ou seja, os pesos) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks" 
		double[][] correcaoPesoSaida = new double[this.camadaDeSaida.length][this.camadaEscondida.length];		
		
		//Equivalente ao DeltaW0k (que ser� usado para, mais tarde, atualizar os W0k, ou seja, os bias) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks"
		double[] correcaoBiasSaida = new double[this.camadaDeSaida.length];
		
		for(int index = 0; index < this.camadaDeSaida.length; index++)
		{
			if(dados.getClasse() == index)
				resultadoEsperado = 1;
			else
				resultadoEsperado = 0;
			
			//Definindo o gradiente de erro
			double erro = (resultadoEsperado - outputCamSaida[index].getOutput()) * this.camadaDeSaida[index].DerivadaFuncaoDeAtivacaoBinariaDeSigmoid(outputCamSaida[index].getSomatorioPeso());			
			this.camadaDeSaida[index].setLocalGradient(erro);
			
			//Calculando termo de corre��o de peso
			for(int j = 0; j < camadaEscondida.length; j++)
				correcaoPesoSaida[index][j] = aprendizado * this.camadaDeSaida[index].getLocalGradient() * outputCamEntrada[j].getOutput();
			
			//Calculando termo de corre��o de bias
			correcaoBiasSaida[index] = aprendizado * this.camadaDeSaida[index].getLocalGradient();
			
			
			
			
			
			// prepara o c�lculo para o termo de erro de informa��o
			double[] delta_inJ = new double[camadaEscondida.length];
			double[] deltaJ = new double[camadaEscondida.length];
			
			double[][] delta_vIJ = new double[camadaEscondida.length][camadaEscondida[0].peso.length];
			double[] delta_v0J = new double[camadaEscondida.length];
						
			for(int j = 0; j < camadaEscondida.length; j++)
			{
				// faz o somat�rio para cada input de delta
				for(int k = 0; k < camadaSaida.length; k++)
				{
					delta_inJ[j] += deltaK[k]*camadaSaida[k].getPeso(j);
				}
				
				// calcula o termo de erro de informa��o
				deltaJ[j] = delta_inJ[j]*camadaEscondida[j].derivada();
				
				// calcula a corre��o para cada peso do neur�nio ativo
				for(int i = 0; i < tupla.length(); i++)
					delta_vIJ[j][i] = aprendizado*deltaJ[j]*tupla.valor(i);
				
				delta_v0J[j] = aprendizado*deltaJ[j];
				
			}
			
			// atualiza pesos e vi�s na camada de sa�da
			
			for(int k = 0; k < camadaSaida.length; k++)
			{
				camadaSaida[k].setVies(camadaSaida[k].getVies()+delta_w0K[k]);
				for(int j = 0; j < camadaEscondida.length; j++)
					camadaSaida[k].setPeso(j, delta_wJK[k][j]);
			}
			
			
			// atualiza pesos e vi�s na camada escondida
			for(int j = 0; j < camadaEscondida.length; j++)
			{

				camadaEscondida[j].setVies(camadaEscondida[j].getVies()+delta_v0J[j]);
				for(int i = 0; i < tupla.length(); i++)
					camadaEscondida[j].setPeso(i, delta_vIJ[j][i]);
			}	
		}
	}
	
	private void criarNeuroniosCamadaEscondida(InformacoesDaCamada camadaEscondida, double[] biasDaCamadaEscondida)
	{
		for(int i = 0; i < this.camadaEscondida.length; i++)
		{
			this.camadaEscondida[i] = new Neuronio(camadaEscondida.getQuantidadeDeNeuronios());
			this.camadaEscondida[i].setBias(biasDaCamadaEscondida[i]);
			this.camadaEscondida[i].InicializarComPesosEspecificos(camadaEscondida.getPesos());;
		}
	}
	
	private void criarNeuroniosCamadaSaida(InformacoesDaCamada camadaSaida, double[] biasDaCamadaDeSaida)
	{
		for(int i = 0; i < this.camadaDeSaida.length; i++)
		{
			this.camadaDeSaida[i] = new Neuronio(camadaSaida.getQuantidadeDeNeuronios());
			this.camadaDeSaida[i].setBias(biasDaCamadaDeSaida[i]);
			this.camadaDeSaida[i].InicializarComPesosEspecificos(camadaSaida.getPesos());;
		}
	}
	
	private void criarNeuroniosCamadaEscondidaTreinamento(int qtdeNeuroniosCamadaEscondida)
	{
		for(int i = 0; i < this.camadaEscondida.length; i++)
		{
			this.camadaEscondida[i] = new Neuronio(qtdeNeuroniosCamadaEscondida);
			this.camadaEscondida[i].InicializarBiasComValorAleatoro();
			this.camadaEscondida[i].InicializarPesosComValoresAleatorios();
		}
	}
	
	private void criarNeuroniosCamadaSaidaTreinamento(int qtdeNeuroniosCamadaSaida)
	{
		for(int i = 0; i < this.camadaDeSaida.length; i++)
		{
			this.camadaDeSaida[i] = new Neuronio(qtdeNeuroniosCamadaSaida);
			this.camadaDeSaida[i].InicializarBiasComValorAleatoro();
			this.camadaDeSaida[i].InicializarPesosComValoresAleatorios();
		}
	}
}
