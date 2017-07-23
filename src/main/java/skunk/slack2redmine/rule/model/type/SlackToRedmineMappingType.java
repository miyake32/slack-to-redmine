package skunk.slack2redmine.rule.model.type;

import java.util.Objects;

public enum SlackToRedmineMappingType {
	REGEX_MATCH, FIXED_TEXT;
	
	public static SlackToRedmineMappingType of(String value) {
		if (Objects.isNull(value)) {
			return null;
		}
		if (value.equalsIgnoreCase("regex_match") || value.equalsIgnoreCase("regexmatch")) {
			return REGEX_MATCH;
		}
		if (value.equalsIgnoreCase("fixed_text") || value.equalsIgnoreCase("fixedtext")) {
			return FIXED_TEXT;
		}
		return null;
	}
}
