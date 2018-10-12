package it.ltc.clienti.coltorti;

public class SpostaPezziInventarioMain {
	
	public static final int ID_INVENTARIO = 3500;
	public static final int ID_INVENTARIO_BIS = 4358;
	public static final int ID_INVENTARIO_CORR = 5027;
	
	public static void main(String[] args) {
		int[] idDestinazioni = { 10566 };		
		SpostaPezziCarico spostatore = SpostaPezziCarico.getInstance();
		spostatore.spostaPezzi(ID_INVENTARIO, idDestinazioni);
	}
	
}