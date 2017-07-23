package skunk.slack2redmine.rule.model.type;

import java.util.Objects;

public enum RedmineTicketField {
	SUBJECT, DESCRIPTION, NOTES;
	
	public static RedmineTicketField of(String field) {
		if (Objects.isNull(field)) {
			return null;
		}
		if (field.equalsIgnoreCase("subject")) {
			return SUBJECT;
		}
		if (field.equalsIgnoreCase("description")) {
			return DESCRIPTION;
		}
		if (field.equalsIgnoreCase("notes")) {
			return NOTES;
		}
		return null;
 	}
}
