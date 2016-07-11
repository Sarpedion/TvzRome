package hr.tvz.rome.factory;

import org.springframework.stereotype.Component;

import hr.tvz.rome.model.ReportData;
@Component
public class ReportDataFactory {

	public ReportData createLineGraphData(String label, String fillColor){
		ReportData reportData = new ReportData();
		reportData.setLabel( label);
		reportData.setFillColor(fillColor);
		return reportData;
	}
	
	public ReportData createBarGraphData(String label, String fillColor){
		ReportData reportData = new ReportData();
		reportData.setLabel( label);
		reportData.setFillColor(fillColor);
		return reportData;
	}
	
	public ReportData createPieGraphData(String label, int value, String color){
		ReportData reportData = new ReportData();
		reportData.setLabel(label);
		reportData.setValue(value);
		reportData.setColor(color);
		return reportData;
	}
}
