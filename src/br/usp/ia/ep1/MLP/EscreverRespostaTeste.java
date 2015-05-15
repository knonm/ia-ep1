package br.usp.ia.ep1.MLP;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.usp.ia.ep1.utils.ES;

public class EscreverRespostaTeste {

	public static void escrever(DadosDeTeste[] resultado, MatrizDeConfusao matrizConfusao, int epocaParada) throws IOException {
		String arquivoSaida = "./out/ResultadoRede/Teste da MLP " + getCurrentTimeStamp() + ".txt";
		String linhaBranco = "";
		String header1 = "Resultado do teste na Rede Neural MLP";
		String epoca = "Epoca de parada do treinamento: " + String.valueOf(epocaParada);
		String[] matrizEscrever = matrizDeConfusaoParaStringArray(matrizConfusao);
		String[] resultadoEscrever = dadosDeTesteParaStringArray(resultado);
		
		String[] escrever = new String[matrizEscrever.length + resultadoEscrever.length + 7];
		escrever[0] = header1;
		escrever[1] = linhaBranco;
		escrever[2] = epoca;
		escrever[3] = linhaBranco;
		escrever[4] = "Taxa de acuracia: " + String.valueOf(matrizConfusao.taxaAcuracia()) + "%";
		escrever[5] = "Taxa de erro: " + String.valueOf(matrizConfusao.taxaErro()) + "%";
		escrever[6] = linhaBranco;
		int contadorLinha = 7;
		for (int i = 0; i < matrizEscrever.length; i++) {
			escrever[contadorLinha] = matrizEscrever[i];
			contadorLinha++;
		}
		for (int i = 0; i < resultadoEscrever.length; i++) {
			escrever[contadorLinha] = resultadoEscrever[i];
			contadorLinha++;
		}
		
		ES.escreverDadosPulandoLinha(arquivoSaida, escrever);
	}
	
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	    Date agora = new Date();
	    String dataAgora = sdfDate.format(agora);
	    return dataAgora;
	}
	
	public static String[] matrizDeConfusaoParaStringArray(MatrizDeConfusao matrizConfusao) {
		Double[] classes = matrizConfusao.getClasses();
		int[][] matriz = matrizConfusao.getMatrizDeConfusao();
		String[] resultado = new String[matriz.length + 2]; // +2 para rotulos
		
		String header = "Matriz de Confusao resultante do teste na MLP (separador utilizado: espaco):";
		resultado[0] = header;
		
		String[] aux = new String[classes.length + 1]; // +1 para formatacao da tabela
		aux[0] = " ";
		for (int i = 0; i < classes.length; i++) {
			aux[i+1] = String.valueOf(classes[i].intValue()) + " ";
		}
		resultado[1] = StringArrayParaSring(aux);
		
		for (int linha = 0; linha < matriz.length; linha++) {
			aux = new String[classes.length + 1];
			aux[0] = String.valueOf(classes[linha].intValue()) + " ";
			for (int coluna = 0; coluna < matriz.length; coluna++) {
				aux[coluna+1] = String.valueOf((int)matriz[linha][coluna]) + " ";
			}
			resultado[linha+2] = StringArrayParaSring(aux);
		}	
		return resultado;
	}
	
	public static String[] dadosDeTesteParaStringArray(DadosDeTeste[] resultado) {
		String[] resultadoEmString = new String[resultado.length + 2]; // +1 para rotulo
		String linhaBranco = "";
		String header = "Respostas da rede MLP para o conjunto de testes:";
		resultadoEmString[0] = linhaBranco;
		resultadoEmString[1] = header;
		String[] aux;
		for (int i = 0; i < resultado.length; i++) {
			aux = new String[resultado[i].getQuantidadeAtributos() + 3];
			aux[0] = "Classe predita: ";
			aux[1] = String.valueOf((int)resultado[i].getClassePredita()) + " ";
			aux[2] = "Dados Testados: ";
			double[] dadosTestados = resultado[i].getDadosDeTeste();
			for (int y = 0; y < dadosTestados.length; y++) {
				aux[y+3] = String.valueOf((int)dadosTestados[y]) + " ";
			}
			resultadoEmString[i+2] = StringArrayParaSring(aux);
		}
		
		return resultadoEmString;
	}
	
	public static String StringArrayParaSring(String[] array) {
		StringBuffer resultado = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
		   resultado.append( array[i] );
		}
		String stringResultante = resultado.toString();
		return stringResultante;
	}
}
