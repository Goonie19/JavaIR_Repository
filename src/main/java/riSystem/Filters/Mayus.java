
package riSystem.Filters;

import riSystem.GenFilter;

public class Mayus implements GenFilter {

	@Override
	public String execute(String s) {
		return s.toUpperCase();
	}

}
