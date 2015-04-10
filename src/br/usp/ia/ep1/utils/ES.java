package br.usp.ia.ep1.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ES {

	public static String[] lerArquivo(String caminho) throws FileNotFoundException {
		List<String> dados = new ArrayList<String>();
		Scanner s = new Scanner(new File(caminho));
		while(s.hasNext()) {
			dados.add(s.nextLine());
		}
		s.close();
		return dados.toArray(new String[0]);
	}
	
	public static String[] lerArquivos(String[] caminhos) throws FileNotFoundException {
		List<String> dados = new ArrayList<String>();
		Scanner s;
		for(String arq : caminhos) {
			s = new Scanner(new File(arq));
			while(s.hasNext()) {
				dados.add(s.nextLine());
			}
			s.close();
		}
		return dados.toArray(new String[0]);
	}
	
	public static void escreverDado(String caminho, String dado) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(caminho)));
		bw.write(dado);
		bw.close();
	}
	
	public static void escreverDados(String caminho, String[] dados) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(caminho)));
		for(String dado : dados) {
			bw.write(dado);
		}
		bw.close();
	}
}
