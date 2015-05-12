package br.usp.ia.ep1;

import br.usp.ia.ep1.utils.MN;

public class LVQ {

	private float taxaAprendizado;
	private float[][] neuronios;
	private float[][] dadosReais;
	private int qtdDeErasTotais = 0;
	
	public void printStatus(){
		System.out.println("Taxa de Aprendizado Final: " + taxaAprendizado);
		System.out.println("Valor Final dos Neuronios e suas Classes: ");
		for( int i = 0; i < neuronios.length; i++){
		//	for ( int j = 0; j < neuronios[i].length-1; j++) System.out.println(i + " " + j + ": " + neuronios[i][j] + " | Classe: " + neuronios[i][neuronios[i].length-1]);
			System.out.println(i + " " + " | Classe: " + neuronios[i][neuronios[i].length-1]);
		}
	System.out.println("Quantidade de Eras passadas: " + qtdDeErasTotais);
	}
	
	public float distEuclid(float[] v1, float[] v2) {
		float dist = -1;
		if(v1.length == v2.length) {
			dist = 0;
			for(int i = 0; i < v1.length-2; i++) { // -2 para desconciderar o valor da classe no calculo ( a classe eh o ultimo valor)
				dist += Math.pow(v1[i] - v2[i], 2);
			}
			dist = (float) Math.sqrt(dist);
		}
		return dist;
	}
	
	public boolean holdout(float[][] neuroniosTeste, float[][] valores, int qtdAcertosPrevios){
		float menorDist;
		float menorDistAux;
		int menorDistPeso;
		int qtdAcertosAtuais = 0;
		
		for(int dadoAtual = 0; dadoAtual < valores.length; dadoAtual++) {
			menorDistPeso = 0;
			menorDist = Float.MAX_VALUE;
			for(int i = neuronios.length-1; i > -1; i--) {
				menorDistAux = distEuclid(valores[dadoAtual], neuroniosTeste[i]);
				if(menorDistAux < menorDist) {
					menorDistPeso = i;
					menorDist = menorDistAux;
				}
			}
			
			//System.out.println("Dado atual: " + dadoAtual);
			//System.out.println("Menor Distancia: " + menorDist);
			//System.out.println("Neuronio representante: " + menorDistPeso);
			
			System.out.println("Comparando no Holdout " + neuroniosTeste[menorDistPeso][neuroniosTeste[menorDistPeso].length-1] + " com " + valores[dadoAtual][valores[dadoAtual].length-1]);
			if(neuroniosTeste[menorDistPeso][neuroniosTeste[menorDistPeso].length-1] == valores[dadoAtual][valores[dadoAtual].length-1]){
				qtdAcertosAtuais++;
				//System.out.println("Acertou! =)");
			} //else System.out.println("Errou! =(");
			
		}
		
		if(qtdAcertosAtuais < qtdAcertosPrevios){
			return false;
		}else{
			return true;
		}		
	}
	
	public void exec(float[][] dados, boolean treina) {
		float txAprend = this.taxaAprendizado;
		float menorDist;
		float menorDistAux;
		int menorDistPeso;
		int qtdAcertos = 0;
		int ciclosSemMelhorar = 0;
		float[][] neuroniosaux = neuronios;
		int qtdDeEras = 0;
		
		while(ciclosSemMelhorar <= 1000) {
			for(int dadoAtual = 0; dadoAtual < dados.length; dadoAtual++) {
				menorDistPeso = 0;
				menorDist = Float.MAX_VALUE;
				for(int i = neuronios.length-1; i > -1; i--) {
					menorDistAux = distEuclid(dados[dadoAtual], neuroniosaux[i]);
					if(menorDistAux < menorDist) {
						menorDistPeso = i;
						menorDist = menorDistAux;
					}
				}
				
				//System.out.println("Dado atual: " + dadoAtual);
				//System.out.println("Menor Distancia: " + menorDist);
				//System.out.println("Neuronio representante: " + menorDistPeso);
				
				
				if(treina) {
					System.out.println("Comparando " + neuroniosaux[menorDistPeso][neuroniosaux[menorDistPeso].length-1] + " com " + dados[dadoAtual][dados[dadoAtual].length-1]);
					if(neuroniosaux[menorDistPeso][neuroniosaux[menorDistPeso].length-1] == dados[dadoAtual][dados[dadoAtual].length-1]){ // se o neuronio representa a classe do dado
						for ( int i = 0; i < neuroniosaux[menorDistPeso].length-1; i++){
							neuroniosaux[menorDistPeso][i] = neuroniosaux[menorDistPeso][i] + txAprend*(dados[dadoAtual][i] - neuroniosaux[menorDistPeso][i]);
						}
						qtdAcertos++;
					}else{
						for ( int i = 0; i < neuroniosaux[menorDistPeso].length-1; i++){
							neuroniosaux[menorDistPeso][i] = neuroniosaux[menorDistPeso][i] - txAprend*(dados[dadoAtual][i] - neuroniosaux[menorDistPeso][i]);
						}
					}					
				} else {
					System.out.println("Comparando " + neuroniosaux[menorDistPeso][neuroniosaux[menorDistPeso].length-1] + " com " + dados[dadoAtual][dados[dadoAtual].length-1]);
					if(neuroniosaux[menorDistPeso][neuroniosaux[menorDistPeso].length-1] == dados[dadoAtual][dados[dadoAtual].length-1]){
						qtdAcertos++;
						System.out.println("Acertou! =)");
					} else System.out.println("Errou! =(");
				}
			}
			if(treina) {
			//	if(holdout(neuroniosaux, dados, qtdAcertos)){
			//		neuronios = neuroniosaux;
					if(qtdDeEras == 10){
						txAprend = txAprend * 0.9F;
						qtdDeEras = 0;
					}
			//		qtdAcertos = 0;
			//	}else{
			//		ciclosSemMelhorar++;
			//		qtdAcertos = 0;
			//	}
				ciclosSemMelhorar++;
			} else {
				ciclosSemMelhorar = 999999999;
				//ciclosSemMelhorar++;
				System.out.println("Quantidade de Acertos: " + qtdAcertos);
			}
			qtdDeErasTotais++;
			qtdDeEras++;
			System.out.println("QUANTIDADE DE EPOCAS TOTAIS: " + qtdDeErasTotais);
		}
		taxaAprendizado = txAprend;
	}
	
	public LVQ(float[][] dadosCrus, float taxaAprendizado, int qtdNeuronios, boolean pesosAleatorios) {
		this.taxaAprendizado = taxaAprendizado;
		this.dadosReais = dadosCrus;
		this.neuronios = new float[(MN.valorMaximoClasse(dadosCrus)+1)][dadosCrus[0].length];
		
		int vlrClasse = MN.valorMinimoClasse(dadosCrus);

		if(pesosAleatorios) {
			for(int i = 0; i < neuronios.length; i++) 
			{
				for(int j = 0; j < neuronios[i].length; j++) 
				{
					if(j == neuronios[i].length-1){
						this.neuronios[i][j] = vlrClasse;
					} else this.neuronios[i][j] = (((int)Math.random())%1000)/1000;
				}
				vlrClasse++;
			}
		} else {
			for(int i = 0; i < neuronios.length; i++) 
			{
				for(int j = 0; j < neuronios[i].length; j++) 
				{
					if(j == neuronios[i].length-1){
						this.neuronios[i][j] = vlrClasse;
					} else this.neuronios[i][j] = 0;
				}
				vlrClasse++;
			}
		}
	}
}
