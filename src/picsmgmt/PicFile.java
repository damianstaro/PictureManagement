package picsmgmt;

import java.util.Date;

public class PicFile implements Comparable<PicFile> {

	private String path;
	
	private char rating = '?';
	
	private PicTopic topic = null;

	private Date date = new Date();
	
	public PicFile() {
		
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public char getRating() {
		return rating;
	}

	public void setRating(char rating) {
		this.rating = rating;
	}

	public PicTopic getTopic() {
		return topic;
	}

	public void setTopic(PicTopic topic) {
		this.topic = topic;
	}

	public int compareTo(PicFile o) {
		return this.path.compareTo(((PicFile) o).path);
	}
	
}
