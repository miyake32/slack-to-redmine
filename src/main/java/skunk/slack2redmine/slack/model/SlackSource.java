package skunk.slack2redmine.slack.model;

import java.util.Objects;

import skunk.slack2redmine.rule.model.type.SlackSourceField;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

public abstract class SlackSource {
	public abstract SlackSourceType getType();

	public abstract String getContent();

	public abstract String getUser();

	public abstract String getFileName();

	public abstract String getId();

	public abstract String getSourceUrl();

	public String get(SlackSourceField field) {
		if (Objects.isNull(field)) {
			return null;
		}
		switch (field) {
		case CONTENT:
			return getContent();
		case FILENAME:
			return getFileName();
		case SOURCE_URL:
			return getSourceUrl();
		case USER:
			return getUser();
		}
		return null;
	}
}
