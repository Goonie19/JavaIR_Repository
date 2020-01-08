package riSystem;

import java.util.HashMap;


public class Tupla {
	private double idf=0.0;
	private HashMap<String,Double> docPeso;
	
	/**Constructor
	 * @param idf Guarda el idf
	 * @param m Map con todas las parejas documento-peso
	 */
	public Tupla(double idf, HashMap<String,Double> m){
		
		this.idf=idf;
		docPeso=m;
	}
	
	/**
	 * Observador de docPeso
	 * @return
	 */
	public HashMap<String,Double> docPeso(){return docPeso;}
	/**
	 * Modificador de IDF 
	 * @param d
	 */
	public void IDF(double d){idf=d;}
	/**
	 * Observador de IDF
	 * @return
	 */
	public double getIDF(){ return idf; }
	
	/* Sobrecarga del m�todo toString
	 * @see java.lang.Object#toString()
	 */
	public String toString() { return idf + " - " + docPeso + " "; }
	
}