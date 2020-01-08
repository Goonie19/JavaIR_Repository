package riSystem.Filters;

import riSystem.GenFilter;

public class Minus implements GenFilter{

	@Override
	public String execute(String s) {
		return s.toLowerCase();
	}

}
