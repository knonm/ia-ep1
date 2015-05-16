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
		System.out.println("Iniciando procedimentos...");
		//Scanner sc = new Scanner(System.in);
		Scanner sc = new Scanner("out/treino.csv out/valida.csv out/teste.csv"
				+ " 0,0000001 50 1 false");

		String nmArqTreino = sc.next();
		String nmArqValida = sc.next();
		String nmArqTeste = sc.next();
		float txAprend = sc.nextFloat();
		int numNeuroMLP = sc.nextInt();
		int numNeuroLVQ = sc.nextInt();
		boolean iniPesos = sc.nextBoolean();
		
		System.out.println();
		PreProcessamento pre = new PreProcessamento(new String[] { "./res/optdigits.tra", "./res/optdigits.tes" },
								   new String[] { nmArqTreino, nmArqValida, nmArqTeste },
								   new float[] { 0.6F, 0.2F, 0.2F });
		System.out.println();
		float[][] dadosTreina = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTreino), PreProcessamento.CHR_DELIMIT);
		float[][] dadosValida = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqValida), PreProcessamento.CHR_DELIMIT);
		float[][] dadosTeste = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTeste), PreProcessamento.CHR_DELIMIT);
		
		int qntEpocasTotais = 10;
		int qntEpocasValidacao = 50;
		
		System.out.println("Instanciando LVQ...");
		LVQ lvq = new LVQ(dadosTreina, dadosValida, dadosTeste, txAprend, numNeuroLVQ, iniPesos);
		System.out.println("Comecando treinamento LVQ...");
		lvq.init(1, qntEpocasValidacao ,qntEpocasTotais);
		System.out.println("Treinamento concluido.");
		System.out.println();
		System.out.println("Testando resultado...");
		
		LOG log = new LOG(0, qntEpocasValidacao, qntEpocasTotais, numNeuroLVQ, txAprend, 0);
		
		RespostaClassificador rc = lvq.testar();
		System.out.println("Teste completo.");
		System.out.println();
		System.out.println("Iniciando LOG...");
		log.completaLog(lvq.pesos, rc.getQtdAcertos(), lvq.epocas, 0, lvq.txAprend, rc.getMatrizConfusao());
		
		System.out.println("Escrevendo LOG...");
		log.escreveLOG();
		System.out.println("LOG completo.");
		
		System.out.println();
		System.out.println("Valore resultantes:");
		System.out.println("Quantidade de acertos: " + rc.getQtdAcertos());
		System.out.println("Quantidade de erros: " + rc.getQtdErros());
		System.out.println("Epocas passadas: " + lvq.epocas);
				
		//lvq.imprimePesos();
		
		System.out.println();
		System.out.println("Matriz confusao: ");
		
		System.out.println("	9	8	7	6	5	4	3	2	1	0");
		
		for(int i = rc.getMatrizConfusao().getMatrizConfusao().length-1; i > -1; i--) {
			System.out.print(i);
			for(int j = rc.getMatrizConfusao().getMatrizConfusao()[i].length-1; j > -1; j--) {
				System.out.print("	"+rc.getMatrizConfusao().getMatrizConfusao()[i][j]);
			}
			System.out.println();
		}
	}
}
