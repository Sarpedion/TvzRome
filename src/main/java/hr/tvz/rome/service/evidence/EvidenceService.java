package hr.tvz.rome.service.evidence;

import hr.tvz.rome.controllers.entities.EvidenceRequest;
import hr.tvz.rome.model.Employee;
import hr.tvz.rome.model.EvidenceNew;
import hr.tvz.rome.model.EvidenceType;
import hr.tvz.rome.model.decorators.EvidenceDecorator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by Marko on 11.6.2016..
 */
public interface EvidenceService {

    EvidenceNew create(EvidenceRequest evidenceRequest);

    List<EvidenceDecorator> findAll();

    List<EvidenceDecorator> findByUsername(String username);

    List<EvidenceDecorator> findToday();

    EvidenceDecorator findLatestUserEvidence(String username);

	Map<EvidenceType, List<EvidenceNew>> processEvidenceByType(List<EvidenceNew> evidences);
	
	List<EvidenceNew> createVacationEvidence(LocalDate start, LocalDate end, Long employeeId);
	
	List<EvidenceNew> createVacationEvidence(LocalDate start, LocalDate end, Employee employee);

}
