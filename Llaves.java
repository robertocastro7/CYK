import java.util.ArrayList;
import java.util.List;

public class Llaves {

	String key;
	List<String> val;
	
	
	public Llaves(String key) {
		this.key = key;
		val = new ArrayList<String>();
	}
	
	public void addElement(String elem) {
		val.add(elem);
	}
	
	public String getValues() {
    	String values = "";
    	for(int x = 0; x < val.size(); x++) {
			values = values + val.get(x);
		}
    	return values;
    }
}