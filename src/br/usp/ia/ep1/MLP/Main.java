package br.usp.ia.ep1.MLP;

import br.usp.ia.ep1.utils.*;

import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		String arqTreino = "./res/" + args[0];
		String arqValida = "./res/" + args[1];
		String arqTeste = "./res/" + args[2];
		double taxaAprendizado = Double.valueOf(args[3]);
		int nCamadaEscondida = Integer.valueOf(args[4]);
		int nCamadaSaida = Integer.valueOf(args[5]);
		boolean inicializacaoAleatoria = Boolean.valueOf(args[6]); //true or false
		
		String[] linhasArquivoTreino = ES.lerArquivo(arqTreino);
		DadosDeEntradaProcessados[] dadosTreino = transformarDadosTreino(linhasArquivoTreino);
		
		//imprimirDados(dadosTreino);		
		
		//TreinamentoMLP treino = new TreinamentoMLP(nCamadaEscondida,nCamadaSaida,dadosTreino,taxaAprendizado,inicializacaoAleatoria);
		//int epocaParada = treino.Treinar();
		
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
	
	/* Criei esse metodo so para testar a informacao passada nos dados lidos */
	public static void imprimirDados(DadosDeEntradaProcessados[] dados) {
		for (int i = 0; i < dados.length; i++) {
			double[] aux = dados[i].getDadosDeEntrada();
			for (int x = 0; x < aux.length; x++) {
				System.out.print((int)aux[x] + " ");
			}
			System.out.println((int)dados[i].getClasse());
		}
	}
}