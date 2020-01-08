package riSystem;

import java.util.ArrayList;

import riSystem.org.tartarus.snowball.ext.*;

public class Stem {
	private englishStemmer Estemmer = new englishStemmer();

	public ArrayList<String> execute(ArrayList<String> terms) {
		int i;
		for (i = 0; i < terms.size(); i++) {
			if (! terms.get(i).contains("-")) {
				this.Estemmer.setCurrent(terms.get(i));
				this.Estemmer.stem();
				terms.set(i, Estemmer.getCurrent());
			}
		}
		return terms;
		}
}