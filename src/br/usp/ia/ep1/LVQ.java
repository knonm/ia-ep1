package br.usp.ia.ep1;

public class LVQ extends Classificador {

	@Override
	protected RespostaClassificador exec(float[][] dados, boolean ehTreino) {
		RespostaClassificador resposta = new RespostaClassificador();
		
		float menorDist;
		float menorDistAux;
		int menorDistPeso;
		int qtdAcertos = 0;
		int qtdErros = 0;
		
		for(int dadoAtual = 0; dadoAtual < dados.length; dadoAtual++) {
			menorDistPeso = 0;
			menorDist = Float.MAX_VALUE;
			for(int i = super.pesos[0].length-1; i > -1; i--) {
				menorDistAux = distEuclid(dados[dadoAtual], super.pesos[0][i]);
				if(menorDistAux < menorDist) {
					menorDistPeso = i;
					menorDist = menorDistAux;
				}
			}
			
			if(ehTreino) {
				if(this.pesos[0][menorDistPeso][super.pesos[0][menorDistPeso].length-1] == dados[dadoAtual][dados[dadoAtual].length-1]) { // se o neuronio representa a classe do dado
					for(int i = super.pesos[0][menorDistPeso].length-2; i > -1; i--) {
						super.pesos[0][menorDistPeso][i] += super.txAprend*(dados[dadoAtual][i] - this.pesos[0][menorDistPeso][i]);
					}
					qtdAcertos++;
				} else {
					for(int i = super.pesos[0][menorDistPeso].length-2; i > -1; i--) {
						super.pesos[0][menorDistPeso][i] -= super.txAprend*(dados[dadoAtual][i] - this.pesos[0][menorDistPeso][i]);
					}
					qtdErros++;
				}
			} else {
				if(super.pesos[0][menorDistPeso][super.pesos[0][menorDistPeso].length-1] == dados[dadoAtual][dados[dadoAtual].length-1]){
					qtdAcertos++;
				} else {
					qtdErros++;
				}
			}
		}
		
		resposta.setDados(dados);
		resposta.setErroQuadrado(qtdAcertos - qtdErros);
		resposta.setQtdAcertos(qtdAcertos);
		resposta.setQtdErros(qtdErros);
		resposta.setTxAprend(super.txAprend);
		
		return resposta;
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
	
	public LVQ(float[][] dadosTreinamento, float[][] dadosValidacao,
			float[][] dadosTeste, float txAprend, int qtdNeuronios,
			boolean pesosAleatorios) {
		super(dadosTreinamento, dadosValidacao, dadosTeste, txAprend, qtdNeuronios,
				pesosAleatorios);
	}
}