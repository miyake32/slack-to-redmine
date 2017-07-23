package skunk.slack2redmine.slack.model.type;

import java.util.Objects;

public enum SlackSourceType {
	FILE, MESSAGE;
	
	public static SlackSourceType of(String value) {
		if (Objects.isNull(value)) {
			return null;
		}
		if (value.equalsIgnoreCase("file")) {
			return FILE;
		}
		if (value.equalsIgnoreCase("message")) {
			return MESSAGE;
		}
		return null;
	}
}
