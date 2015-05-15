package br.usp.ia.ep1.MLP;

public class TreinamentoMLP 
{
	private DadosDeEntradaProcessados[] entrada;
	private EstruturaMLP mlp;
	private double taxaDeAprendizado;
		
	/*public TreinamentoMLP(int qtdNeuroniosCamadaEscondida, int qtdeNeuroniosCamadaSaida, DadosDeEntradaProcessados[] dadosEntrada, double taxaAprendizado, boolean pesosAleatorios)
	{
		this.mlp = new EstruturaMLP(qtdNeuroniosCamadaEscondida, qtdeNeuroniosCamadaSaida, pesosAleatorios);		
		this.entrada = new DadosDeEntradaProcessados[dadosEntrada.length];
		this.taxaDeAprendizado = taxaAprendizado;
		
		this.PopularDadosDeEntrada(dadosEntrada);			
	}*/
	
	public TreinamentoMLP(EstruturaMLP mlp, DadosDeEntradaProcessados[] dadosEntrada, double taxaAprendizado, boolean inicializacaoAleatorio)
	{
		this.mlp = mlp;		
		this.entrada = new DadosDeEntradaProcessados[dadosEntrada.length];
		this.taxaDeAprendizado = taxaAprendizado;
		
		this.PopularDadosDeEntrada(dadosEntrada);			
	}
	
	private void PopularDadosDeEntrada(DadosDeEntradaProcessados[] dadosEntrada)
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
	
	//M�todo que executa o treino da rede para cada epoca corrente no m�todo Treinar()
	private void executarTreino(DadosDeEntradaProcessados dados)
	{
		PesosCalculados[] outputCamEscondida = new PesosCalculados[this.mlp.getTamanhoCamadaEscondida()];
		PesosCalculados[] outputCamSaida = new PesosCalculados[this.mlp.getTamanhoCamadaSaida()];
		
		//Executa FeedFoward para cada neur�nio da camada escondida (por isso, metodo foi guardado dentro do neur�nio)
		for(int neuronioEscondido = 0; neuronioEscondido < this.mlp.getTamanhoCamadaEscondida(); neuronioEscondido++)
			outputCamEscondida[neuronioEscondido] = this.mlp.ExecutarFeedFoward(dados);
		
		
		//Executa FeedFoward para cada neur�nio da camada de sa�da (por isso, metodo foi guardado dentro do neur�nio)
		for(int neuronioSaida = 0; neuronioSaida < this.mlp.getTamanhoCamadaSaida(); neuronioSaida++)
			outputCamSaida[neuronioSaida] = this.mlp.ExecutarFeedFoward(outputCamEscondida);
		
		
		this.executarBackPropagation(outputCamEscondida, outputCamSaida, dados);	
	}
	
	private void executarBackPropagation(PesosCalculados[] outputCamEntrada, PesosCalculados[] outputCamSaida, DadosDeEntradaProcessados dados)
	{
		int tamanhoCamEscondida = this.mlp.getTamanhoCamadaEscondida();
		int tamanhoCamSaida = this.mlp.getTamanhoCamadaSaida();
				
		//Equivale ao target pattern, Tk, especificado no livro de Laurene Fausett, "Fundamentals of Neural Networks"
		double resultadoEsperado;

		//Equivalente ao DeltaWjk (que ser� usado para, mais tarde, atualizar os Wjk, ou seja, os pesos) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks" 
		double[][] correcaoPesoSaida = new double[tamanhoCamSaida][tamanhoCamEscondida];		

		//Equivalente ao DeltaW0k (que ser� usado para, mais tarde, atualizar os W0k, ou seja, os bias) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks"
		double[] correcaoBiasSaida = new double[tamanhoCamSaida];

		//Inicia corre��o de pesos na camada de Saida
		for(int index = 0; index < tamanhoCamSaida; index++)
		{
			if(dados.getClasse() == index)
				resultadoEsperado = 1;
			else
				resultadoEsperado = 0;

			//Definindo o gradiente de erro das camadas de sa�da
			double erro = (resultadoEsperado - outputCamSaida[index].getOutput()) * this.mlp.DerivadaFuncaoBinariaSigmoid(outputCamSaida[index].getSomatorioPeso());			
			this.mlp.getNeuronioCamadaSaida(index).setTermoDeErro(erro);

			//Calculando termo de corre��o de peso
			for(int j = 0; j < tamanhoCamEscondida ; j++)
				correcaoPesoSaida[index][j] = this.taxaDeAprendizado * this.mlp.getNeuronioCamadaSaida(index).getTermoDeErro() * outputCamEntrada[j].getOutput();

			//Calculando termo de corre��o de bias
			correcaoBiasSaida[index] = this.taxaDeAprendizado * this.mlp.getNeuronioCamadaSaida(index).getTermoDeErro();			
		}
		
		//INICIA BACKPROPAGATION PARA CAMADA ESCONDIDA

		double[][] correcaoPesoEscondida = new double[this.mlp.getTamanhoCamadaEscondida()][this.mlp.getTamanhoCamadaEntrada()];
		double[] correcaoBiasEscondida = new double[tamanhoCamEscondida ];

		PesosCalculados[] dadosNeuronio = new PesosCalculados[tamanhoCamEscondida];

		//Inicializando array
		for(int d = 0; d < dadosNeuronio.length; d++)
			dadosNeuronio[d] = new PesosCalculados();

		//Inicia corre��o de pesos para camada escondida
		for(int j = 0; j < tamanhoCamEscondida ; j++)
		{
			// faz o somat�rio para cada input de delta
			for(int k = 0; k < tamanhoCamSaida; k++)
				dadosNeuronio[j].setSomatorioPeso(this.mlp.getNeuronioCamadaSaida(k).getTermoDeErro() * this.mlp.getNeuronioCamadaSaida(k).getPeso(j)); ;

				//Calcular Termo de Erro da camada escondida
				double somatorioInputsCamadaEscondida = outputCamEntrada[j].getSomatorioPeso();			
				double erroEscondida = dadosNeuronio[j].getSomatorioPeso() * this.mlp.DerivadaFuncaoBinariaSigmoid(somatorioInputsCamadaEscondida);				
				this.mlp.getNeuronioCamadaEscondida(j).setTermoDeErro(erroEscondida);

				// calcula a corre��o para cada peso do neur�nio ativo
				for(int i = 0; i < dados.QuantidadeDadosEntrada(); i++)
					correcaoPesoEscondida[j][i] = this.taxaDeAprendizado * this.mlp.getNeuronioCamadaEscondida(j).getTermoDeErro() * dados.getDadoEntrada(i);

				correcaoBiasEscondida[j] = this.taxaDeAprendizado * this.mlp.getNeuronioCamadaEscondida(j).getTermoDeErro();
		}

		// atualiza pesos e vi�s na camada de sa�da
		for(int k = 0; k < tamanhoCamSaida; k++)
		{
			this.mlp.getNeuronioCamadaSaida(k).setBias(this.mlp.getNeuronioCamadaSaida(k).getBias() + correcaoBiasSaida[k]);
			
			for(int j = 0; j < tamanhoCamEscondida; j++)
				this.mlp.getNeuronioCamadaSaida(k).setPeso(j, correcaoPesoSaida[k][j]);
		}


		// atualiza pesos e vi�s na camada escondida
		for(int j = 0; j < tamanhoCamEscondida; j++)
		{
			this.mlp.getNeuronioCamadaEscondida(j).setBias(this.mlp.getNeuronioCamadaEscondida(j).getBias() + correcaoBiasEscondida[j]);
			
			for(int i = 0; i < dados.QuantidadeDadosEntrada(); i++)
				this.mlp.getNeuronioCamadaEscondida(j).setPeso(i, correcaoPesoEscondida[j][i]);
		}	
	}
}
