package br.usp.ia.ep1.MLP;

import br.usp.ia.ep1.PreProcessamento;
import br.usp.ia.ep1.utils.*;

import java.io.*;
import java.util.Scanner;

public class Main {

	public static EstruturaMLP MLP;

	
	public static void main(String[] args) throws IOException 
	{
		//Scanner sc = new Scanner(System.in);
		Scanner sc = new Scanner("./out/treino.out ./out/valida.out teste.out 0,8 2 2 true");
		
		String arqTreino = sc.next();
		String arqValida = sc.next();
		String arqTeste = sc.next();
		double taxaAprendizado = sc.nextDouble();
		int nCamadaEscondida = sc.nextInt();
		int nCamadaSaida = sc.nextInt();
		boolean inicializacaoAleatoria = sc.nextBoolean(); //true or false
		/*
		PreProcessamento pre = new PreProcessamento(new String[] { "./res/optdigits.tra", "./res/optdigits.tes" },
				   new String[] { arqTreino, arqValida, arqTeste },
				   new float[] { 0.6F, 0.2F, 0.2F });
		
		DadosDeEntradaProcessados[] dadosTreino = transformarDadosTreino(ES.lerArquivo(arqTreino));
		DadosDeEntradaProcessados[] dadosValidacao = transformarDadosTreino(ES.lerArquivo(arqValida));
		DadosDeTeste[] dadosTeste = transformarDadosTeste(ES.lerArquivo(arqTeste));
		*/
		int quantidadeDeTreinos = 1000;

		//Inicializa os pesos na rede de acordo com o valor requisitado pelo usuario
		MLP = new EstruturaMLP(nCamadaEscondida, nCamadaSaida, inicializacaoAleatoria, 2);
		//MLP = new EstruturaMLP(nCamadaEscondida, nCamadaSaida, inicializacaoAleatoria, dadosTreino[0].QuantidadeDadosEntrada());

		testarAndOrXor(taxaAprendizado,nCamadaEscondida,nCamadaSaida,inicializacaoAleatoria);
		/*
		//Treina os pesos da rede para prepara-la para ser utilizada
		TreinamentoMLP treino = new TreinamentoMLP(MLP, dadosTreino, taxaAprendizado, inicializacaoAleatoria);
		treino.Treinar(quantidadeDeTreinos);

		//int epocaParada = treino.Treinar();
		
		//Treina a rede com os dados de entrada
		MLP.ExecutarRede(dadosTeste);
		*/
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
		
		TreinamentoMLP treino = new TreinamentoMLP(MLP, dadosTreinoAnd, taxaAprendizado, inicializacaoAleatoria);
		treino.Treinar(1000, 10000, dadosTesteAnd);
		
		MLP.ExecutarRede(dadosTesteAnd);
		
		//chamada da rede neural de treino e de teste aqui para os arquivos criados acima.
	}
}