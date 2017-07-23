package skunk.slack2redmine.rule.model;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import skunk.slack2redmine.rule.model.type.RedmineTicketField;
import skunk.slack2redmine.rule.model.type.SlackSourceField;
import skunk.slack2redmine.rule.model.type.SlackToRedmineMappingType;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class SlackToRedmineMappingRule {
	private SlackToRedmineMappingType type;
	private String regex;
	private String text;
	private SlackSourceField source;
	private RedmineTicketField destination;

	public boolean isValid(SlackSourceType type) {
		Objects.requireNonNull(type);
		if (type != SlackSourceType.FILE && source == SlackSourceField.FILENAME) {
			if (source == SlackSourceField.FILENAME || source == SlackSourceField.SOURCE_URL) {
				return false;
			}
		}
		if (this.type == SlackToRedmineMappingType.FIXED_TEXT) {
			return Objects.nonNull(text) && !text.isEmpty() && Objects.nonNull(destination);
		}
		if (this.type == SlackToRedmineMappingType.REGEX_MATCH) {
			return Objects.nonNull(regex) && !regex.isEmpty() && Objects.nonNull(destination)
					&& Objects.nonNull(source);
		}
		return false;
	}
}
