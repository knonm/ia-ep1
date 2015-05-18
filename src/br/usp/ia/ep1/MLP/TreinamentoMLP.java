package br.usp.ia.ep1.MLP;

import br.usp.ia.ep1.RespostaClassificador;

public class TreinamentoMLP 
{
	private DadosDeEntradaProcessados[] entrada;
	private EstruturaMLP mlp;
	private double taxaDeAprendizado;
	private EstruturaMLP backupMLP;
	
	private boolean redeEstaMelhorando = true;
		
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
	
	public void Treinar(int quantidadeTreinos, int maximoFracassos, DadosDeTeste[] teste, DadosDeTeste[] validacao)
	{
		RespostaClassificador rc;
		int epocasExecutadas = 0;
		int fracassosAteAqui = 0;
		double errosAtuais = 0;
		double melhorResultado = Double.MAX_VALUE;
		
		int paradaObrigatorio = 0;
		
		System.out.println("Iniciando Treinamento da MLP...");
		System.out.println();
		
		while(redeEstaMelhorando)
		{
			for(int epoca = 0; epoca < quantidadeTreinos; epoca++)
			{
				for(DadosDeEntradaProcessados dado: entrada)
					this.executarTreino(dado);	
				
				epocasExecutadas++;
				
				System.out.println("Epocas passadas: " + epocasExecutadas + " | Epocas de treinamento apos validacao: " + epoca);
				System.out.println("Taxa de erro quadrado: " + errosTreinamentoDaRedeAtual(teste).getErroQuadrado() + " | Taxa de aprendizado: " + this.taxaDeAprendizado);
				
				rc = errosTreinamentoDaRedeAtual(validacao);
				errosAtuais = rc.getErroQuadrado();
				
				if(errosAtuais < melhorResultado)
				{
					melhorResultado = errosAtuais;
					backupMLP = this.mlp.ClonarRede(mlp);
					fracassosAteAqui = 0;
					this.taxaDeAprendizado *= 0.999F;
				}
				else
					fracassosAteAqui++;
				
				if(fracassosAteAqui >= maximoFracassos ||
						errosAtuais - rc.getErroQuadrado() < 0.2)
					redeEstaMelhorando = false;

				paradaObrigatorio++;
				
 
				
				if(paradaObrigatorio >= 20)
					redeEstaMelhorando = false;
				
			}
		}
		mlp.setQtdEpocaTreino(epocasExecutadas);
	}
	
	private RespostaClassificador errosTreinamentoDaRedeAtual(DadosDeTeste[] entradas)
	{
		int erros = 0;
		
		RespostaClassificador rc = this.mlp.ExecutarRede(entradas);
		
		for(DadosDeTeste entrada : entradas)
		{
			if(!entrada.ClassePreditaEhIgualAClasseEncontrada())
				erros++;
		}
		
		rc.setQtdErros(erros);
		return rc;
	}
	
	//Método que executa o treino da rede para cada epoca corrente no método Treinar()
	private void executarTreino(DadosDeEntradaProcessados dados)
	{
		PesosCalculados[] outputCamEscondida = new PesosCalculados[this.mlp.getTamanhoCamadaEscondida()];
		PesosCalculados[] outputCamSaida = new PesosCalculados[this.mlp.getTamanhoCamadaSaida()];
		
		//Executa FeedFoward para cada neurônio da camada escondida (por isso, metodo foi guardado dentro do neurônio)
		for(int neuronioEscondido = 0; neuronioEscondido < this.mlp.getTamanhoCamadaEscondida(); neuronioEscondido++)
			outputCamEscondida[neuronioEscondido] = this.mlp.ExecutarFeedFowardCamadaEscondida(dados, neuronioEscondido);
		
		
		//Executa FeedFoward para cada neurônio da camada de saída (por isso, metodo foi guardado dentro do neurônio)
		for(int neuronioSaida = 0; neuronioSaida < this.mlp.getTamanhoCamadaSaida(); neuronioSaida++)
			outputCamSaida[neuronioSaida] = this.mlp.ExecutarFeedFowarCamadaSaida(outputCamEscondida, neuronioSaida);
		
		this.executarBackPropagation(outputCamEscondida, outputCamSaida, dados);	
	}
	
	private void executarBackPropagation(PesosCalculados[] outputCamEntrada, PesosCalculados[] outputCamSaida, DadosDeEntradaProcessados dados)
	{
		int tamanhoCamEscondida = this.mlp.getTamanhoCamadaEscondida();
		int tamanhoCamSaida = this.mlp.getTamanhoCamadaSaida();
		
		//Equivale ao target pattern, Tk, especificado no livro de Laurene Fausett, "Fundamentals of Neural Networks"
		double resultadoEsperado;

		//Equivalente ao DeltaWjk (que será usado para, mais tarde, atualizar os Wjk, ou seja, os pesos) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks" 
		double[][] correcaoPesoSaida = new double[tamanhoCamSaida][tamanhoCamEscondida];		

		//Equivalente ao DeltaW0k (que será usado para, mais tarde, atualizar os W0k, ou seja, os bias) definido no livro de Laurene Fauset, "Fundamentals of Neural Networks"
		double[] correcaoBiasSaida = new double[tamanhoCamSaida];

		//Inicia correcao de pesos na camada de Saida
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
		
		//INICIA BACKPROPAGATION PARA CAMADA ESCONDIDA

		double[][] correcaoPesoEscondida = new double[this.mlp.getTamanhoCamadaEscondida()][this.mlp.getTamanhoCamadaEntrada()];
		double[] correcaoBiasEscondida = new double[tamanhoCamEscondida ];

		PesosCalculados[] dadosNeuronio = new PesosCalculados[tamanhoCamEscondida];

		//Inicializando array
		for(int d = 0; d < dadosNeuronio.length; d++)
			dadosNeuronio[d] = new PesosCalculados();

		//Inicia correcao de pesos para camada escondida
		for(int j = 0; j < tamanhoCamEscondida ; j++)
		{
			// faz o somatório para cada input de delta
			for(int k = 0; k < tamanhoCamSaida; k++)
				dadosNeuronio[j].setSomatorioPeso(this.mlp.getNeuronioCamadaSaida(k).getTermoDeErro() * this.mlp.getNeuronioCamadaSaida(k).getPeso(j)); ;

				//Calcular Termo de Erro da camada escondida
				double somatorioInputsCamadaEscondida = outputCamEntrada[j].getSomatorioPeso();			
				double erroEscondida = dadosNeuronio[j].getSomatorioPeso() * this.mlp.DerivadaFuncaoBinariaSigmoid(somatorioInputsCamadaEscondida);				
				this.mlp.getNeuronioCamadaEscondida(j).setTermoDeErro(erroEscondida);

				// calcula a correcao para cada peso do neuronio ativo
				for(int i = 0; i < dados.QuantidadeDadosEntrada(); i++)
					correcaoPesoEscondida[j][i] += this.taxaDeAprendizado * this.mlp.getNeuronioCamadaEscondida(j).getTermoDeErro() * dados.getDadoEntrada(i);

				correcaoBiasEscondida[j] = this.taxaDeAprendizado * this.mlp.getNeuronioCamadaEscondida(j).getTermoDeErro();
		}

		// atualiza pesos e bias na camada de saida
		for(int k = 0; k < tamanhoCamSaida; k++)
		{
			this.mlp.getNeuronioCamadaSaida(k).setBias(this.mlp.getNeuronioCamadaSaida(k).getBias() + correcaoBiasSaida[k]);
			
			for(int j = 0; j < tamanhoCamEscondida; j++)
				this.mlp.getNeuronioCamadaSaida(k).setPeso(j, this.mlp.getNeuronioCamadaSaida(k).getPeso(j) + correcaoPesoSaida[k][j]);
		}


		// atualiza pesos e bias na camada escondida
		for(int j = 0; j < tamanhoCamEscondida; j++)
		{
			this.mlp.getNeuronioCamadaEscondida(j).setBias(this.mlp.getNeuronioCamadaEscondida(j).getBias() + correcaoBiasEscondida[j]);
			
			for(int i = 0; i < dados.QuantidadeDadosEntrada(); i++)
				this.mlp.getNeuronioCamadaEscondida(j).setPeso(i, this.mlp.getNeuronioCamadaEscondida(j).getPeso(i) + correcaoPesoEscondida[j][i]);
		}
	}
}
