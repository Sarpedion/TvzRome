package hr.tvz.rome.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.tvz.rome.model.TimePresentation;

public interface TimePresentationRepository extends JpaRepository<TimePresentation, Long> {

	List<TimePresentation> findByHourAndMinute(Integer hour, Integer minute);

}
