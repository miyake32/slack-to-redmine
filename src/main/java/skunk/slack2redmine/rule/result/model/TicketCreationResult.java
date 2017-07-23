package skunk.slack2redmine.rule.result.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import lombok.Data;
import lombok.ToString;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@Data
@ToString
public class TicketCreationResult {
	private LocalDateTime createdDateTime;
	private SlackSourceType sourceType;
	private String sourceId;
	private Integer ticketNo;

	public void setCreatedDateTime() {
		this.createdDateTime = LocalDateTime.now();
	}
	
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(createdDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
		sb.append('\t');
		sb.append(sourceType.toString());
		sb.append('\t');
		sb.append(sourceId);
		sb.append('\t');
		sb.append(ticketNo);
		return sb.toString();
	}

	public static TicketCreationResult deserialize(String line) {
		String[] values = line.split("\t");
		TicketCreationResult ret = new TicketCreationResult();

		ret.setCreatedDateTime(LocalDateTime.parse(values[0], DateTimeFormatter.ISO_DATE_TIME));

		String sourceTypeStr = values[1];
		SlackSourceType sourceType = SlackSourceType.of(sourceTypeStr);
		Objects.requireNonNull(sourceType, "source type in result is null");
		ret.setSourceType(sourceType);

		ret.setSourceId(values[2]);
		ret.setTicketNo(Integer.parseInt(values[3]));

		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TicketCreationResult other = (TicketCreationResult) obj;
		if (sourceId == null) {
			if (other.sourceId != null)
				return false;
		} else if (!sourceId.equals(other.sourceId))
			return false;
		if (sourceType != other.sourceType)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());
		result = prime * result + ((sourceType == null) ? 0 : sourceType.hashCode());
		return result;
	}
}
