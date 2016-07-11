package hr.tvz.rome.service.evidence;

import hr.tvz.rome.controllers.entities.EvidenceRequest;
import hr.tvz.rome.model.*;
import hr.tvz.rome.model.decorators.EvidenceDecorator;
import hr.tvz.rome.repository.*;
import hr.tvz.rome.service.DatePresentationService;
import hr.tvz.rome.service.TimePresentationService;
import hr.tvz.rome.utilities.DateTimeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Marko on 11.6.2016..
 */
@Service
public class EvidenceServiceImpl implements EvidenceService {

    private static final String prijava = "Prijava";
    private static final String odjava = "Odjava";
    private static final String godisnji = "GO";

    @Autowired
    private TimePresentationService timePresentationService;
    
    @Autowired
    private DatePresentationService datePresentationService;
    
    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private EvidenceTypeRepository evidenceTypeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EvidenceRepository evidenceRepository;

    @Override
    public EvidenceNew create(EvidenceRequest request) {
        Employee employee = employeesRepository.findByUsername(request.getUsername());

        EvidenceType evidenceType = evidenceTypeRepository.findByName(request.getType());

        Project project = projectRepository.findByName(request.getProject());
        if (project == null) {
            project = new Project(request.getProject());
            project = projectRepository.saveAndFlush(project);
        }

        Location location = locationRepository.findByName(request.getLocation());
        if (location == null) {
            location = new Location(request.getLocation());
            location = locationRepository.saveAndFlush(location);
        }

        String uniqueId;

        if (evidenceType!= null && evidenceType.getName().equals("Odjava")) {
            uniqueId = request.getUniqueId();
        } else {
            uniqueId = UUID.randomUUID().toString();
        }
        LocalDateTime timestamp = LocalDateTime.now();
        EvidenceNew evidenceNew = new EvidenceNew(employee, project, location, timestamp, evidenceType, uniqueId,timePresentationService.getTimePresentation(timestamp), datePresentationService.getDatePresentation(timestamp));

        return evidenceRepository.saveAndFlush(evidenceNew);
    }

    @Override
    public List<EvidenceDecorator> findAll() {

        return createFromDatabaseList(evidenceRepository.findAll());
    }

    @Override
    public List<EvidenceDecorator> findByUsername(String username) {
        Employee employee = employeesRepository.findByUsername(username);
        if (employee == null) {
            return null;
        }
        return createFromDatabaseList(evidenceRepository.findByEmployee(employee));
    }

    @Override
    public List<EvidenceDecorator> findToday() {
        return createFromDatabaseList(evidenceRepository.findByTimestampGreaterThanEqual(DateTimeBuilder.fromDateTime(DateTimeBuilder.now().buildDateTime().getStartOfDay()).buildDate()));

    }

    @Override
    public EvidenceDecorator findLatestUserEvidence(String username) {
        Employee employee = employeesRepository.findByUsername(username);
        if (employee == null) {
            return null;
        }
        return new EvidenceDecorator(evidenceRepository.findFirstByEmployeeOrderByTimestampDesc(employee));
    }

    private List<EvidenceDecorator> createFromDatabaseList(List<EvidenceNew> evidenceNews){

        Map<String, EvidenceDecorator> evidenceMap = new HashMap<>();

        evidenceNews.forEach(evidence -> {

            if(evidence.getType() != null && evidence.getType().getName().equals(prijava)){
                evidenceMap.put(evidence.getUniqueId(), new EvidenceDecorator(evidence));
            } else if(evidenceMap.containsKey(evidence.getUniqueId())){
                evidenceMap.get(evidence.getUniqueId()).setSignOutTimestamp(evidence.getTimestamp());
            }
        });


        return new ArrayList<>(evidenceMap.values());
    }
    /**
     * Implementation of processing evidences by employee, last evidence and type
     */
    @Override
	public Map<EvidenceType, List<EvidenceNew>> processEvidenceByType(List<EvidenceNew> evidences){
    	Map<Employee, EvidenceNew> employeeEvidenceMap = new HashMap<Employee, EvidenceNew>();
    	Map<EvidenceType, List<EvidenceNew>> evidenceTypeEvidenceMap = new HashMap<EvidenceType, List<EvidenceNew>>();
    	//calculate last active evidence for employee
		for (EvidenceNew evidence : evidences){
			if (evidence.getEmployee() != null){
				if (!employeeEvidenceMap.containsKey(evidence.getEmployee())
					||
					(employeeEvidenceMap.get(evidence.getEmployee()).getDate().compareTo(evidence.getDate()) < 0) 
					||
					(employeeEvidenceMap.get(evidence.getEmployee()).getTime().compareTo(evidence.getTime()) < 0)
					||
					(employeeEvidenceMap.get(evidence.getEmployee()).getTimestamp().compareTo(evidence.getTimestamp()) < 0)
					){
					employeeEvidenceMap.put(evidence.getEmployee(), evidence);
				}
			}
		}
		for (Employee employee : employeeEvidenceMap.keySet()) {
			EvidenceNew evidence = employeeEvidenceMap.get(employee);
			if (!evidenceTypeEvidenceMap.containsKey(evidence.getType())){
				evidenceTypeEvidenceMap.put(evidence.getType(), new ArrayList<EvidenceNew>());
			}
			evidenceTypeEvidenceMap.get(evidence.getType()).add(evidence);
		}
		return evidenceTypeEvidenceMap;
	}

    /**
     * create evidence when employee is on vacation
     */
	@Override
	public List<EvidenceNew> createVacationEvidence(LocalDate start, LocalDate end, Long employeeId) {

		return createVacationEvidence(start, end, employeesRepository.findOne(employeeId));
	}

    /**
     * create evidence when employee is on vacation
     */
	@Override
	public List<EvidenceNew> createVacationEvidence(LocalDate start, LocalDate end, Employee employee) {
		List<EvidenceNew> evidences = new ArrayList<>();
		if (employee != null){
			EvidenceType type = evidenceTypeRepository.findByName(godisnji);
			TimePresentation timePresentation = timePresentationService.getZeroTimePresentation();
			List<DatePresentation> dates =  datePresentationService.fetchWorkingDaysBetween(start, end);
			for(DatePresentation date : dates){
				evidences.add(new EvidenceNew(employee, null, null,datePresentationService.toLocalDateTime(date), type, UUID.randomUUID().toString(),timePresentation, date));
			}
		}
		else {
			throw new IllegalArgumentException("Error creating vacation evidence; missing param employee.");
		}
		if (!evidences.isEmpty()){
			evidenceRepository.save(evidences);
			evidenceRepository.flush();
		}
		return evidences;
	}
}
