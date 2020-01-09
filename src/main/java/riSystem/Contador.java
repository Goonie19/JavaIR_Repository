package riSystem;

import java.util.ArrayList;
import java.util.HashMap;

public class Contador {
	public HashMap<String, Integer> execute(ArrayList<String> palabras){
		
		HashMap<String, Integer> map=new HashMap<String, Integer>();
		int f = 0;
		for(int i=0;i<palabras.size();i++){
			if(map.containsKey(palabras.get(i))){

				f = map.get(palabras.get(i)) + 1;
			}else {

				f = 1;
			}
			map.put(palabras.get(i), f);
		}
		
		return map;
		
	}
}
