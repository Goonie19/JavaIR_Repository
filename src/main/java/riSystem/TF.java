package riSystem;

import java.util.HashMap;
import java.util.Map;

public class TF {
	
	private static HashMap<String,Tupla> indiceInvertido = new HashMap<String,Tupla>();	
	private static HashMap<String,Double> mapPesos = new HashMap<String,Double>();	
	
	
	public static HashMap<String,Tupla> getIndice() { return indiceInvertido; }
	
	public static void setIndice(HashMap<String,Tupla> indice){ indiceInvertido = indice; }
	
	public static HashMap<String,Double> getLong(){ return mapPesos; }
	
	public static void setLong(HashMap<String,Double> longitudes){ mapPesos = longitudes; }
	
	public static void execute(Map<String, Integer> map, String f){
		
		HashMap<String,Double> pesoDocumento;

		double tf=0;

		for (String p: map.keySet()) {
		    
			tf= 1+Math.log(map.get(p))/Math.log(2);
			
			if(indiceInvertido.containsKey(p)) {

				pesoDocumento = indiceInvertido.get(p).docPeso();
			} else {

				pesoDocumento = new HashMap<String,Double>();
			}
			pesoDocumento.put(f, tf);
			indiceInvertido.put(p, new Tupla(0.0, pesoDocumento));
			
		}
		
	}
	
	public static void calculaIDF(int nFiles){
		 
		for(String p : indiceInvertido.keySet()){
			 
			indiceInvertido.get(p).IDF(
	    		Math.log((double)nFiles/indiceInvertido.get(p).docPeso().size())/ Math.log(2));
	    }
	}
	
	public static void vector(HashMap<String,Tupla> Indice){
		
		double peso;
		
		//recorro el map de tuplas para cada termino
		for(String s:Indice.keySet()) {
			peso = 0;
			//recorro los pesos para cada id-documento
			for(String t : Indice.get(s).docPeso().keySet()) {
				
				peso = Math.pow(Indice.get(s).docPeso().get(t) * Indice.get(s).getIDF(), 2);
				//si el map ya contiene el id-doc pues sumo el valor al peso
				if(mapPesos.containsKey(t)) {

					peso += mapPesos.get(t);
				}
				
				mapPesos.put(t, peso);
				
			}
		}
		
		for(String id : mapPesos.keySet()) {

			mapPesos.put(id, Math.sqrt(mapPesos.get(id)));
		}
		
		
	}
	
}