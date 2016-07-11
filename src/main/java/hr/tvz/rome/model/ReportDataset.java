package hr.tvz.rome.model;

import java.util.ArrayList;
import java.util.List;

public class ReportDataset {

	List<Object> series = new ArrayList<Object>();
	List<Object> labels = new ArrayList<Object>();
	List<ReportData> datasets = new ArrayList<ReportData>();
	
	public List<Object> getSeries() {
		return series;
	}
	public void addSerie(Object serie) {
		this.series.add(serie);
	}
	public List<Object> getLabels() {
		return labels;
	}
	public void addLabel(Object label) {
		this.labels.add(label);
	}
	public List<ReportData> getDatasets() {
		return datasets;
	}
	public void addDataset(ReportData dataset) {
		this.datasets.add(dataset);
	}

}
