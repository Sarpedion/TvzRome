package hr.tvz.rome.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.rome.exceptions.BadRequestException;
import hr.tvz.rome.exceptions.SomethingWentWrongException;
import hr.tvz.rome.model.DatePresentation;
import hr.tvz.rome.repository.DatePresentationRepository;

@Service
public class DatePresentationService {

	
	private DatePresentationRepository datePresentationRepository;
	@Autowired
	public DatePresentationService(DatePresentationRepository datePresentationRepository){
		this.datePresentationRepository = datePresentationRepository;
	}
	
	/**
	 * get dataPresentation from localDate
	 * @param date
	 * @return
	 */
	public DatePresentation getDatePresentation(LocalDateTime dateTime){
		return getDatePresentation(dateTime.toLocalDate());
	}
	
	
	/**
	 * get dataPresentation from localDate
	 * @param date
	 * @return
	 */
	public DatePresentation getDatePresentation(LocalDate date){
		
		List<DatePresentation> datePresentation = datePresentationRepository.findByDayAndMonthAndYear(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
		if (datePresentation != null && !datePresentation.isEmpty()){
			if (datePresentation.size() == 1){
				return datePresentation.get(0);
			}else{
				throw new SomethingWentWrongException();
			}
		}else{
			return fillDatePresentation(date);
		}
	}
	
	/**
	 * fetch existing dataPresentation from localDate
	 * @param date
	 * @return
	 */
	public DatePresentation fetchDatePresentation(LocalDate date){
		
		List<DatePresentation> datePresentation = datePresentationRepository.findByDayAndMonthAndYear(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
		if (datePresentation != null && !datePresentation.isEmpty()){
			if (datePresentation.size() == 1){
				return datePresentation.get(0);
			}
		}
		return null;
	}
/**fill missing dataPresentations in table
 * 
 * @param date
 * @return
 */
	private DatePresentation fillDatePresentation(LocalDate date) {
		if(dateIsRegular(date)){
			DatePresentation datePresentation = datePresentationRepository.findFirstByOrderByIdDesc();
			if(datePresentation == null){
				datePresentation = fromLocalDate(LocalDate.now());
				//it is empty so it's the first day
				datePresentation.setDayOfStart(1);
				datePresentationRepository.save(datePresentation);
			}
			//fill other days
			while(toLocalDate(datePresentation).isBefore(date)){
				Integer dayOfStart = datePresentation.getDayOfStart();
				datePresentation = fromLocalDate( toLocalDate(datePresentation).plusDays(1));
				datePresentation.setDayOfStart(++dayOfStart);
				datePresentationRepository.save(datePresentation);
			}
			return datePresentation;
		}
		else throw new BadRequestException();
	}
	
	/**
	 * Create new dataPresentation from localDate
	 * @param date
	 * @return
	 */
public DatePresentation fromLocalDate(LocalDate date) {
		DatePresentation datePresentation = new DatePresentation();
		datePresentation.setDay(date.getDayOfMonth());
		datePresentation.setMonth(date.getMonthValue());
		datePresentation.setYear(date.getYear());
		datePresentation.setDayOfYear(date.getDayOfYear());
		datePresentation.setRemark(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
		datePresentation.setHoliday(DayOfWeek.SATURDAY.equals(date.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(date.getDayOfWeek()));
		return datePresentation;
	}

/**parse datePresentation to LocalDate
 * 
 * @param datePresentation
 * @return
 */
	public LocalDate toLocalDate(DatePresentation datePresentation) {
		return LocalDate.of(datePresentation.getYear(), datePresentation.getMonth(), datePresentation.getDay());
	}
	
	public LocalDateTime toLocalDateTime(DatePresentation datePresentation) {

		return LocalDateTime.of(toLocalDate(datePresentation), LocalTime.MIN);
	}

/**check if date is regular (between now - 1 month and now + 1 month)
 *  
 * @param date
 * @return
 */
	private static boolean dateIsRegular(LocalDate date) {
		return date!= null && LocalDate.now().minusMonths(1).isBefore(date) && date.isBefore(LocalDate.now().plusMonths(1));
	}

public DatePresentation fetchDatePresentation(LocalDate date, Boolean smallerLargerExact) {
	DatePresentation datePresentation = fetchDatePresentation(date);
	if (Boolean.TRUE.equals(smallerLargerExact)){
		//true, return smaller
		datePresentation = datePresentationRepository.findFirstByDayLessThanEqualAndMonthLessThanEqualAndYearLessThanEqualOrderByIdDesc(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
		if (datePresentation != null){
			return datePresentation;
		}else{
			return datePresentationRepository.findFirstByOrderByIdAsc();
		}
		
	}
	else if (Boolean.FALSE.equals(smallerLargerExact)){
		//false, return larger
		datePresentation = datePresentationRepository.findFirstByDayGreaterThanEqualAndMonthGreaterThanEqualAndYearGreaterThanEqualOrderByIdAsc(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
		if (datePresentation != null){
			return datePresentation;
		}else{
			return datePresentationRepository.findFirstByOrderByIdDesc();
		}
	}
	else{
		//null, return exact
		return datePresentation;
	}
}
/**
 * fetch today date presentation
 * @param offset
 * @return
 */
public DatePresentation fetchToday(int offset) {
	if (offset > 0){
		return fetchDatePresentation(LocalDate.now().plusDays(offset), null);	
	}
	else if (offset < 0){
		return fetchDatePresentation(LocalDate.now().minusDays(Math.abs(offset)), null);	
	}
	else{
		return fetchDatePresentation(LocalDate.now(), null);
	}
}

public List<DatePresentation> fetchWorkingDaysBetween(LocalDate start, LocalDate end) {
	//first create all days till end if they don't exist
	DatePresentation _end = this.getDatePresentation(end);
	DatePresentation _start = this.getDatePresentation(start);
	return datePresentationRepository.findByDayOfStartGreaterThanEqualAndDayOfStartLessThanEqualAndHoliday(_start.getDayOfStart(), _end.getDayOfStart(), false);
}



	
}
