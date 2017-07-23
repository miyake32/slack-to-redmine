package skunk.slack2redmine.rule.model;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import skunk.slack2redmine.rule.model.type.SlackSourceField;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class SlackSourceCondition {
	private SlackSourceField field;
	private String regex;

	public boolean isValid(SlackSourceType type) {
		Objects.requireNonNull(type);
		if (type != SlackSourceType.FILE) {
			if (field == SlackSourceField.FILENAME || field == SlackSourceField.SOURCE_URL) {
				return false;
			}
		}
		return Objects.nonNull(field) && Objects.nonNull(regex) && !regex.isEmpty();
	}
}
