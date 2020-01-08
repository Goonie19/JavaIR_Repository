package riSystem.Filters;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import riSystem.GenFilter;;

public class Intercambia implements GenFilter {

	private String remplazada;
	private String remplazo;
	
	/**Constructor
	 * @param a
	 * @param b
	 */
	public Intercambia(String a, String b){
		remplazada=a; remplazo=b;
	}
	
	/** Sustituye las expresiones regulares
	 * @see GenericFilter#execute(java.lang.String)
	 */
	public String execute(String s) {
		Pattern p1=Pattern.compile(remplazada);
		Matcher m=p1.matcher(s);
		
		s=m.replaceAll(remplazo);
		return s;
	}

}