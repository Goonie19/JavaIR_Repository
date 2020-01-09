package riSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

import riSystem.Filters.*;

public class RISystem  {

	private static File f=new File("corpus");

    private static Scanner _reader;

    public void index() throws IOException {

		FilterManager manejaFiltros = new FilterManager();
		
		manejaFiltros.add(new Intercambia("[^-\\w]", " "));
		manejaFiltros.add(new Intercambia("\\b[0-9]+\\b", " "));
		manejaFiltros.add(new Intercambia("-+ | -+", " "));
		manejaFiltros.add(new Intercambia(" +", " "));
		manejaFiltros.add(new Minus());

		ArrayList<String> array;
		Terminos t=new Terminos();//para quitar palabras vacias
		
		try(BufferedReader brPalabras = new BufferedReader(new FileReader("palabras vacias.txt"))){  
		
			String pVacia;
			while((pVacia = brPalabras.readLine()) != null) {

				t.add(pVacia);
			}
		}
		String texto = "", textoFiltrado = "";
		int nArchivos=0; //para el idf
		
		if(f.isDirectory()){
			System.out.println("\n----------------Creando indice invertido----------------");

			for(File file:f.listFiles()){
				texto = new String(Files.readAllBytes(Paths.get(file.getPath())));
				textoFiltrado = manejaFiltros.execute(texto);
				
				++nArchivos;
			
				array=Division.execute(textoFiltrado);
				array=t.execute(array);//palabras vacias
				
				Stem st=new Stem();
				
				st.execute(array);//stemmer
				
				Umbral umbral = new Umbral(array);
				umbral.execute();

				Contador ct = new Contador();
				HashMap<String, Integer> map=new HashMap<String, Integer>();
				map=ct.execute(array);
				
				TF.execute(map, file.getName());
			}
			System.out.println("\n----------------"+nArchivos +" archivos leidos----------------\n");
		}
		
		TF.calculaIDF(nArchivos);
		Recupera.guardaIndice(TF.getIndice());
		TF.setIndice(TF.getIndice());
		
	    System.out.println("----------------Calculando longitudes----------------");
		TF.vector(TF.getIndice());
		Recupera.guardaLongitud(TF.getLong());
				
		
	}

	public void buscar(String terminos,int nRes) throws IOException{
		
		HashMap<String,Tupla> tfidf=TF.getIndice();
		HashMap<String,Double> pesos=TF.getLong();
		
		ArrayList<String> busqueda;		
		busqueda=Division.execute(terminos);
		
		HashMap<String,Double> puntuacion=new HashMap<String,Double>();
		
		
		for(String doc:pesos.keySet()){
			puntuacion.put(doc,0.0);//meto en puntuacion todos los documentos clave del vector
		}
		
		TreeMap<String, Double> ordenado=new TreeMap<String, Double>();
		
		for(String termino:busqueda){
			for(String doc:puntuacion.keySet()){
				
				double idf=0;
				HashMap<String, Double> t=new HashMap<String, Double>();
				
				if(tfidf.containsKey(termino)){
					idf=tfidf.get(termino).getIDF();
					
					t=tfidf.get(termino).docPeso();//La tupla
					
					if(t.containsKey(doc)){//compruebo que el termino esta en el documento actual
						double punt=puntuacion.get(doc);
						puntuacion.put(doc, punt+(idf*idf*t.get(doc)/pesos.get(doc)));
					}
				}	
			}
			Comparator<String> comparator = new Comparador(puntuacion);
			ordenado = new TreeMap<String, Double>(comparator);
			ordenado.putAll(puntuacion);			
		}
		
		System.out.println("---------------- Resultados ----------------");
		int i=0;
		
		bucle:
		for(String doc:ordenado.keySet()){
			if(puntuacion.get(doc)>0.0){
				System.out.println("------------------------------------------------------");
				System.out.println("Archivo: "+doc+" | Puntuacion: "+puntuacion.get(doc));
				
				i++;
				if(i==nRes){
					
					break bucle;
				}
			}
		}
	}

    public static void main(String[] args) throws IOException{
        _reader = new Scanner(System.in);
		System.out.println("Muy buenas! Bienvenido a RISystem!\n--------------------------------------------------------");
		RISystem ri = new RISystem();
        int iOperation;
		boolean exit = false;
		
		TF.getIndice().clear();
		TF.getLong().clear();
				
		long inicio = System.currentTimeMillis();
		ri.index();

		long fin = System.currentTimeMillis() - inicio;
		
		System.out.println("(Indexados todos los documentos en "+fin+" milisegundos)");

        while(!exit) {
            System.out.println("¿Qué operación desea realizar?\n 0. Salir!\n1. Consulta");
            iOperation = _reader.nextInt();
            switch(iOperation) {
                case 0: {

                    exit = true;
                }; break;
                case 1: {

                    System.out.print("Introduzca los terminos a buscar: ");
					_reader.nextLine();
					String terminos= _reader.nextLine();
					System.out.print("Introduzca cuantos resultados desea: ");
					int n= _reader.nextInt();
				
					System.out.println("---------------- Buscando '" + terminos + "' en " + f + " ----------------");
				
					ri.buscar(terminos,n);
				
					System.out.println("------------------------------------------------------");
					System.out.println("(Mostrados los "+n+" primeros resultados para '"+terminos+"')");
                }; break;
                default: System.out.println("Eso no es una opción!");break;
            }
        }
    }
}
