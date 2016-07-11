package hr.tvz.rome.repository;

import hr.tvz.rome.model.DatePresentation;
import hr.tvz.rome.model.Employee;
import hr.tvz.rome.model.EvidenceNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EvidenceRepository extends JpaRepository<EvidenceNew, Long> {

    List<EvidenceNew> findByEmployee(Employee employee);
    List<EvidenceNew> findByTimestampGreaterThanEqual(Date date);
    EvidenceNew findFirstByEmployeeOrderByTimestampDesc(Employee employee);
    @Query(value="SELECT COUNT(1), tp.hour FROM EVIDENCE_NEW evd left join TIME_PRESENTATION tp ON evd.time_id = tp.id WHERE evd.date_id >=:start AND evd.date_id <=:end GROUP BY tp.hour ORDER BY tp.hour", nativeQuery = true)
    List<Object[]> countByHours(@Param("start") long start,@Param("end") long end);
    @Query(value="SELECT COUNT(1), tp.hour FROM EVIDENCE_NEW evd left join TIME_PRESENTATION tp ON evd.time_id = tp.id WHERE evd.date_id >=:start AND evd.date_id <=:end AND evd.type_id =:evidenceType GROUP BY tp.hour ORDER BY tp.hour", nativeQuery = true)
    List<Object[]> countByHoursConditionEvidenceType(@Param("start") long start,@Param("end") long end, @Param("evidenceType") long evidenceType);
    @Query(value="SELECT COUNT(1), tp.hour, dp.day FROM EVIDENCE_NEW evd left join TIME_PRESENTATION tp ON evd.time_id = tp.id left join DATE_PRESENTATION dp ON evd.date_id = dp.id WHERE evd.date_id >=:start AND evd.date_id <=:end GROUP BY tp.hour, dp.day", nativeQuery = true)
    List<Object[]> countByHoursAndDays(@Param("start") long start,@Param("end") long end);
	List<EvidenceNew> findByDate(DatePresentation date);

}
