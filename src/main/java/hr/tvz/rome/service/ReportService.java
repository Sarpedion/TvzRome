package hr.tvz.rome.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.rome.factory.ReportDataFactory;
import hr.tvz.rome.model.DatePresentation;
import hr.tvz.rome.model.EvidenceNew;
import hr.tvz.rome.model.EvidenceType;
import hr.tvz.rome.model.ReportDataset;
import hr.tvz.rome.model.ReportData;
import hr.tvz.rome.repository.EmployeesRepository;
import hr.tvz.rome.repository.EvidenceRepository;
import hr.tvz.rome.repository.EvidenceTypeRepository;
import hr.tvz.rome.service.evidence.EvidenceService;

@Service
public class ReportService {

	@Autowired
	private DatePresentationService datePresentationService;
	
	@Autowired
	private EvidenceTypeRepository evidenceTypeRepository;
	
	@Autowired
	private EmployeesRepository employeesRepository;
	
	@Autowired
	private EvidenceRepository evidenceRepository;
	
	@Autowired
	private EvidenceService evidenceService;
	
	@Autowired
	private ReportDataFactory reportDataFactory;
	/**
	 * public method, calculate start and end if not defined
	 * @param start
	 * @param end
	 * @return
	 */
	public Object getHoursReport(LocalDate start, LocalDate end){
		DatePresentation _start = getStartDatePresentation(start, end);
		DatePresentation _end = getEndDatePresentation(start, end);
		return getHoursReportJson(_start, _end);

	}
	
	/**
	 * public method, calculate start and end if not defined
	 * @param start
	 * @param end
	 * @return
	 */
	public Object getHoursReportByEvidenceType(LocalDate start, LocalDate end){
		DatePresentation _start = getStartDatePresentation(start, end);
		DatePresentation _end = getEndDatePresentation(start, end);
		return getHoursReportByEvidenceTypeJson(_start, _end);

	}
	
	public Object getPresenceReport(){
		List<ReportData> results = new ArrayList<ReportData>();
		List<EvidenceNew> evidences = evidenceRepository.findByDate(datePresentationService.fetchToday(0));
		Map<EvidenceType, List<EvidenceNew>> evidencesSortedByType = evidenceService.processEvidenceByType(evidences);
		Long employeesCount = employeesRepository.count();
		for (EvidenceType type : evidencesSortedByType.keySet()){
			results.add(reportDataFactory.createPieGraphData(type.getName(),evidencesSortedByType.get(type).size(), generateColorCode(type.getId())));
			employeesCount -= evidencesSortedByType.get(type).size();
		}
		if (employeesCount > 0){
			results.add(reportDataFactory.createPieGraphData("Nema prijave",employeesCount.intValue(), generateColorCode(1)));
		}
		return results;
	}
	

/**get Report presentation object (line/bar)
 * 
 * @param _start
 * @param _end
 * @return
 */
	private Object getHoursReportJson(DatePresentation _start, DatePresentation _end){
		ReportDataset data = new ReportDataset();
		ReportData dataset1 = new ReportData();
		Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
		dataset1.setLabel("Vrijeme");
		List<Object[]> rows = evidenceRepository.countByHours(_start.getId(),_end.getId());
		for (Object[] row : rows){
			resultMap.put(Integer.valueOf(row[1].toString()), Integer.valueOf(row[0].toString()));
		}
		for(int i = 0; i < 24; i++){
			//hour
			data.addLabel(i);
			//count
			dataset1.setData(resultMap.containsKey(i)?resultMap.get(i):0);
		}
		data.addDataset(dataset1);
		return data;
	}
	/**get Report presentation object (line/bar)
	 * 
	 * @param _start
	 * @param _end
	 * @return
	 */
	private Object getHoursReportByEvidenceTypeJson(DatePresentation _start, DatePresentation _end){
		ReportDataset data = new ReportDataset();
		Random r = new Random();
		int colourMax = 255;		
		//fill labels for data
		for(int i = 0; i < 24; i++){
			//hour
			data.addLabel(i + ":00h");
		}
		List<EvidenceType> evidenceTypes = evidenceTypeRepository.findAll();
		for (EvidenceType type : evidenceTypes){
			data.addSerie(type.getName());
			ReportData reportData = reportDataFactory.createBarGraphData( type.getName(),"rgba("+ r.nextInt(colourMax) + "," + r.nextInt(colourMax) + "," + r.nextInt(colourMax) + ",0.2)");
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			List<Object[]> rows = evidenceRepository.countByHoursConditionEvidenceType(_start.getId(),_end.getId(),type.getId());
			for (Object[] row : rows){
				//map result by hours
				resultMap.put(Integer.valueOf(row[1].toString()), Integer.valueOf(row[0].toString()));
			}
			for(int i = 0; i < 24; i++){
				//fill data
				reportData.setData(resultMap.containsKey(i)?resultMap.get(i):0);
			}
			data.addDataset(reportData);
		}
		return data;
	}
	/**
	 * calculate end date presentation
	 * @param start
	 * @param end
	 * @return
	 */
	private DatePresentation getEndDatePresentation(LocalDate start, LocalDate end) {
		if(end != null){
			return datePresentationService.fetchDatePresentation(end, true);
		}
		else if (start != null){
			return datePresentationService.fetchDatePresentation(start.plusMonths(1), true);
		}
		else{
			return datePresentationService.fetchDatePresentation(LocalDate.now(), true);
		}
	}
/**
 * calculate start date presentation
 * @param start
 * @param end
 * @return
 */
	private DatePresentation getStartDatePresentation(LocalDate start, LocalDate end) {
		if(start != null){
			return datePresentationService.fetchDatePresentation(start, false);
		}
		else if (end != null){
			return datePresentationService.fetchDatePresentation(end.minusMonths(1), false);
		}
		else{
			return datePresentationService.fetchDatePresentation(LocalDate.now().minusMonths(1), false);
		}
	}
	
    static String generateColorCode(long id)
    {
        String[] letters = new String[15];
        letters = "0123456789ABCDEF".split("");
        String code ="#";
        for(int i=0;i<6;i++)
        {
            long index =  (id*i) % 15;
            code += letters[(int)index]; 
        }
        return code;
    }
}
