package br.usp.ia.ep1;

public class LVQ {

	private float txAprend;
	private float[][] neuronios;
	
	public float distEuclid(float[] v1, float[] v2) {
		float dist = -1;
		if(v1.length == v2.length) {
			dist = 0;
			for(int i = v1.length-1; i > -1; i--) {
				dist += Math.pow(v1[i] - v2[i], 2);
			}
		}
		return dist;
	}
	
	public void exec(float[][] dados, boolean treina) {
		float txAprend = this.txAprend;
		float condParada = txAprend*((float)Math.pow(0.9F, 100));
		float menorDist;
		float menorDistAux;
		int menorDistPeso;
		float novoPeso;
		int qtdAcertos = 0;
		
		while(txAprend > condParada) {
			for(float[] linha : dados) {
				menorDistPeso = 0;
				menorDist = Float.MAX_VALUE;
				for(int i = neuronios.length-1; i > -1; i--) {
					menorDistAux = distEuclid(linha, neuronios[i]);
					if(menorDistAux < menorDist) {
						menorDistPeso = i;
						menorDist = menorDistAux;
					}
				}
				if(treina) {
					novoPeso = txAprend*menorDist;
					novoPeso *= neuronios[menorDistPeso][neuronios.length-1] == linha[neuronios.length-1] ?
							1 : -1;
					for(int j = neuronios[menorDistPeso].length-2; j > -1; j--) {
						neuronios[menorDistPeso][j] += novoPeso;
					}
				} else {
					qtdAcertos++;
				}
			}
			if(treina) {
				txAprend = condParada - 1F;
				System.out.println(qtdAcertos);
			} else {
				txAprend *= 0.9;
			}
		}
	}
	
	public LVQ(float txAprend, int qtdNeuronios, boolean pesosAleatorios) {
		this.txAprend = txAprend;
		this.neuronios = new float[qtdNeuronios*(PreProcessamento.MAX_VLR_CLASSE+1)][PreProcessamento.NUM_ATRIBS];
		
		int vlrClasse = PreProcessamento.MIN_VLR_CLASSE;
		int qtdClasses = 0;
		if(pesosAleatorios) {
			for(int i = this.neuronios.length-1; i > -1; i--) {
				for(int j = this.neuronios[i].length-2; j > -1; j--) {
					this.neuronios[i][j] = (((int)Math.random())%1000)/1000;
				}
				if(qtdClasses++ < qtdNeuronios) {
					this.neuronios[i][this.neuronios[i].length-1] = vlrClasse;
				} else {
					vlrClasse++;
					qtdClasses = 0;
				}
			}
		} else {
			for(int i = this.neuronios.length-1; i > -1; i--) {
				for(int j = this.neuronios[i].length-2; j > -1; j--) {
					this.neuronios[i][j] = 0;
				}
				if(qtdClasses++ < qtdNeuronios) {
					this.neuronios[i][this.neuronios[i].length-1] = vlrClasse;
				} else {
					vlrClasse++;
					qtdClasses = 0;
				}
			}
		}
	}
}
