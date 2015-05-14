package br.usp.ia.ep1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import br.usp.ia.ep1.utils.*;

public class Main {
	
	public static final int LOG_ERRO = 1;
	public static final int LOG_INFO = 0;
	
	public static void log(int lvl, String msg) {
		switch(lvl) {
		case Main.LOG_ERRO:
			System.err.println("[ERRO] " + msg);
			break;
		case Main.LOG_INFO:
			System.out.println("[INFO] " + msg);
		}
	}
	
	private static void printDescDados(String caminho) throws FileNotFoundException {
		float[][] dados = MN.transformarArrayStringParaFloat(ES.lerArquivo(caminho), PreProcessamento.CHR_DELIMIT);
		DescDados desc = MN.descDados(dados, dados[0].length-1);
		
		System.out.println(caminho);
		System.out.println("Pos. Classe: " + (dados[0].length-1));
		System.out.println("Min. Vlr. Atrib.: " + desc.getMinVlrAtrib());
		System.out.println("Max. Vlr. Atrib.: " + desc.getMaxVlrAtrib());
		System.out.println("Min. Vlr. Classe: " + desc.getMinVlrClasse());
		System.out.println("Max. Vlr. Classe: " + desc.getMaxVlrClasse());
		System.out.println("Qtd. Atrib.: " + (desc.getQtdAtribs() - 1));
		System.out.println("Qtd. Dados: " + desc.getQtdDados());
		System.out.println("Qtd. Vlr. Atrib.: " + desc.getQtdVlrAtribs());
		System.out.println("Qtd. Vlr. Classe: " + desc.getQtdVlrClasses());
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		//Scanner sc = new Scanner(System.in);
		Scanner sc = new Scanner("out/treino.out out/valida.out out/teste.out 0,6 50 50 true");

		String nmArqTreino = sc.next();
		String nmArqValida = sc.next();
		String nmArqTeste = sc.next();
		float txAprend = sc.nextFloat();
		int numNeuroMLP = sc.nextInt();
		int numNeuroLVQ = sc.nextInt();
		boolean iniPesos = sc.nextBoolean();
		
		PreProcessamento pre = new PreProcessamento(new String[] { "./res/optdigits.tra", "./res/optdigits.tes" },
								   new String[] { nmArqTreino, nmArqValida, nmArqTeste },
								   new float[] { 0.6F, 0.2F, 0.2F });
		
		float[][] dadosTreina = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTreino), PreProcessamento.CHR_DELIMIT);
		float[][] dadosValida = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqValida), PreProcessamento.CHR_DELIMIT);
		float[][] dadosTeste = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTeste), PreProcessamento.CHR_DELIMIT);
		
		LVQ lvq = new LVQ(dadosTreina, dadosValida, dadosTeste, txAprend, numNeuroLVQ, iniPesos);
		lvq.init(1, 10 ,10);
		
		RespostaClassificador rc = lvq.testar();
		
		System.out.println("Quantidade de acertos: " + rc.getQtdAcertos());
		System.out.println("Quantidade de erros: " + rc.getQtdErros());
		System.out.println("Taxa de aprendizado: " + rc.getTxAprend());
		System.out.println();
		
		lvq.imprimePesos();
		
		System.out.println();
		System.out.println("Matriz confusao: ");
		
		for(int i = rc.getMatrizConfusao().length-1; i > -1; i--) {
			for(int j = rc.getMatrizConfusao()[i].length-1; j > -1; j--) {
				System.out.println("[" + i + "]" + "[" + j + "]: " + rc.getMatrizConfusao()[i][j]);
			}
		}
		
		/*for(int i = 1; i <= 500; i++){
			for (float j = 1; j >= 0.5; j -= 0.1){
				for( int k = 0; k <= 1; k++){
					for(int x = 10; x <= 100; x++){
						for(int t = 1; t <= 10; t++){
							LOG logauxlvq = new LOG(k, x, t, i, j, 0);
							
							LVQ auxlvq = new LVQ(dadosTreina, dadosValida, dadosTeste, j, i, iniPesos);
							auxlvq.init(1, t ,x);
							
							RespostaClassificador auxrc = auxlvq.testar();
							
							logauxlvq.completaLog(auxlvq.pesos, auxrc.getQtdAcertos(), auxlvq.epocas, 0, auxrc.getTxAprend());
						
							System.out.println("Quantidade de acertos: " + auxrc.getQtdAcertos());
							System.out.println("Quantidade de erros: " + auxrc.getQtdErros());
							System.out.println("Taxa de aprendizado: " + auxrc.getTxAprend());
							System.out.println();
						
							logauxlvq.escreveLOG();
						}
					}
				}
			}
		}*/
	}

}
