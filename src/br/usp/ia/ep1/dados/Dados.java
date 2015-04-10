package br.usp.ia.ep1.dados;

public class Dados {

	// Quantidade de atributos por instancia no conjunto de dados
	public static final int NUM_ATRIBS = 65;
		
	// Posicao no vetor de atributos em que o atributo de classe esta localizado
	protected static final int POS_ATRIB_CLASSE = 64;
		
	// Menor valor que um atributo pode assumir no conjunto de dados
	private static final int MIN_VLR_ATRIB = 0;
		
	// Maior valor que um atributo pode assumir no conjunto de dados
	private static final int MAX_VLR_ATRIB = 16;
		
	// Menor valor que um atributo classe pode assumir no conjunto de dados
	public static final int MIN_VLR_CLASSE = 0;
		
	// Maior valor que um atributo classe pode assumir no conjunto de dados
	public static final int MAX_VLR_CLASSE = 9;
	
	private float[][] dados;
	
	protected float[] getDado(int index) throws ArrayIndexOutOfBoundsException {
		return dados[index];
	}
	
	public Dados() {
		
	}
}
