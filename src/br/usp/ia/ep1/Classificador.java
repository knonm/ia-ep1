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
	
	public void imprimePesos() {
		for(int i = 0; i < this.pesos.length; i++) {
			for(int j = 0; j < this.pesos[i].length; j++) {
				for(int k = 0; k < this.pesos[i][j].length; k++) {
					System.out.println("[" + i + "]" + "[" + j + "]" + "[" + k + "]: " + this.pesos[i][j][k]);
				}
			}
		}
	}
	
	private void swapAleatorio(float[][] array1, float[][] array2) {
		float[] swapAux;
		int pos1, pos2;
		boolean ehOk = false;

		Random r = new Random();
		
		while(!ehOk) {
			pos1 = r.nextInt(array1.length);
			pos2 = r.nextInt(array2.length);
			ehOk = array1[pos1][array1[pos1].length-1] == array2[pos2][array2[pos2].length-1];
			if(ehOk) {
				swapAux = array1[pos1];
				array1[pos1] = array2[pos2];
				array2[pos2] = swapAux;
			}
		}
	}

	public void aleatorizaDados(int limiteIteracao) {
		int qtdItera = 0;
		
		while(qtdItera < limiteIteracao) {
			if(((int)Math.random())%2 == 0) {
				swapAleatorio(dadosTreinamento, dadosValidacao);
			}
			qtdItera++;
		}
	}
	
	public RespostaClassificador testar() {
		RespostaClassificador rc;
		
		int qtdAcertos = 0;
		int qtdErros = 0;
		int[][] matrizConfusao = new int[PreProcessamento.valorMaximoClasse+1][PreProcessamento.valorMaximoClasse+1];
		
		for(int i = matrizConfusao.length-1; i > -1; i--) {
			for(int j = matrizConfusao[i].length-1; j > -1; j--) {
				matrizConfusao[i][j] = 0;
			}
		}
		
		for(int ind = 0; ind < 1000; ind++) {
			rc = this.exec(this.dadosTeste, false);
			qtdAcertos += rc.getQtdAcertos();
			qtdErros += rc.getQtdErros();
			for(int i = matrizConfusao.length-1; i > -1; i--) {
				for(int j = matrizConfusao[i].length-1; j > -1; j--) {
					matrizConfusao[i][j] += rc.getMatrizConfusao().getMatrizConfusao()[i][j];
				}
			}
		}
		
		rc = new RespostaClassificador();
		rc.setQtdAcertos(qtdAcertos);
		rc.setQtdErros(qtdErros);
		rc.setMatrizConfusao(new MatrizConfusao(matrizConfusao, 1000));
		
		return rc;
	}
	
	public void initPesos(float[][] dados, int qtdCamadas) {
		DescDados desc;
		float vlrMinDados, vlrMaxDados;
		float vlrClasse;
		
		Random rand = new Random(System.currentTimeMillis());
		
		desc = MN.descDados(dados, dados[0].length-1);
		//vlrMinDados = desc.getMinVlrAtrib();
		//vlrMaxDados = desc.getMaxVlrAtrib();
		vlrMinDados = 0;
		vlrMaxDados = 1;
		
		this.pesos = new float[qtdCamadas][this.qtdNeuronios*desc.getQtdVlrClasses()][desc.getQtdAtribs()];
		
		vlrClasse = desc.getMaxVlrClasse()+1;
		
		for(int i = this.pesos.length-1; i > -1; i--) {
			for(int j = this.pesos[i].length-1; j > -1; j--) {
				for(int k = this.pesos[i][j].length-2; k > -1; k--) {
					if(pesosAleatorios) {
						this.pesos[i][j][k] = (vlrMinDados) + ((vlrMaxDados - vlrMinDados) * rand.nextFloat());
					} else {
						this.pesos[i][j][k] = 0;
					}
				}
				
				if((j+1)%this.qtdNeuronios == 0) {
        			vlrClasse--;
        		}
				
				this.pesos[i][j][this.pesos[i][j].length-1] = vlrClasse;
			}
		}
	}
	
	public void init(int qtdCamadas) {
		float[][][] pesos = null;
		int acertosAnterior = 0;
		int epocasTreina = 0;
		int epocasValida = 0;
		int epocasTotais = 0;
		RespostaClassificador respostaValida;
		
		this.initPesos(this.dadosTeste, qtdCamadas);
		
		while(epocasTreina < this.qtdEpocasTreinamento){
			//System.out.println("Epoca atual: " + epocasTotais + " | Periodos de epocas ("+numEpocasTreina+") des do ultimo aprendizado: " + epocasTreina + " | Tx Aprend: " + this.txAprend + " | Epoca Validacao: " + epocasValida);
			this.exec(this.dadosTreinamento, true);
			
			if(epocasValida == this.qtdEpocasValidacao){
				respostaValida = this.exec(this.dadosValidacao, false);
				if(respostaValida.getQtdAcertos() > acertosAnterior){
					pesos = this.pesos.clone();
					epocasTreina = 0;
					acertosAnterior = respostaValida.getQtdAcertos();
					if(respostaValida.getErroQuadrado() > 0.9F) {
						epocasTreina = this.qtdEpocasTreinamento;
					}
				}else{
					epocasTreina++;
				}
				System.out.println("Epocas passadas: " + epocasTotais + " | Epocas de treinamento apos validacao: " + epocasTreina);
				System.out.println("Quantidade de acertos na validacao: " + acertosAnterior + " | Taxa de aprendizado: " + this.txAprend);
				epocasValida = 0;
				
				this.txAprend *= 0.99F;
			}else epocasValida++;
			
			this.aleatorizaDados(100);
			epocasTotais++;
			
		}
		
		this.epocas = epocasTotais;
		this.pesos = pesos;
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
