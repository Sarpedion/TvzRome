package hr.tvz.rome.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TimePresentation implements Comparable<TimePresentation>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Integer hour;
	private Integer minute;

	public long getId() {
		return id;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	@Override
	public String toString() {
		return hour + ":" + minute;
	}

	public int compareTo(TimePresentation time) {
		if (time.getHour() != this.getHour()){
			return this.getHour() -  time.getHour();
		}else{
			return this.getMinute() - time.getMinute();
		}
	}

}
