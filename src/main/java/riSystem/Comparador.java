package riSystem;

import java.util.Comparator;
import java.util.HashMap;

public class Comparador implements Comparator<String>{

	HashMap<String, Double> map = new HashMap<String, Double>();

	public Comparador(HashMap<String, Double> map) {

		this.map.putAll(map);
	}

	@Override
	public int compare(String s1, String s2) {

		if(map.get(s1) >= map.get(s2)) {

			return -1;
		} else{

			return 1;
		}	
	}
}