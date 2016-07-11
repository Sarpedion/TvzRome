package hr.tvz.rome.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class EvidenceNew {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade=CascadeType.MERGE)
    private Employee employee;
    @ManyToOne(cascade=CascadeType.MERGE)
    private DatePresentation date;
    @ManyToOne(cascade=CascadeType.MERGE)
    private TimePresentation time;
    @ManyToOne(cascade=CascadeType.MERGE)
    private EvidenceType type;
    @ManyToOne(cascade=CascadeType.MERGE)
    private EvidenceDirection direction;
    @ManyToOne(cascade=CascadeType.MERGE)
    private Project project;
	@ManyToOne(cascade=CascadeType.MERGE)
	private Location location;

	private String uniqueId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    //guest data
    private Boolean guest;
    private String guestFirstName;
    private String guestLastName;
    private String guestOrganizationName;

	public EvidenceNew() {
	}

	public EvidenceNew(Employee employee, Project project, Location location, LocalDateTime timestamp, EvidenceType type, String uniqueId, TimePresentation time, DatePresentation date) {
		this.employee = employee;
		this.project = project;
		this.location = location;
		this.timestamp = Date.from(timestamp.atZone(ZoneId.systemDefault()).toInstant());
		this.type = type;
		this.uniqueId = uniqueId;
		this.date = date;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public DatePresentation getDate() {
		return date;
	}

	public void setDate(DatePresentation date) {
		this.date = date;
	}

	public TimePresentation getTime() {
		return time;
	}

	public void setTime(TimePresentation time) {
		this.time = time;
	}

	public EvidenceType getType() {
		return type;
	}

	public void setType(EvidenceType type) {
		this.type = type;
	}

	public EvidenceDirection getDirection() {
		return direction;
	}

	public void setDirection(EvidenceDirection direction) {
		this.direction = direction;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getGuest() {
		return guest;
	}

	public void setGuest(Boolean guest) {
		this.guest = guest;
	}

	public String getGuestFirstName() {
		return guestFirstName;
	}

	public void setGuestFirstName(String guestFirstName) {
		this.guestFirstName = guestFirstName;
	}

	public String getGuestLastName() {
		return guestLastName;
	}

	public void setGuestLastName(String guestLastName) {
		this.guestLastName = guestLastName;
	}

	public String getGuestOrganizationName() {
		return guestOrganizationName;
	}

	public void setGuestOrganizationName(String guestOrganizationName) {
		this.guestOrganizationName = guestOrganizationName;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getUniqueId() {
		return uniqueId;
	}
}
