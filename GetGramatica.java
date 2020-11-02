import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class GetGramatica {

	public List<Llaves> prodList;
	boolean insertFlg = false;
	boolean contentFlg = false;

	public GetGramatica(){
		prodList = new ArrayList<Llaves>();
	}

	@SuppressWarnings("resource")
	public GetGramatica (File file) throws IOException {
		prodList = new ArrayList<Llaves>();
		String leftVal = "";
		String keyRightVal = "";
		int arrows = 0;
		int lineLenght = 0;
		int barsCount = 0;
		BufferedReader fileRead = new BufferedReader(new FileReader(file));
		String line = fileRead.readLine();
		@SuppressWarnings("unused")
		StringTokenizer st;
		while(line != null) {
			lineLenght = line.length();
			arrows = line.indexOf("-->");
			leftVal = line.substring(0, arrows);
			line = line.substring(arrows + 3, lineLenght);
			if(line.contains("||")) {
				barsCount = barsCounter(line);
			}else {
				barsCount = 0;
			}
			for (int x = 0; x <= barsCount; x++) {
				if(line.contains("||")) {
					keyRightVal = line.substring(0, line.indexOf("||"));
					addProduction(keyRightVal, leftVal);
					line = line.substring(line.indexOf("||") + 2, line.length());
				}else {
					keyRightVal = line;
					addProduction(keyRightVal, leftVal);
				}
			}
			line = fileRead.readLine();
		}
	}

	private int barsCounter(String line) {
		int count = 0;
		int lineLength = line.length();
		String search = "||";
		String tmp = "";
		for(int x = 0; x < lineLength; x = x + 2) {
			if(line.length() >= 2) {
				tmp = line.substring(0, 2);
				if(tmp.equals(search)) {
					count++;
				}
			}
			line = line.substring(2);
		}
		return count;
	}

	public void addProduction(String keyRightVal, String leftVal){
		insertFlg = true;
		insertKeyList(keyRightVal, leftVal);
	}

	public void insertKeyList(String keyRightVal, String leftVal) {
		boolean flag = false;
		if(prodList.isEmpty()) {
			Llaves key = new Llaves(keyRightVal);
			key.addElement(leftVal);
			prodList.add(key);
		}else {
			ListIterator<Llaves> it = prodList.listIterator();
			while(it.hasNext()) {
				Llaves node = it.next();
				if(node.key.equals(keyRightVal)) {
					node.addElement(leftVal);
					flag = true;
				}
			}
			if(flag == false) {
				Llaves key = new Llaves(keyRightVal);
				key.addElement(leftVal);
				prodList.add(key);
			}
		}
		insertFlg = false;
	}

	public String getContent(String keyRightVal) {
		contentFlg = true;
		String content = "";
		ListIterator<Llaves> it = prodList.listIterator();
		Llaves node;
		while(it.hasNext()) {
			node = it.next();
			if(node.key.equals(keyRightVal)) {
				content=node.getValues();
			}
		}
		return content;
	}
	
	public String getKey(String keyLeftVal) {
		contentFlg = true;
		String content = "";
		ListIterator<Llaves> it = prodList.listIterator();
		Llaves node;
		while(it.hasNext()) {
			node = it.next();
			if(node.getValues().equals(keyLeftVal)) {
				content = node.key;
			}
		}
		return content;
	}
}