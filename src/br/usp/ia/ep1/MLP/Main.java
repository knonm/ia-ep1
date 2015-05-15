package br.usp.ia.ep1.MLP;

import br.usp.ia.ep1.PreProcessamento;
import br.usp.ia.ep1.utils.*;

import java.io.*;

public class Main {

	public static EstruturaMLP MLP;

	
	public static void main(String[] args) throws IOException 
	{
		String arqTreino = "./out/" + args[0];
		String arqValida = "./out/" + args[1];
		String arqTeste = "./out/" + args[2];
		double taxaAprendizado = Double.valueOf(args[3]);
		int nCamadaEscondida = Integer.valueOf(args[4]);
		int nCamadaSaida = Integer.valueOf(args[5]);
		boolean inicializacaoAleatoria = Boolean.valueOf(args[6]); //true or false
		
		//testarAndOrXor(taxaAprendizado,nCamadaEscondida,nCamadaSaida,inicializacaoAleatoria);
		
		PreProcessamento pre = new PreProcessamento(new String[] { "./res/optdigits.tra", "./res/optdigits.tes" },
				   new String[] { arqTreino, arqValida, arqTeste },
				   new float[] { 0.6F, 0.2F, 0.2F });
		
		DadosDeEntradaProcessados[] dadosTreino = transformarDadosTreino(ES.lerArquivo(arqTreino));
		DadosDeEntradaProcessados[] dadosValidacao = transformarDadosTreino(ES.lerArquivo(arqValida));
		DadosDeTeste[] dadosTeste = transformarDadosTeste(ES.lerArquivo(arqTeste));
		
		
		//imprimirDados(dadosTreino);		
		
		//TreinamentoMLP treino = new TreinamentoMLP(nCamadaEscondida,nCamadaSaida,dadosTreino,taxaAprendizado,inicializacaoAleatoria);
		//int epocaParada = treino.Treinar();




		MLP = new EstruturaMLP(nCamadaEscondida, nCamadaSaida);
		
		TreinamentoMLP treino = new TreinamentoMLP(MLP ,dadosTreino,taxaAprendizado, inicializacaoAleatoria);
		//treino.Treinar();

		
	}
	
	/* Metodo que transforma a saida de ES.lerArquivo em um objeto DadosDeEntradaProcessados[] para ser passado para a MLP */
	public static DadosDeEntradaProcessados[] transformarDadosTreino(String[] linhasArquivo) {
		DadosDeEntradaProcessados[] dados = new DadosDeEntradaProcessados[linhasArquivo.length];
		for (int i = 0; i < linhasArquivo.length; i++) {
			String[] aux = linhasArquivo[i].split(",");
			double[] dado = new double[aux.length - 1];
			for (int x = 0; x < aux.length - 1; x++) {
				dado[x] = Double.valueOf(aux[x]);
			}
			double classe = Double.valueOf(aux[aux.length-1]);
			dados[i] = new DadosDeEntradaProcessados(classe, dado);
		}
		return dados;
	}
	
	/* Metodo que transforma a saida de ES.lerArquivo em um objeto DadosDeTeste[] para ser passado para a MLP */
	public static DadosDeTeste[] transformarDadosTeste(String[] linhasArquivo) {
		DadosDeTeste[] dados = new DadosDeTeste[linhasArquivo.length];
		for (int i = 0; i < linhasArquivo.length; i++) {
			String[] aux = linhasArquivo[i].split(",");
			double[] dado = new double[aux.length - 1];
			for (int x = 0; x < aux.length - 1; x++) {
				dado[x] = Double.valueOf(aux[x]);
			}
			double classeReal = Double.valueOf(aux[aux.length-1]);
			dados[i] = new DadosDeTeste(classeReal, dado);
		}
		return dados;
	}
	
	public static void testarAndOrXor(double taxaAprendizado, int nCamadEscondida, int nCamdaSaida, boolean inicializacaoAleatoria) throws FileNotFoundException {
		DadosDeEntradaProcessados[] dadosTreinoAnd = transformarDadosTreino(ES.lerArquivo("./res/AND.txt"));
		DadosDeEntradaProcessados[] dadosTreinoOr = transformarDadosTreino(ES.lerArquivo("./res/OR.txt"));
		DadosDeEntradaProcessados[] dadosTreinoXor = transformarDadosTreino(ES.lerArquivo("./res/XOR.txt"));
		
		DadosDeTeste[] dadosTesteAnd = transformarDadosTeste(ES.lerArquivo("./res/AND.txt"));
		DadosDeTeste[] dadosTesteOr = transformarDadosTeste(ES.lerArquivo("./res/OR.txt"));
		DadosDeTeste[] dadosTesteXor = transformarDadosTeste(ES.lerArquivo("./res/XOR.txt"));
		
		//chamada da rede neural de treino e de teste aqui para os arquivos criados acima.
	}
	
	/* Criei esse metodo so para testar a informacao passada nos dados lidos */
	public static void imprimirDados(DadosDeEntradaProcessados[] dados) {
		for (int i = 0; i < dados.length; i++) {
			double[] aux = dados[i].getDadosDeEntrada();
			for (int x = 0; x < aux.length; x++) {
				System.out.print(aux[x] + " ");
			}
			System.out.println(dados[i].getClasse());
		}
	}
}