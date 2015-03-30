package br.usp.ia.ep1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	
	public static void main(String[] args) throws IOException {
		try {
			String nmArqTreino = args[0];
			String nmArqValida = args[1];
			String nmArqTeste = args[2];
			//float txAprend = Float.valueOf(args[3]);
			//int numNeuroMLP = Integer.valueOf(args[4]);
			//int numNeuroLVQ = Integer.valueOf(args[5]);
			//boolean iniPesos = Integer.valueOf(args[6]) == 0 ? false : true;
			
			Map<String, Float> mp = new HashMap<String, Float>();
			mp.put(nmArqTreino, 0.6F);
			mp.put(nmArqValida, 0.2F);
			mp.put(nmArqTeste, 0.2F);
			
			new RedeNeural(new String[] { "./res/optdigits.tra", "./res/optdigits.tes" }, mp);
		} catch (NumberFormatException ex) {
			Main.log(Main.LOG_ERRO, "Conversão do parâmetro de entrada falhou. Corrija o formato dos parâmetros de entradas.");
		}
	}

}