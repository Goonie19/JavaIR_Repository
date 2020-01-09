package riSystem;

import java.util.HashMap;


public class Tupla {
	private double idf=0.0;
	private HashMap<String,Double> docPeso;
	
	public Tupla(double idf, HashMap<String,Double> m){
		
		this.idf=idf;
		docPeso=m;
	}
	
	public HashMap<String,Double> docPeso(){return docPeso;}
	
	public void IDF(double d){idf=d;}
	
	public double getIDF(){ return idf; }
	
	public String toString() { return idf + " - " + docPeso + " "; }
	
}