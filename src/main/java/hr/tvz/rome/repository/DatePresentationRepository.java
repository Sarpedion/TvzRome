package hr.tvz.rome.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.tvz.rome.model.DatePresentation;
import java.lang.Integer;
import java.util.List;

public interface DatePresentationRepository extends JpaRepository<DatePresentation, Long> {

	List<DatePresentation> findByDayAndMonthAndYear(Integer day, Integer month, Integer year);
	DatePresentation findFirstByDayGreaterThanEqualAndMonthGreaterThanEqualAndYearGreaterThanEqualOrderByIdAsc(Integer day, Integer month, Integer year);
	DatePresentation findFirstByDayLessThanEqualAndMonthLessThanEqualAndYearLessThanEqualOrderByIdDesc(Integer day, Integer month, Integer year);
	DatePresentation findFirstByOrderByIdDesc();
	DatePresentation findFirstByOrderByIdAsc();
	
	List<DatePresentation> findByDayOfStartGreaterThanEqualAndDayOfStartLessThanEqualAndHoliday(Integer start, Integer end, Boolean holiday);
	
}
