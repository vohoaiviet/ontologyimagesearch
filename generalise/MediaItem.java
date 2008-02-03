package generalise;

import java.util.Vector;

public class MediaItem {

	private String name;
	private Vector<String> keywords;
	
	public MediaItem(String name, Vector<String> keyWords){
		this.name=name;
		this.keywords=keyWords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Vector<String> keywords) {
		this.keywords = keywords;
	}
}
