package skunk.slack2redmine.slack.model;

import skunk.slack2redmine.slack.model.type.SlackSourceType;

public interface SlackSource {
	public SlackSourceType getType();
	public String getContent();
	public String getUser();
	public String getFileName();
	public String getId();
}
