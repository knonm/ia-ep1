package br.usp.ia.ep1.MLP;

public class TreinamentoMLP 
{
	private DadosDeEntradaProcessados[] entrada;
	private EstruturaMLP mlp;
	private double taxaDeAprendizado;
	
	public TreinamentoMLP(int qtdNeuroniosCamadaEscondida, int qtdeNeuroniosCamadaSaida, DadosDeEntradaProcessados[] dadosEntrada, double taxaAprendizado)
	{
		this.mlp = new EstruturaMLP(qtdNeuroniosCamadaEscondida, qtdeNeuroniosCamadaSaida);		
		this.entrada = new DadosDeEntradaProcessados[dadosEntrada.length];
		this.taxaDeAprendizado = taxaAprendizado;
		
		this.PopularEstruturaDadosDeEntrada(dadosEntrada);			
	}
	
	private void PopularEstruturaDadosDeEntrada(DadosDeEntradaProcessados[] dadosEntrada)
	{
		for(int i = 0; i < this.entrada.length; i++)
			this.entrada[i] = new DadosDeEntradaProcessados(dadosEntrada[i].getClasse(), dadosEntrada[i].getDadosDeEntrada());
	}
	
	public void Treinar(int quantidadeTreinos)
	{
		int intervalosParaExibicao = 100;
		int epocasExecutadas = 0;
		
		for(int treino = 0; treino < quantidadeTreinos/intervalosParaExibicao; treino++)
		{
			for(int epoca = 0; epoca < intervalosParaExibicao; epoca++)
			{
				for(DadosDeEntradaProcessados dado: entrada)
				{
					this.executarTreino(dado);
				}				
				epocasExecutadas += epoca;
			}
			
			System.out.println("Epocas executadas: " + epocasExecutadas);
			System.out.println("Taxa de aprendizado: " + this.taxaDeAprendizado);
			//System.out.println("Taxa de erro: "+errosTreinamento());
			System.out.println();
		}
	}
	
	//Método que executa o treino da rede para cada epoca corrente no método Treinar()
	private void executarTreino(DadosDeEntradaProcessados dados)
	{
		PesosCalculados[] resultCamEscondida = new PesosCalculados[this.mlp.getTamanhoCamadaEscondida()];
		PesosCalculados[] resultCamSaida = new PesosCalculados[this.mlp.getTamanhoCamadaSaida()];
		
		//Executa FeedFoward para cada neurônio da camada escondida (por isso, metodo foi guardado dentro do neurônio)
		for(int neuronioEscondido = 0; neuronioEscondido < this.mlp.getTamanhoCamadaEscondida(); neuronioEscondido++)
			resultCamEscondida[neuronioEscondido] = this.mlp.ExecutarFeedFoward(dados);
		
				
		//Executa FeedFoward para cada neurônio da camada de saída (por isso, metodo foi guardado dentro do neurônio)
		for(int neuronioSaida = 0; neuronioSaida < this.mlp.getTamanhoCamadaSaida(); neuronioSaida++)
			resultCamSaida[neuronioSaida] = this.mlp.ExecutarFeedFoward(resultCamEscondida);
		
		//Executa BackPropagation diretamente no objeto MLP
		this.ExecutarBackPropagation(resultCamEscondida, resultCamSaida, dados);	
	}
	
	private void ExecutarBackPropagation(PesosCalculados[] outputCamEntrada, PesosCalculados[] outputCamSaida, 
			DadosDeEntradaProcessados dados)
	{
		int tamanhoCamEscondida = this.mlp.getTamanhoCamadaEscondida();
		int tamanhoCamSaida = this.mlp.getTamanhoCamadaSaida();
				
		//Equivale ao target pattern, Tk, especificado no livro de Laurene Fausett, "Fundamentals of Neural Networks"
		double resultadoEsperado;

		//Equivalente ao DeltaWjk (que será usado para, mais tarde, atualizar os Wjk, ou seja, os pesos) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks" 
		double[][] correcaoPesoSaida = new double[tamanhoCamSaida][tamanhoCamEscondida];		

		//Equivalente ao DeltaW0k (que será usado para, mais tarde, atualizar os W0k, ou seja, os bias) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks"
		double[] correcaoBiasSaida = new double[tamanhoCamSaida];

		//Inicia correção de pesos na camada de Saida
		for(int index = 0; index < tamanhoCamSaida; index++)
		{
			if(dados.getClasse() == index)
				resultadoEsperado = 1;
			else
				resultadoEsperado = 0;

			//Definindo o gradiente de erro das camadas de saída
			double erro = (resultadoEsperado - outputCamSaida[index].getOutput()) * this.mlp.DerivadaFuncaoBinariaSigmoid(outputCamSaida[index].getSomatorioPeso());			
			this.mlp.getNeuronioCamadaSaida(index).setTermoDeErro(erro);

			//Calculando termo de correção de peso
			for(int j = 0; j < tamanhoCamEscondida ; j++)
				correcaoPesoSaida[index][j] = this.taxaDeAprendizado * this.mlp.getNeuronioCamadaSaida(index).getTermoDeErro() * outputCamEntrada[j].getOutput();

			//Calculando termo de correção de bias
			correcaoBiasSaida[index] = this.taxaDeAprendizado * this.mlp.getNeuronioCamadaSaida(index).getTermoDeErro();			
		}

		//double[][] correcaoPesoEscondida = new double[camadaEscondida.length][camadaEscondida[0].peso.length];

		double[] correcaoBiasEscondida = new double[tamanhoCamEscondida ];

		PesosCalculados[] dadosNeuronio = new PesosCalculados[tamanhoCamEscondida];

		//Inicializando array
		for(int d = 0; d < dadosNeuronio.length; d++)
			dadosNeuronio[d] = new PesosCalculados();

		//Inicia correção de pesos para camada escondida
		for(int j = 0; j < tamanhoCamEscondida ; j++)
		{
			// faz o somatório para cada input de delta
			for(int k = 0; k < tamanhoCamSaida; k++)
				dadosNeuronio[j].setSomatorioPeso(this.mlp.getNeuronioCamadaSaida(k).getTermoDeErro() * this.mlp.getNeuronioCamadaSaida(k).getPeso(j)); ;

				//Calcular Termo de Erro da camada escondida
				double somatorioInputsCamadaEscondida = outputCamEntrada[j].getSomatorioPeso();			
				double erroEscondida = dadosNeuronio[j].getSomatorioPeso() * this.mlp.DerivadaFuncaoBinariaSigmoid(somatorioInputsCamadaEscondida);				
				this.mlp.getNeuronioCamadaEscondida(j).setTermoDeErro(erroEscondida);

				// calcula a correção para cada peso do neurônio ativo
				for(int i = 0; i < tupla.length(); i++)
					correcaoPesoEscondida[j][i] = this.taxaDeAprendizado *errorTerm[j]*tupla.valor(i);

				correcaoBiasEscondida[j] = this.taxaDeAprendizado *errorTerm[j];
		}

		// atualiza pesos e viés na camada de saída
		for(int k = 0; k < tamanhoCamSaida; k++)
		{
			camadaSaida[k].setVies(camadaSaida[k].getVies()+delta_w0K[k]);
			for(int j = 0; j < camadaEscondida.length; j++)
				camadaSaida[k].setPeso(j, delta_wJK[k][j]);
		}


		// atualiza pesos e viés na camada escondida
		for(int j = 0; j < tamanhoCamEscondida; j++)
		{

			camadaEscondida[j].setVies(camadaEscondida[j].getVies()+correcaoBiasEscondida[j]);
			for(int i = 0; i < tupla.length(); i++)
				camadaEscondida[j].setPeso(i, correcaoPesoEscondida[j][i]);
		}	
	}
}
