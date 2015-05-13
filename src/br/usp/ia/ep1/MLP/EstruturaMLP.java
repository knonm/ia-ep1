package br.usp.ia.ep1.MLP;
import java.util.*;

public class EstruturaMLP {
	
	private Neuronio[] camadaEscondida;
	private Neuronio[] camadaDeSaida;

	public EstruturaMLP(){ }
	
	/*O construtor inicializará a estrutura da MLP com os valores de peso e a quantidade de neurônios tanto da camada
	 * de entrada quanto a de saída, assim como os valores de bias de ambas as camadas, especificados pelo usuário.
	 * A classe 'DadosProcessados' é onde estes valores serão armazenados para uma melhor visibiliade*/	
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
		
		//Equivalente ao DeltaWjk (que será usado para, mais tarde, atualizar os Wjk, ou seja, os pesos) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks" 
		double[][] correcaoPesoSaida = new double[this.camadaDeSaida.length][this.camadaEscondida.length];		
		
		//Equivalente ao DeltaW0k (que será usado para, mais tarde, atualizar os W0k, ou seja, os bias) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks"
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
			
			//Calculando termo de correção de peso
			for(int j = 0; j < camadaEscondida.length; j++)
				correcaoPesoSaida[index][j] = aprendizado * this.camadaDeSaida[index].getLocalGradient() * outputCamEntrada[j].getOutput();
			
			//Calculando termo de correção de bias
			correcaoBiasSaida[index] = aprendizado * this.camadaDeSaida[index].getLocalGradient();
			
			
			
			// Somatorio armazenará a multiplicação entre cada gradiente local e cada peso das camadas escondidas
			double[] somatorio = new double[camadaEscondida.length];
			
			//Armazenará o resultado dos calculos dos termos de informação de erro
			double[] errorTerm = new double[camadaEscondida.length];
			
			
			//double[][] correcaoPesoEscondida = new double[camadaEscondida.length][camadaEscondida[0].peso.length];
			
			double[] correcaoBiasEscondida = new double[camadaEscondida.length];
						
			for(int j = 0; j < camadaEscondida.length; j++)
			{
				// faz o somatório para cada input de delta
				for(int k = 0; k < camadaSaida.length; k++)
				{
					somatorio[j] += deltaK[k]*camadaSaida[k].getPeso(j);
				}
				
				// calcula o termo de erro de informação
				errorTerm[j] = somatorio[j]*camadaEscondida[j].derivada();
				
				// calcula a correção para cada peso do neurônio ativo
				for(int i = 0; i < tupla.length(); i++)
					correcaoPesoEscondida[j][i] = aprendizado*errorTerm[j]*tupla.valor(i);
				
				correcaoBiasEscondida[j] = aprendizado*errorTerm[j];
				
			}
			
			// atualiza pesos e viés na camada de saída
			
			for(int k = 0; k < camadaSaida.length; k++)
			{
				camadaSaida[k].setVies(camadaSaida[k].getVies()+delta_w0K[k]);
				for(int j = 0; j < camadaEscondida.length; j++)
					camadaSaida[k].setPeso(j, delta_wJK[k][j]);
			}
			
			
			// atualiza pesos e viés na camada escondida
			for(int j = 0; j < camadaEscondida.length; j++)
			{

				camadaEscondida[j].setVies(camadaEscondida[j].getVies()+correcaoBiasEscondida[j]);
				for(int i = 0; i < tupla.length(); i++)
					camadaEscondida[j].setPeso(i, correcaoPesoEscondida[j][i]);
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
