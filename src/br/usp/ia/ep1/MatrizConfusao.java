package br.usp.ia.ep1;

public class MatrizConfusao {

	private int[][] matrizConfusao;
	private float[] taxaVerdadeiros; // taxa de verdadeiros positivos (TPR) e taxa de verdadeiros negativos (SPC)
	private float[] taxaFalsos; // taxa de falsos positivos (FPR)
	private float[] precisao;
	private float[] taxaFalsasDescobertas;
	private Integer acuracia;
	private Integer erro;
	
	private int qtdExec;
	
	public float getTaxaVerdadeiros(int classe) {
		if(this.taxaVerdadeiros == null) {
			this.taxaVerdadeiros = new float[this.matrizConfusao.length];
			
			int falso;
			for(int classeReal = this.taxaVerdadeiros.length-1; classeReal > -1; classeReal--) {
				falso = 0;
				for(int classePredita = this.matrizConfusao[classeReal].length-1; classePredita > -1; classePredita--) {
					if(classePredita != classeReal) {
						falso += this.matrizConfusao[classeReal][classePredita];
					}
				}
				this.taxaVerdadeiros[classeReal] = 
						this.matrizConfusao[classeReal][classeReal] / (this.matrizConfusao[classeReal][classeReal] + falso);
			}
		}
		return this.taxaVerdadeiros[classe];
	}
	
	public float getTaxaFalsos(int classe) {
		if(this.taxaFalsos == null) {
			this.taxaFalsos = new float[this.matrizConfusao.length];
			
			int falso;
			for(int classeReal = this.taxaFalsos.length-1; classeReal > -1; classeReal--) {
				falso = 0;
				for(int classePredita = this.matrizConfusao[classeReal].length-1; classePredita > -1; classePredita--) {
					if(classePredita != classeReal) {
						falso += this.matrizConfusao[classeReal][classePredita];
					}
				}
				this.taxaFalsos[classeReal] = 
						falso / (this.matrizConfusao[classeReal][classeReal] + falso);
			}
		}
		return this.taxaFalsos[classe];
	}
	
	public float getPrecisao(int classe) {
		if(this.precisao == null) {
			this.precisao = new float[this.matrizConfusao.length];
			
			int falso;
			for(int classePredita = this.matrizConfusao[0].length-1; classePredita > -1; classePredita--) {
				falso = 0;
				for(int classeReal = this.precisao.length-1; classeReal > -1; classeReal--) {
					if(classePredita != classeReal) {
						falso += this.matrizConfusao[classeReal][classePredita];
					}
				}
				this.precisao[classePredita] = 
						this.matrizConfusao[classePredita][classePredita] / (this.matrizConfusao[classePredita][classePredita] + falso);
			}
		}
		return this.precisao[classe];
	}
	
	public float getTaxaFalsasDescobertas(int classe) {
		if(this.taxaFalsasDescobertas == null) {
			this.taxaFalsasDescobertas = new float[this.matrizConfusao.length];
			
			int falso;
			for(int classePredita = this.matrizConfusao[0].length-1; classePredita > -1; classePredita--) {
				falso = 0;
				for(int classeReal = this.taxaFalsasDescobertas.length-1; classeReal > -1; classeReal--) {
					if(classePredita != classeReal) {
						falso += this.matrizConfusao[classeReal][classePredita];
					}
				}
				this.taxaFalsasDescobertas[classePredita] = 
						falso / (this.matrizConfusao[classePredita][classePredita] + falso);
			}
		}
		return this.taxaFalsasDescobertas[classe];
	}
	
	public int getAcuracia() {
		if(this.acuracia == null) {
			this.acuracia = 0;
			for(int classe = this.matrizConfusao.length-1; classe > -1; classe--) {
				this.acuracia += this.matrizConfusao[classe][classe];
			}
		}
		return this.acuracia;
	}
	
	public int getErro() {
		if(this.erro == null) {
			this.erro = 0;
			for(int classeReal = this.matrizConfusao.length-1; classeReal > -1; classeReal--) {
				for(int classePredita = this.matrizConfusao[classeReal].length-1; classePredita > -1; classePredita--) {
					if(classePredita != classeReal) {
						this.erro += this.matrizConfusao[classeReal][classePredita];
					}
				}
			}
		}
		return this.erro;
	}
	
	public int[][] getMatrizConfusao() {
		return this.matrizConfusao;
	}
	
	public MatrizConfusao(int[][] matrizConfusao) {
		this.matrizConfusao = matrizConfusao;
		qtdExec = 1;
	}
	
	public MatrizConfusao(int[][] matrizConfusao, int qtdExec) {
		this.matrizConfusao = matrizConfusao;
		this.qtdExec = qtdExec;
	}
}
