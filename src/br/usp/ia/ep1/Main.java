package br.usp.ia.ep1;

import java.io.FileNotFoundException;
import java.io.IOException;

import br.usp.ia.ep1.utils.ES;

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
	
	private static void printDescDados(String caminho, PreProcessamento pre) throws FileNotFoundException {
		float[][] dados = pre.transformarArrayStringParaFloat(ES.lerArquivo(caminho), PreProcessamento.CHR_DELIMIT);
		float[] desc = pre.descDados(dados, dados[0].length-1);
		
		System.out.println(caminho);
		System.out.println("Pos. Classe: " + (dados[0].length-1));
		System.out.println("Min. Vlr. Atrib.: " + desc[0]);
		System.out.println("Max. Vlr. Atrib.: " + desc[1]);
		System.out.println("Min. Vlr. Classe: " + desc[2]);
		System.out.println("Max. Vlr. Classe: " + desc[3]);
		System.out.println("Qtd. Atrib.: " + desc[4]);
		System.out.println("Qtd. Dados: " + desc[5]);
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		PreProcessamento pre;
		try {
			String nmArqTreino = args[0];
			String nmArqValida = args[1];
			String nmArqTeste = args[2];
			//float txAprend = Float.valueOf(args[3]);
			//int numNeuroMLP = Integer.valueOf(args[4]);
			//int numNeuroLVQ = Integer.valueOf(args[5]);
			//boolean iniPesos = Integer.valueOf(args[6]) == 0 ? false : true;
			
			pre = new PreProcessamento(new String[] { "./res/optdigits.tra", "./res/optdigits.tes" },
					new String[] { nmArqTreino, nmArqValida, nmArqTeste },
					new float[] { 0.6F, 0.2F, 0.2F });
			
			printDescDados("./res/optdigits.tra", pre);
			printDescDados("./res/optdigits.tes", pre);
			printDescDados(nmArqTreino, pre);
			printDescDados(nmArqValida, pre);
			printDescDados(nmArqTeste, pre);
		} catch (NumberFormatException ex) {
			Main.log(Main.LOG_ERRO, "Conversão do parâmetro de entrada falhou. Corrija o formato dos parâmetros de entradas.");
		}
	}

}