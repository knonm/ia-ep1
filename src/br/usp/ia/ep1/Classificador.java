package br.usp.ia.ep1;

import java.util.Random;

import br.usp.ia.ep1.utils.MN;

public abstract class Classificador {

	protected float[][][] pesos;
	
	protected float[][] dadosTreinamento;
	protected float[][] dadosValidacao;
	protected float[][] dadosTeste;

	protected float txAprend;
	
	protected int qtdNeuronios;
	protected boolean pesosAleatorios;
	protected int qtdEpocasTreinamento;
	protected int qtdEpocasValidacao;
	
	protected int epocas;
	
	protected abstract RespostaClassificador exec(float[][] dados, boolean ehTreino);
	
	public void imprimePesos() { // Método para imprimir os pesos dos neurônios na tela
		for(int i = 0; i < this.pesos.length; i++) {
			for(int j = 0; j < this.pesos[i].length; j++) {
				for(int k = 0; k < this.pesos[i][j].length; k++) {
					System.out.println("[" + i + "]" + "[" + j + "]" + "[" + k + "]: " + this.pesos[i][j][k]);
				}
			}
		}
	}
	
	/*  Método auxiliar do "aleatorizaDados" que troca dados com outros arquivos, sendo que os arquivos vem como
	parâmetros */
	private void swapAleatorio(float[][] array1, float[][] array2) {
		float[] swapAux;
		int pos1, pos2;
		boolean ehOk = false;

		Random r = new Random();
		
		while(!ehOk) {
			pos1 = r.nextInt(array1.length);
			pos2 = r.nextInt(array2.length);
			ehOk = array1[pos1][array1[pos1].length-1] == array2[pos2][array2[pos2].length-1]; // Ve se está trocando com dados da mesma classe, mantendo a proporção.
			if(ehOk) {
				swapAux = array1[pos1];
				array1[pos1] = array2[pos2];
				array2[pos2] = swapAux;
			}
		}
	}

	/* Método que faz uma quantidade determinada por parâmetros de trocas */
	public void aleatorizaDados(int limiteIteracao) {
		int qtdItera = 0;
		
		while(qtdItera < limiteIteracao) {
			if(((int)Math.random())%2 == 0) {
				swapAleatorio(dadosTreinamento, dadosValidacao); // Chama o método auxiliar.
			}
			qtdItera++;
		}
	}
	
	/* Método que chama a execução da rede */
	public RespostaClassificador testar() {
		return this.exec(this.dadosTeste, false);
	}
	
	/* Método que inicia os pesos nos neurônios, sendo que eles (se escolhidos como aleatório), 
	ficam entre o range determinado pelas variaveis vlrMinDados e vlrMaxDados*/
	public void initPesos(float[][] dados, int qtdCamadas) {
		DescDados desc;
		float vlrMinDados, vlrMaxDados;
		float vlrClasse;
		
		Random rand = new Random(System.currentTimeMillis());
		
		desc = MN.descDados(dados, dados[0].length-1);
		//vlrMinDados = desc.getMinVlrAtrib();
		//vlrMaxDados = desc.getMaxVlrAtrib();
		vlrMinDados = 0F;
		vlrMaxDados = 0.3F;
		
		this.pesos = new float[qtdCamadas][this.qtdNeuronios*desc.getQtdVlrClasses()][desc.getQtdAtribs()]; // Instancia o array dos pesos
		
		vlrClasse = desc.getMaxVlrClasse()+1;
		
		for(int i = this.pesos.length-1; i > -1; i--) {
			for(int j = this.pesos[i].length-1; j > -1; j--) {
				for(int k = this.pesos[i][j].length-2; k > -1; k--) {
					if(pesosAleatorios) {
						this.pesos[i][j][k] = (vlrMinDados) + ((vlrMaxDados - vlrMinDados) * rand.nextFloat()); // Valor aleatório determinado com uma função.
					} else {
						this.pesos[i][j][k] = 0; // Zeros
					}
				}
				
				if((j+1)%this.qtdNeuronios == 0) {
        			vlrClasse--; // Vai para a próxima classe.
        		}
				
				this.pesos[i][j][this.pesos[i][j].length-1] = vlrClasse;
			}
		}
	}
	
	/* Método princial que roda a rede em si, a heurística escolhida foi a seguinte: Fique rodando o treinamento
	indeterminadamente, quando a rede tiver treinado por "this.qtdEpocasValidacao", parâmetro passado na instanciação
	da rede, ele faz a alidação do aprendizado. Ele agora checa se as quantidades de acerto desta NOVA rede é melhor
	do que a da rede anterior, SE SIM ele armazena os novos pesos em uma variavel que ele pode acessar qualquer momento
	(e retornar a melhor rede no final), SE NÃO ele conta um no contador de épocas para parar o treinamento 
	"this.qtdEpocasTreinamento" e quando ele NÃO melhorar durante as determinadas épocas ele retorna a melhor rede 
	encontrada, desta maneira os treinamentos ficam sendo controlados pela melhoria da rede.*/
	public void init(int qtdCamadas) {
		float[][][] pesos = null;
		int acertosAnterior = 0;
		int epocasTreina = 0;
		int epocasValida = 0;
		int epocasTotais = 1;
		RespostaClassificador respostaValida;
		
		this.initPesos(this.dadosTeste, qtdCamadas);
		
		while(epocasTreina < this.qtdEpocasTreinamento){ // Verificação se chegou na quantidade máxima de épocas com fracaço
			this.exec(this.dadosTreinamento, true); // TREINA A REDE.
			
			if(epocasValida == this.qtdEpocasValidacao){ // Se está a X epocas sem validar, ele valida agora.
				respostaValida = this.exec(this.dadosValidacao, false); // TESTA A REDE.
				
				epocasTotais--;
				System.out.println("Epocas passadas: " + epocasTotais + " | Epocas de treinamento apos validacao: " + epocasTreina);
				System.out.println("Quantidade de acertos na validacao: " + acertosAnterior + " | Taxa de aprendizado: " + this.txAprend);
				
				if(respostaValida.getQtdAcertos() > acertosAnterior){ // Checa se a rede melhorou.
					pesos = this.pesos.clone(); // Clona a rede melhor para uma rede local.
					epocasTreina = 0; // Reseta o contador de épocas máximas de treino.
					acertosAnterior = respostaValida.getQtdAcertos();
					if(respostaValida.getErroQuadrado() > 0.9F) {
						epocasTreina = this.qtdEpocasTreinamento;
					}
				}else{
					epocasTreina++; // Se não melhorou aumenta o contador de épocas sem melhorar.
				}
			
				epocasValida = 0; // Reseta o contador de épocas para validação.
				
				this.txAprend *= 0.99F;
			}else epocasValida++; // Aumenta o contador de épocas sem validação.
			
			this.aleatorizaDados(100);
			epocasTotais++; // Aumenta o contador de épocas totais passadas.
			
		}
		
		this.epocas = epocasTotais;
		this.pesos = pesos;
	}

	public float[][][] getPesos() {
		return pesos;
	}

	public float getTxAprend() {
		return txAprend;
	}

	public int getEpocas() {
		return epocas;
	}

	public Classificador(float[][] dadosTreinamento, float[][] dadosValidacao, float[][] dadosTeste,
			float txAprend, int qtdNeuronios, boolean pesosAleatorios, int qtdEpocasTreinamento,
			int qtdEpocasValidacao) {
		this.dadosTreinamento = dadosTreinamento.clone();
		this.dadosValidacao = dadosValidacao.clone();
		this.dadosTeste = dadosTeste.clone();
		this.txAprend = txAprend;
		this.qtdNeuronios = qtdNeuronios;
		this.pesosAleatorios = pesosAleatorios;
		this.qtdEpocasTreinamento = qtdEpocasTreinamento;
		this.qtdEpocasValidacao = qtdEpocasValidacao;
	}
}
