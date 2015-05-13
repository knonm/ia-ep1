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
		int maxRandom = Integer.MAX_VALUE - (array1.length < array2.length ? array1.length : array2.length);

		Random r = new Random();
		
		while(!ehOk) {
			pos1 = (r.nextInt(maxRandom) + array1.length)%array1.length;
			pos2 = (r.nextInt(maxRandom) + array2.length)%array2.length;
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
			if(((int)Math.random())%2 == 0) {
				swapAleatorio(dadosTreinamento, dadosTeste);
			}
			if(((int)Math.random())%2 == 0) {
				swapAleatorio(dadosValidacao, dadosTeste);
			}
			qtdItera++;
		}
	}
	
	public RespostaClassificador testar() {
		return this.exec(this.dadosTeste, false);
	}
	
	public void initPesos(float[][] dados, int qtdCamadas) {
		DescDados desc;
		float vlrMinDados, vlrMaxDados;
		float vlrClasse;
		int qtdClasses;
		
		Random rand = new Random(System.currentTimeMillis());
		
		desc = MN.descDados(dados, dados[0].length-1);
		vlrMinDados = desc.getMinVlrAtrib();
		vlrMaxDados = desc.getMaxVlrAtrib();
		
		this.pesos = new float[qtdCamadas][this.qtdNeuronios*desc.getQtdVlrClasses()][desc.getQtdAtribs()];
		
		vlrClasse = desc.getMinVlrClasse();
		qtdClasses = 0;
		
		for(int i = this.pesos.length-1; i > -1; i--) {
			for(int j = this.pesos[i].length-1; j > -1; j--) {
				for(int k = this.pesos[i][j].length-2; k > -1; k--) {
					if(pesosAleatorios) {
						this.pesos[i][j][k] = vlrMinDados + (Math.abs(vlrMaxDados - vlrMinDados) * rand.nextFloat());
					} else {
						this.pesos[i][j][k] = 0;
					}
				}
				
				if(++qtdClasses%(this.qtdNeuronios+1) == 0) {
					vlrClasse++;
					qtdClasses = 0;
				}
				
				this.pesos[i][j][this.pesos[i][j].length-1] = vlrClasse;
			}
		}
	}
	
	public void init(int qtdCamadas, int numEpocasTreino, int numEpocasValida) {
		float[][][] pesos = null;
		float erroQuadradoAnt = Float.MAX_VALUE;
		int epocasTreina = 0;
		int epocasValida = 0;
		RespostaClassificador respostaValida;
		
		this.initPesos(this.dadosTeste, qtdCamadas);
		
		while(epocasValida < numEpocasValida) {
			while(epocasTreina++ < numEpocasTreino) {
				this.exec(this.dadosTreinamento, true);
			}
			epocasTreina = 0;
			
			respostaValida = this.exec(this.dadosValidacao, false);
			
			if(respostaValida.getErroQuadrado() < erroQuadradoAnt) {
				pesos = this.pesos.clone();
				epocasValida = 0;
			} else {
				epocasValida++;
			}
			
			this.txAprend *= 0.99999F;
			
			erroQuadradoAnt = respostaValida.getErroQuadrado();
			
			this.aleatorizaDados(10000);
		}
		
		this.pesos = pesos;
	}
	
	public Classificador(float[][] dadosTreinamento, float[][] dadosValidacao, float[][] dadosTeste,
			float txAprend, int qtdNeuronios, boolean pesosAleatorios) {
		this.dadosTreinamento = dadosTreinamento.clone();
		this.dadosValidacao = dadosValidacao.clone();
		this.dadosTeste = dadosTeste.clone();
		this.txAprend = txAprend;
		this.qtdNeuronios = qtdNeuronios;
		this.pesosAleatorios = pesosAleatorios;
	}
}