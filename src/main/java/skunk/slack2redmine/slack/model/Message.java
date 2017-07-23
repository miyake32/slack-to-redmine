package skunk.slack2redmine.slack.model;

import lombok.AllArgsConstructor;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@AllArgsConstructor
public class Message extends SlackSource {
	private com.github.seratch.jslack.api.model.Message message;
	private String userName;

	@Override
	public SlackSourceType getType() {
		return SlackSourceType.MESSAGE;
	}

	@Override
	public String getContent() {
		return message.getText();
	}

	@Override
	public String getUser() {
		return userName;
	}

	@Override
	public String getFileName() {
		throw new UnsupportedOperationException("Message#getFileName is prohibited");
	}

	@Override
	public String getId() {
		return message.getTs();
	}
	
	public com.github.seratch.jslack.api.model.Message get() {
		return message;
	}

	@Override
	public String getSourceUrl() {
		throw new UnsupportedOperationException("Message#getSourceUrl is prohibited");
	}
}
