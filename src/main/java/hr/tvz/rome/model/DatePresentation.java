package hr.tvz.rome.model;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DatePresentation implements Comparable<DatePresentation>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	Integer dayOfStart;
	Integer dayOfYear;

	Integer day;
	Integer month;
	Integer year;

	Boolean holiday;
	String remark;

	public long getId() {
		return id;
	}

	public Integer getDayOfStart() {
		return dayOfStart;
	}

	public void setDayOfStart(Integer dayOfStart) {
		this.dayOfStart = dayOfStart;
	}

	public Integer getDayOfYear() {
		return dayOfYear;
	}

	public void setDayOfYear(Integer dayOfYear) {
		this.dayOfYear = dayOfYear;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Boolean getHoliday() {
		return holiday;
	}

	public void setHoliday(Boolean holiday) {
		this.holiday = holiday;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(remark);
		sb.append(", ");
		sb.append(day);
		sb.append(".");
		sb.append(month);
		sb.append(".");
		sb.append(year);
		return sb.toString();
	}

	@Override
	public int compareTo(DatePresentation other) {
		return this.getDayOfStart() - other.getDayOfStart();
	}
}
