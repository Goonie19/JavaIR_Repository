package riSystem;

import java.util.ArrayList;

public class Umbral {
    ArrayList<String> array;

    public Umbral(ArrayList<String> stemmed) {
        array = stemmed;
    }

    public ArrayList<String> execute() {
        for(int i = 0; i< array.size(); ++i) {
            if(array.get(i).length() < 2 ) {
                array.remove(i);
            }
        }
        return array;
    }
}