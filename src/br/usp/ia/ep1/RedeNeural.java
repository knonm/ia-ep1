package br.usp.ia.ep1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

class Entrada {
	
	private Integer[] conteudo;
	private int classe;
	
	public Entrada(List<Integer> conteudo) {
		this.classe = conteudo.get(conteudo.size()-1);
		conteudo.remove(conteudo.size()-1);
		this.conteudo = conteudo.toArray(new Integer[0]);
	}
	
	@Override
	public String toString() {
		String string = new String();
		string += "Classe: " + String.valueOf(classe) + "\n";
		for(int s : conteudo) {
			string += String.valueOf(s) + "\n";
		}
		return string;
	}
}

public class RedeNeural {
	
	public static int minMaxNormal() {
		return 0;
	}
	
	public static void main (String args[]) {
		Scanner s;
		Entrada entrada = null;
		try {
			s = new Scanner(new File("./res/optdigits.tra"));
			String linha;
			List<Integer> conteudo = new ArrayList<Integer>();
			while(s.hasNext()) {
				linha = s.nextLine();
				for(String num : linha.split(",")) {
					conteudo.add(Integer.valueOf(num));
				}
				entrada = new Entrada(conteudo);
				conteudo.clear();
			}
			System.out.println(entrada);
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
	}
}