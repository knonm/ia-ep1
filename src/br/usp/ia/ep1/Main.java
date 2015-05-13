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
		Scanner sc = new Scanner("out/treino.out out/valida.out out/teste.out 1 1 2 true");

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
		
		//float[][] dadosTreina = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTreino), PreProcessamento.CHR_DELIMIT);
		//float[][] dadosValida = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqValida), PreProcessamento.CHR_DELIMIT);
		//float[][] dadosTeste = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTeste), PreProcessamento.CHR_DELIMIT);
		
		float[][] and = new float[4][3];
		and[0][0] = 1;
		and[0][1] = 1;
		and[0][2] = 1;
		and[1][0] = 1;
		and[1][1] = 0;
		and[1][2] = 0;
		and[2][0] = 0;
		and[2][1] = 1;
		and[2][2] = 0;
		and[3][0] = 0;
		and[3][1] = 0;
		and[3][2] = 0;
		
		float[][][] dados = new float[3][][];
		
		for(int i = 0; i < dados.length; i++) {
			dados[i] = and.clone();
		}
		
		LVQ lvq = new LVQ(dados[0], dados[1], dados[2], txAprend, numNeuroLVQ, iniPesos);
		lvq.init(1, 100, 5);
		
		RespostaClassificador rc = lvq.testar();
		
		System.out.println("Quantidade de acertos: " + rc.getQtdAcertos());
		System.out.println("Quantidade de erros: " + rc.getQtdErros());
		System.out.println("Taxa de aprendizado: " + rc.getTxAprend());
		System.out.println();
		
		//lvq.imprimePesos();
	}

}