package picsmgmt;

import java.util.Date;

public class PicTopic {

	public static final PicTopic MISC = new PicTopic("Misc");

	private String name;
	
	private Date minDate;
	
	private Date maxDate;
	
	public PicTopic() {
		
	}
	
	public void process(PicFile f) {
		if (minDate == null) {
			minDate = f.getDate();
			maxDate = f.getDate();
		}
		if (f.getDate().after(maxDate))
			maxDate = f.getDate();
		if (f.getDate().before(minDate))
			minDate = f.getDate();
	}
	
	public PicTopic(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public String toString() {
		return name;
	}
}

