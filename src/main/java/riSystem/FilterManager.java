package riSystem;

import java.util.ArrayList;

public class FilterManager {
	
	private ArrayList<GenFilter> filters;

	public FilterManager(){
		filters=new ArrayList<GenFilter>();
	}
	
	public void add(GenFilter gf){
		filters.add(gf);
	}

	public String execute(String s) {
		
		for(int i = 0; i < filters.size(); ++i){
			s=filters.get(i).execute(s);
		}
		return s;
	}
}