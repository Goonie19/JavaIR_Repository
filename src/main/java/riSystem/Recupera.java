package riSystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Recupera {
	
	public static void guardaIndice(HashMap<String,Tupla> indice) throws FileNotFoundException{
		System.out.println("----------------Guardando indice ----------------");
		PrintWriter f = new PrintWriter("indice.txt");
		f.println(indice);
		f.close();
		System.out.println("----------------Indice guardado ----------------");
	}
	
	public static void guardaLongitud(HashMap<String,Double> pesos) throws FileNotFoundException{
		System.out.println("----------------Guardando longitudes ----------------");
		PrintWriter f = new PrintWriter("peso.txt");
		f.println(pesos);
		f.close();
		System.out.println("----------------Longitudes guardada ----------------");
		
	}
		
	public static HashMap<String,Double> cargaVector() throws IOException{
		HashMap<String,Double> vector=new HashMap<>();
		System.out.println("---------------- Cargando longitudes ----------------");
		FileReader in = new FileReader("peso.txt");
	    BufferedReader br = new BufferedReader(in);

	    
	    String value=br.readLine();
	    
	    in.close();
		
		value = value.substring(1, value.length()-1); //elimina las llaves          
		String[] keyValuePairs = value.split(",");                           

		for(String pair : keyValuePairs)
		{
		    String[] entry = pair.split("=");//Separa la clave del valor
		    vector.put(entry[0].trim(), Double.parseDouble(entry[1].trim()));//mete la pareja de documento y peso. Double.parseDouble(String s)->convierte cadena a double
		}
		System.out.println("---------------- Longitudes cargadas ----------------");
		return vector;
	}
	
	public static HashMap<String,Double> cargaVector2(String value) throws IOException{
		HashMap<String,Double> vector=new HashMap<>();
		
		value = value.substring(1, value.length()-2); //elimina las llaves          
		String[] keyValuePairs = value.split(",");                           

		for(String pair : keyValuePairs)
		{
		    String[] entry = pair.split("=");//Separa la clave del valor
		    vector.put(entry[0].trim(), Double.parseDouble(entry[1].trim()));//mete la pareja de documento y peso. Double.parseDouble(String s)->convierte cadena a double
		}
		
		return vector;
	}
	
	public static HashMap<String,Tupla> cargaIndice() throws IOException{
		HashMap<String,Tupla> indice = new HashMap<String,Tupla>();
		FileReader in = new FileReader("indice.txt");
	    BufferedReader br = new BufferedReader(in);

	    String value=br.readLine();
	    
	    in.close();
		
		value = value.substring(1, value.length()-1); //elimina las llaves   
		String[] keyValuePairs = value.split(" , ");                           
		
		for(String t:keyValuePairs){
			
			String[] parejas=t.split(" - ");
			
			String[] termIDF=parejas[0].split("=");
			
			
				indice.put(termIDF[0], new Tupla(Double.parseDouble(termIDF[1]),cargaVector2(parejas[1])));
			
		}
		
		System.out.println("---------------- Indice cargado ----------------");
		return indice;
	}
}
