import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.util.ArrayList;

public class AlgoritmoCYK {
	public static ArrayList<ArrayList<String>> mainMtrx = new ArrayList<>();
	public static ArrayList<ArrayList<String>> secondMtrx = new ArrayList<>();
	public static GetGramatica gram;
	public boolean initializeFlg = false;
	public boolean belongsFlg = false;
	public boolean resultFlg = false;
	public static ArrayList<Node> nodes;
	public static String word = "[[]][";
	public static boolean endFlg = false;
	public static String derivTree = "";

	public void controlM(GetGramatica grammar, String wrd) {
		System.out.println("1 INIT MATRIX -> THEY'RE ARRAYLISTS");
		initMainMtrx(this.mainMtrx, wrd.length());
		System.out.println("2 SHOW MAIN MATRIX INITIAL FORM");
		mainPrint(this.mainMtrx);
		System.out.println("3 FILL INIT VALUES");
		inVal(mainMtrx,gram, wrd);
		mainPrint(this.mainMtrx);
		System.out.println("4 CYK");
		checkCYK(mainMtrx, gram);
		System.out.println("5 RESULTS");
	}


	public void initMainMtrx(ArrayList<ArrayList<String>> mainMt, int wrdLength) {
		initializeFlg = true;
		@SuppressWarnings("unused")
		int count = 0;
		System.out.println("First init of main matrix...");
		for(int x = 0; x < word.length(); x++){
			ArrayList<String> arrayList = new ArrayList<>();
			for(int y = word.length() - (x); y > 0; y--)
				arrayList.add("#");
			mainMt.add(arrayList);
		}
		count++;
	}


	public static void mainPrint(ArrayList<ArrayList<String>> mainM){
		String tabs = "\t";    	
		for(int x = 0; x < mainM.size(); x++) {
			System.out.print(x + 1 + "\t");
		}
		System.out.println("");
		for(int x = 0; x<mainM.size(); x++){
			if(x > 0){
				System.out.print(tabs);
				tabs += "\t";
			}
			for(int y = 0; y < mainM.get(x).size(); y++) {
				System.out.print(mainM.get(x).get(y) + "\t");
			}
			System.out.println(x + 1);
		}
	}


	public static void inVal(ArrayList<ArrayList<String>> mainMat,GetGramatica gramm, String wrd1){
		String value = "";
		String tmp = wrd1;
		String letter = "";		
		for(int x = 0; x < mainMat.size(); x++) {
			letter = tmp.substring(0, 1);
			value = gramm.getContent(letter);			
			mainMat.get(x).set(0, value);
			if(x < wrd1.length()) {
				tmp = tmp.substring(1);
			}else {
				tmp = "";
			}
		}
	}

	public static void checkCYK(ArrayList<ArrayList<String>> maMtx,GetGramatica grammarObj){
		int xInitPot = 1;
		int yInitPot = 0;
		int cont = 0;
		boolean belongs = false;
		String values = new String();
		int n = maMtx.size();

		for(int i = 0; i < maMtx.size() - 1; i++)
		{
			for(int x = 0; x < maMtx.get(i + 1).size(); x++)
			{
				int increment = x;
				values = "";
				for(int y = 0; y < xInitPot; y++)
				{
					increment++;
					String result = comb(maMtx.get(x).get(y), maMtx.get(increment).get(yInitPot - y), grammarObj);
					if(result != "0"){
						addPoint(result, x, xInitPot, maMtx.get(x).get(y), y, maMtx.get(increment).get(yInitPot - y), increment, yInitPot - y);
					}
					if( (values == "0" && result != "0") || (result == "0" && values.isEmpty())) {
						values = result;
					}else if(result != "0"){                    	
						if(values.isEmpty())
							values += result;
						else
							values = values.concat(" ".concat(result));
					}
				}
				values = clean(values);
				maMtx.get(x).set(xInitPot, values);
				mainPrint(maMtx);
			}
			yInitPot = xInitPot;
			xInitPot++;
		}	

		if(maMtx.get(0).get(maMtx.size() - 1).contains("S")) {
			endFlg = true;
			backTracking();
		}else {
			endFlg = false;
			System.out.println("NOT POSSIBLE");
		}
	}
	
	public static void addPoint(String r, int rx, int ry, String a, int ay, String b, int bx, int by){
		if(nodes == null){
			nodes = new ArrayList<Node>();
			for(int i = 0; i < mainMtrx.size(); i++){
				Node point = new Node();
				point.val = mainMtrx.get(i).get(0);
				point.x = i;
				point.y = 0;
				point.a = null;
				point.b = null;
				point.gram = gram;
				nodes.add(point);
			}
		}

		Node point = new Node();
		point.val = r;
		point.x = rx;
		point.y = ry;				
		point.gram = gram;	
		for(int i = 0; i < nodes.size(); i++){
			if(nodes.get(i).x == rx && nodes.get(i).y == ay){
				point.a = nodes.get(i);
				break;
			}											
		}
		for(int i = 0; i < nodes.size(); i++){				
			if(nodes.get(i).x == bx && nodes.get(i).y == by){
				point.b = nodes.get(i);
				break;
			}								
		}			
		nodes.add(point);
		System.out.println(r + " " + rx + " " + ry + " " + a + " " + rx + " " + ay + " " + b + " " + bx + " " + by);
	}

	public static void backTracking() {
		String tree = "";
		String result = "";
		int it = nodes.size() - 1;
		int x = nodes.get(it).x;
		int y = nodes.get(it).y;
		do{	
			result = "";
			tree = (nodes.get(it).backTracking(nodes.get(it).val));
			for(int z = 0;z < tree.length(); z++){
				if(Character.isLowerCase(tree.charAt(z))){
					result = result + tree.charAt(z);
				}
			}
			if(result.equals(word)){
				System.out.println(tree);
				derivTree = tree;
			}
			it--;
		}while(nodes.get(it).x == x && nodes.get(it).y == y);
	}
	
	public static String comb(String a, String b, GetGramatica myGram){
		boolean concatA = false;
		boolean concatB = false;
		int cont1 = 0;
		int cont2 = 1;
		String values = "";
		String comb1 = null, comb2 = null;
		int tabsA, tabsB;
		tabsA = a.length() - a.replace(" ", "").length();
		for (int i = 0; i <= tabsA; i++) {
			if(tabsA == 0){
				comb1 = a;
				cont1++;
			}
			else{
				concatA = true;
				comb1 = a.substring(0, a.indexOf(" "));
				a = a.substring(a.indexOf(" ") + 1, a.length());
				tabsA--;
				i--;
				if(concatA) {
					System.out.println("Do the concat A");
				}
			}
			tabsB = b.length() - b.replace(" ", "").length();
			for (int j = 0; j <= tabsB; j++) {
				if(tabsB == 0){
					comb2 = b;
					cont2++;
				}
				else{
					concatB = true;
					comb2 = b.substring(0, b.indexOf(" "));
					b = b.substring(b.indexOf(" ")+1, b.length());
					tabsB--;
					j--;
					if(concatB) {
						System.out.println("Do the concat B");
					}
				}
				if(!myGram.getContent(comb1.concat(comb2)).isEmpty())
					values += myGram.getContent(comb1.concat(comb2)) + " ";
					cont1 = 0;
					cont2 = 0;
			}
		}
		if(values.isEmpty()) {
			values = "0";
			System.out.println("A & B: " + cont1 + ", " + cont2);
		}
		else{
			values = values.substring(0, values.length() - 1);
		}
		return values;
	}

	public static String clean(String chain){
		String out = "";
		int cont = 0;
		CharSequence character = "";
		int tabs = chain.length() - chain.replaceAll(" ", "").length();
		for (int i = 0; i <= tabs; i++) {
			if(tabs == 0){
				character = chain;
				cont++;
			}
			else{
				character = chain.substring(0,chain.indexOf(" ") + 1);
				chain = chain.substring(chain.indexOf(" ") + 1, chain.length());
				tabs--;
				i--;
				cont--;
			}
			if(!out.contains(character))
				out += character;
		}
		System.out.println("CLEARED " + cont);
		return out;
	}   

	public boolean getResult() {
		resultFlg = true;
		return resultFlg;
	}


	public void exportRslts(String name, String wordFile, ArrayList<ArrayList<String>> mainMtx) throws IOException {
		if(endFlg == true) {
			String tabs = "\t";
			PrintWriter pw = new PrintWriter(new FileWriter(name, true));
			pw.print(getResult());
			pw.println("\n");
			pw.println("The word used was: "+ wordFile);
			pw.println("\n");
			pw.println("End Matrix: ");
			pw.println("\n");
			for(int x = 0; x < mainMtx.size(); x++) { 
				pw.print(x + 1 + "\t");
			}
			pw.println("");
			for(int x = 0; x < mainMtx.size(); x++){
				if(x > 0){
					pw.print(tabs);
					tabs += "\t";
				}
				for(int y = 0; y < mainMtx.get(x).size(); y++) {
					pw.print(mainMtx.get(x).get(y) + "\t");
				}	
				pw.println(x + 1);
			}
			pw.println("\n");
			pw.println("Derivation Tree: ");
			pw.println(derivTree);
			pw.close();
		}else {
			String tabs = "\t";
			PrintWriter pw = new PrintWriter(new FileWriter(name, true));
			pw.println("CHAIN CANNOT BE DONE WITH THIS GRAMMAR");
			pw.println("\n");
			pw.println("The word used was: " + wordFile);
			pw.println("\n");
			pw.println("End Matrix: ");
			pw.println("\n");
			for(int x = 0; x < mainMtx.size(); x++) { 
				pw.print(x + 1 + "\t");
			}
			pw.println("");
			for(int x = 0; x < mainMtx.size(); x++){
				if(x > 0){
					pw.print(tabs);
					tabs += "\t";
				}
				for(int y = 0; y < mainMtx.get(x).size(); y++) {
					pw.print(mainMtx.get(x).get(y) + "\t");
				}	
				pw.println(x + 1);
			}
			pw.println("\n");
			pw.close();
		}
	}



	public static void main(String[]args) throws IOException {
		AlgoritmoCYK obj = new AlgoritmoCYK();
		int menuOpt = 0;
		int file;
		String exportName = "";
		do {
			UIManager.put("OptionPane.messageFont", new FontUIResource(new Font( "Verdana", Font.BOLD, 30)));       
			JOptionPane.showMessageDialog(null, "Select one option", "CYK", JOptionPane.INFORMATION_MESSAGE);
			UIManager.put("OptionPane.messageFont", new FontUIResource(new Font( "Verdana", Font.BOLD, 28)));  
			menuOpt = Integer.parseInt(JOptionPane.showInputDialog(null, "1 See Files\n2 Export Results\n3 Exit", "CYK", JOptionPane.INFORMATION_MESSAGE));
			switch (menuOpt) {
			case 1:
				UIManager.put("OptionPane.messageFont", new FontUIResource(new Font( "Verdana", Font.BOLD, 28)));  
				file = Integer.parseInt(JOptionPane.showInputDialog(null, "Select File\n 1)GRAMATICA1.txt", "CYK", JOptionPane.QUESTION_MESSAGE));
				if(file == 1) {
					File gramatica1 = new File("GRAMATICA1.txt");
					obj.gram = new GetGramatica(gramatica1);
					obj.controlM(obj.gram, obj.word);
				}
				break;

			case 2:
				UIManager.put("OptionPane.messageFont", new FontUIResource(new Font( "Verdana", Font.BOLD, 28)));  
				exportName = JOptionPane.showInputDialog(null, "Name your file", "CYK", JOptionPane.WARNING_MESSAGE);
				obj.exportRslts(exportName,obj.word,obj.mainMtrx);
				JOptionPane.showMessageDialog(null, "Results have been exported to\n" + exportName, "CYK",JOptionPane.INFORMATION_MESSAGE);
				break;

			case 3:
				menuOpt = 3;
				JOptionPane.showMessageDialog(null, "Exiting...","CYK",JOptionPane.OK_CANCEL_OPTION);
				break;
			}

		}while(menuOpt != 3);

	}

}