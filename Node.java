
public class Node {

public String val;
public int x;
public int y;
public Node a;
public Node b;
public GetGramatica gram;
    
    public String backTracking(String acumulate)
    {
    	if(a == null && b == null)
    	{
    		 if(gram.getKey(val) != "")
    			return acumulate += (gram.getKey(val));
    	}
    	else {
    		acumulate = acumulate + a.val + b.val;
    		acumulate = acumulate + a.backTracking("");
    		acumulate = acumulate + b.backTracking("");
    	}
		return acumulate;
    	
    }
    
}