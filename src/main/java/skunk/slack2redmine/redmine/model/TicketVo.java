package skunk.slack2redmine.redmine.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TicketVo {
	private String subject;
	private String description;
	private String tracker;
	private String project;
	private String notes;
	private Integer parentTicketNo;
}
