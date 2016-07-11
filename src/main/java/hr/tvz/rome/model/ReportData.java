package hr.tvz.rome.model;

import java.util.ArrayList;
import java.util.List;

public class ReportData {

	String label;
	int value;
	String color;
    String fillColor;
    String strokeColor;
    String pointColor;
    String pointStrokeColor;
    String pointHighlightFill;
    String pointHighlightStroke;
	List<Object> data = new ArrayList<Object>();
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(Object data) {
		this.data.add(data);
	}
	public String getFillColor() {
		return fillColor;
	}
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}
	public String getStrokeColor() {
		return strokeColor;
	}
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}
	public String getPointColor() {
		return pointColor;
	}
	public void setPointColor(String pointColor) {
		this.pointColor = pointColor;
	}
	public String getPointStrokeColor() {
		return pointStrokeColor;
	}
	public void setPointStrokeColor(String pointStrokeColor) {
		this.pointStrokeColor = pointStrokeColor;
	}
	public String getPointHighlightFill() {
		return pointHighlightFill;
	}
	public void setPointHighlightFill(String pointHighlightFill) {
		this.pointHighlightFill = pointHighlightFill;
	}
	public String getPointHighlightStroke() {
		return pointHighlightStroke;
	}
	public void setPointHighlightStroke(String pointHighlightStroke) {
		this.pointHighlightStroke = pointHighlightStroke;
	}
}
