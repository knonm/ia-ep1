package br.usp.ia.ep1;

import java.io.IOException;
import java.util.Scanner;

import br.usp.ia.ep1.MLP.DadosDeEntradaProcessados;
import br.usp.ia.ep1.MLP.DadosDeTeste;
import br.usp.ia.ep1.MLP.EstruturaMLP;
import br.usp.ia.ep1.MLP.TreinamentoMLP;
import br.usp.ia.ep1.utils.*;

public class Main {
	
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
		int qntEpocasTotais = 10;
		int qntEpocasValidacao = 50;
		
		System.out.println();
		PreProcessamento pre = new PreProcessamento(new String[] { "./res/optdigits.tra", "./res/optdigits.tes" },
								   new String[] { nmArqTreino, nmArqValida, nmArqTeste },
								   new float[] { 0.6F, 0.2F, 0.2F });
		System.out.println();
		float[][] dadosTreina = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTreino), PreProcessamento.CHR_DELIMIT);
		float[][] dadosValida = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqValida), PreProcessamento.CHR_DELIMIT);
		float[][] dadosTeste = MN.transformarArrayStringParaFloat(ES.lerArquivo(nmArqTeste), PreProcessamento.CHR_DELIMIT);

		System.out.println("Instanciando LVQ...");
		LVQ lvq = new LVQ(dadosTreina, dadosValida, dadosTeste, txAprend, numNeuroLVQ, iniPesos, qntEpocasTotais, qntEpocasValidacao);
		System.out.println("Comecando treinamento LVQ...");
		lvq.init(1);
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
		System.out.println("Valores resultantes:");
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
		
		// MLP
		txAprend = 0.5F;
		numNeuroMLP = 20;
		iniPesos = true;
		
		DadosDeEntradaProcessados[] dadosTreinamentoProcessados = MN.transformarDadosTreino(ES.lerArquivo(nmArqTreino));
		DadosDeTeste[] dadosValidacaoProcessados = MN.transformarDadosTeste(ES.lerArquivo(nmArqValida));
		DadosDeTeste[] dadosTesteProcessados = MN.transformarDadosTeste(ES.lerArquivo(nmArqTeste));
		
		//Inicializa os pesos na rede de acordo com o valor requisitado pelo usuario
		System.out.println("Instanciando MLP...");
		EstruturaMLP mlp = new EstruturaMLP(numNeuroMLP, 10, iniPesos, dadosTreinamentoProcessados[0].QuantidadeDadosEntrada());
		
		System.out.println("Comecando treinamento MLP...");
		TreinamentoMLP treino = new TreinamentoMLP(mlp, dadosTreinamentoProcessados, txAprend, iniPesos);
		treino.Treinar(10, 100, dadosTesteProcessados, dadosValidacaoProcessados);
		
		System.out.println("Treinamento concluido.");
		System.out.println();
		System.out.println("Testando resultado...");
		LOG logMLP = new LOG(0, qntEpocasValidacao, qntEpocasTotais, numNeuroMLP, txAprend, 1);
		RespostaClassificador respostaMLP = mlp.ExecutarRede(dadosTesteProcessados);
		
		System.out.println("Teste completo.");
		System.out.println();
		System.out.println("Iniciando LOG...");
		logMLP.completaLogMLP(respostaMLP.getQtdAcertos(), respostaMLP.getEpocasTreinoRede(), mlp.getCamadaEscondida().length, respostaMLP.getMatrizConfusao(), respostaMLP.getDadosDeTesteMLP());
		
		System.out.println("Escrevendo LOG...");
		logMLP.escreveLOG();
		System.out.println("LOG completo.");
		
		System.out.println();
		System.out.println("Valores resultantes:");
		System.out.println("Quantidade de acertos: " + respostaMLP.getQtdAcertos());
		System.out.println("Quantidade de erros: " + respostaMLP.getQtdErros());
		System.out.println("Epocas passadas: " + respostaMLP.getEpocasTreinoRede());
		
		System.out.println();
		System.out.println("Matriz confusao: ");
		
		System.out.println("	9	8	7	6	5	4	3	2	1	0");
		
		for(int i = respostaMLP.getMatrizConfusao().getMatrizConfusao().length-1; i > -1; i--) {
			System.out.print(i);
			for(int j = respostaMLP.getMatrizConfusao().getMatrizConfusao()[i].length-1; j > -1; j--) {
				System.out.print("	"+respostaMLP.getMatrizConfusao().getMatrizConfusao()[i][j]);
			}
			System.out.println();
		}
		
	}
}
