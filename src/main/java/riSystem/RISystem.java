package riSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

import riSystem.Filters.*;

public class RISystem {

	private static File f = new File("corpus");

	private static Scanner _reader;

	HashMap<String,Tupla> indiceInv;		
	HashMap<String,Double> pesos;

	public RISystem() throws IOException{
		try {
			indiceInv = Recupera.cargaIndice();
			pesos = Recupera.cargaVector();
		} catch(Exception e) {
			System.out.println("El indice no existe, necesita indexar primero");
			indiceInv = new HashMap<String,Tupla>();
			pesos = new HashMap<String,Double>();
		}
	}

	public void index() throws IOException {

		FilterManager manejaFiltros = new FilterManager();

		manejaFiltros.add(new Intercambia("[^-\\w]", " "));
		manejaFiltros.add(new Intercambia("\\b[0-9]+\\b", " "));
		manejaFiltros.add(new Intercambia("-+ | -+", " "));
		manejaFiltros.add(new Intercambia(" +", " "));
		manejaFiltros.add(new Minus());

		ArrayList<String> array;
		Terminos t = new Terminos();// para palabras vacias

		try (BufferedReader brPalabras = new BufferedReader(new FileReader("palabras vacias.txt"))) {

			String pVacia;
			while ((pVacia = brPalabras.readLine()) != null) {

				t.add(pVacia);
			}
		}
		String texto = "", textoFiltrado = "";
		int nArchivos = 0; 

		if (f.isDirectory()) {
			System.out.println("\n----------------Creando indice invertido----------------");

			for (File file : f.listFiles()) {
				texto = new String(Files.readAllBytes(Paths.get(file.getPath())));
				textoFiltrado = manejaFiltros.execute(texto);

				++nArchivos;

				array = Division.execute(textoFiltrado);
				array = t.execute(array);// palabras vacias

				Stem st = new Stem();

				st.execute(array);// stemmer

				Umbral umbral = new Umbral(array);
				umbral.execute();

				Contador ct = new Contador();
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map = ct.execute(array);

				TF.execute(map, file.getName());
			}
			System.out.println("\n----------------" + nArchivos + " archivos leidos----------------\n");
		}

		TF.calculaIDF(nArchivos);
		Recupera.guardaIndice(TF.getIndice());
		TF.setIndice(TF.getIndice());

		System.out.println("----------------Calculando longitudes----------------");
		TF.vector(TF.getIndice());
		Recupera.guardaLongitud(TF.getLong());

	}	

	private static HashMap<String, Double> sortByComparator(HashMap<String, Double> unsortMap, final boolean order)
    {

        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                    Entry<String, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


	public void buscar(String terminos,int nRes) throws IOException {
		
		if(indiceInv.isEmpty())
			indiceInv = Recupera.cargaIndice();

		if(pesos.isEmpty())
			pesos = Recupera.cargaVector();

		ArrayList<String> busqueda;		

		FilterManager manejaFiltros = new FilterManager();
		
		manejaFiltros.add(new Minus());
		manejaFiltros.add(new Intercambia("[^-\\w]", " "));
		manejaFiltros.add(new Intercambia("\\b[0-9]+\\b", " "));
		manejaFiltros.add(new Intercambia("-+ | -+", " "));
		manejaFiltros.add(new Intercambia(" +", " "));

		Terminos ter=new Terminos();//para quitar palabras vacias
		
		try(BufferedReader brPalabras = new BufferedReader(new FileReader("palabras vacias.txt"))){  
		
			String pVacia;
			while((pVacia = brPalabras.readLine()) != null) {

				ter.add(pVacia);
			}
		}

		terminos = manejaFiltros.execute(terminos);
		busqueda=Division.execute(terminos);
		busqueda = ter.execute(busqueda);

		Stem st=new Stem();
				
		st.execute(busqueda);//stemmer
				
		Umbral umbral = new Umbral(busqueda);
		umbral.execute();

		double tf, idf, tfIdfTotal;

		HashMap<String,Double> puntuacion=new HashMap<String,Double>();

		for (String termino : busqueda) { 

			if (indiceInv.containsKey(termino)) { 

				idf = indiceInv.get(termino).getIDF(); 
				for (String idDocumento : indiceInv.get(termino).docPeso().keySet()) {
					
					tf = indiceInv.get(termino).docPeso().get(idDocumento); 

					if (puntuacion.containsKey(idDocumento))
						tfIdfTotal = puntuacion.get(idDocumento) + tf * Math.pow(idf, 2);
					else
						tfIdfTotal = tf * Math.pow(idf, 2);
						
					puntuacion.put(idDocumento, tfIdfTotal);
				}
			}
		}

		for (String idDocumento : puntuacion.keySet()) 
			puntuacion.put(idDocumento, puntuacion.get(idDocumento) / pesos.get(idDocumento));

		HashMap<String, Double> ordenado = sortByComparator(puntuacion, false);
		
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

        while(!exit) {
            System.out.println("¿Qué operación desea realizar?\n 0. Salir!\n1. Indexar\n2. Buscar");
            iOperation = _reader.nextInt();
            switch(iOperation) {
                case 0: {

                    exit = true;
				}; break;
				case 1: {
					TF.getIndice().clear();
					TF.getLong().clear();
				
					long inicio = System.currentTimeMillis();
					ri.index();

					long fin = System.currentTimeMillis() - inicio;
		
					System.out.println("(Indexados todos los documentos en "+fin+" milisegundos)");
				} break;
                case 2: {

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
