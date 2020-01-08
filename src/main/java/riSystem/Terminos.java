package riSystem;

import java.util.ArrayList;
import java.util.TreeSet;

public class Terminos {

	private TreeSet<String> arbol;
	
	public Terminos(){
		arbol=new TreeSet<String>();
	}
	
	public void add(String s){
		arbol.add(s);
	}
	
	public ArrayList<String> execute(ArrayList<String> array){
		for(int i = array.size() - 1; i >= 0; --i) {
			//System.out.println(array.get(i));
			if(arbol.contains(array.get(i))) {
                
				array.remove(i);
			}
		}
		return array;
	}
}
