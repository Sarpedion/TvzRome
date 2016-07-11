package hr.tvz.rome.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.tvz.rome.exceptions.SomethingWentWrongException;
import hr.tvz.rome.model.TimePresentation;
import hr.tvz.rome.repository.TimePresentationRepository;

@Service
public class TimePresentationService {
	
	private TimePresentationRepository timePresentationRepository;

	@Autowired
	public TimePresentationService(TimePresentationRepository timePresentationRepository){
		this.timePresentationRepository = timePresentationRepository;
	}
	
	/**
	 * get dataPresentation from localDate
	 * @param date
	 * @return
	 */
	public TimePresentation getTimePresentation(LocalDateTime dateTime){
		return getTimePresentation(dateTime.toLocalTime());
	}
	
	/**
	 * get dataPresentation from localDate
	 * @param date
	 * @return
	 */
	public TimePresentation getTimePresentation(LocalTime time){
		
		List<TimePresentation> timePresentation = timePresentationRepository.findByHourAndMinute(time.getHour(), time.getMinute());
		if (timePresentation != null && !timePresentation.isEmpty()){
			if (timePresentation.size() == 1){
				return timePresentation.get(0);
			}else{
				throw new SomethingWentWrongException();
			}
		}else{
			return fillTimePresentation(time);
		}
	}
	/**
	 * fill time presentation and returns time
	 * @param time
	 * @return
	 */
	private TimePresentation fillTimePresentation(LocalTime time) {
		fillTimePresentation();
		return getTimePresentation(time);
	}

	/**
	 * fill time presentation (24 hours)
	 */
	private void fillTimePresentation() {
		// one time only, fill all hours
		LocalTime localTime = LocalTime.MIN;
		do{
			timePresentationRepository.save(fromLocalTime(localTime));
			localTime = localTime.plusMinutes(1);
		}while(!localTime.equals(LocalTime.MIN));
		
	}

	/**parse timePresentation to LocalTime
	 * 
	 * @param timePresentation
	 * @return
	 */
		private static LocalTime toLocalTime(TimePresentation timePresentation) {
			return LocalTime.of(timePresentation.getHour(), timePresentation.getMinute());
		}
	/**parse localTime to TimePresentation
	 * 
	 * @param localTime
	 * @return
	 */
		private static TimePresentation fromLocalTime(LocalTime localTime) {
			TimePresentation timePresentation = new TimePresentation();
			timePresentation.setHour(localTime.getHour());
			timePresentation.setMinute(localTime.getMinute());
			return timePresentation;
		}

		//returns minimum time presentation
	public TimePresentation getZeroTimePresentation() {
		return getTimePresentation(LocalTime.MIN);
	}
		
}
