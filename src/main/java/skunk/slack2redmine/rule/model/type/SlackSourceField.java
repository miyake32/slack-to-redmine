package skunk.slack2redmine.rule.model.type;

import java.util.Objects;

public enum SlackSourceField {
	FILENAME, CONTENT, USER, SOURCE_URL;
	
	public static SlackSourceField of(String value) {
		if (Objects.isNull(value)) {
			return null;
		}
		if (value.equalsIgnoreCase("filename")) {
			return FILENAME;
		}
		if (value.equalsIgnoreCase("content")) {
			return CONTENT;
		}
		if (value.equalsIgnoreCase("user")) {
			return USER;
		}
		if (value.equalsIgnoreCase("source_url")) {
			return SOURCE_URL;
		}
		return null;
	}
}
