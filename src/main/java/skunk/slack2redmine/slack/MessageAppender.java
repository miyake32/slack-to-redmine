package skunk.slack2redmine.slack;

import java.io.IOException;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest.ChatPostMessageRequestBuilder;
import com.github.seratch.jslack.api.methods.request.files.comments.FilesCommentsAddRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.github.seratch.jslack.api.methods.response.files.comments.FilesCommentsAddResponse;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.slack.model.Message;
import skunk.slack2redmine.slack.model.SlackSource;
import skunk.slack2redmine.slack.model.TextFile;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@Slf4j
public class MessageAppender {
	private String token;

	private MessageAppender() {
		super();
	}

	public MessageAppender(String token) {
		this();
		this.token = token;
	}

	public void appendMessage(TextFile file, String message) throws IOException, SlackApiException {
		Slack slack = Slack.getInstance();
		FilesCommentsAddResponse res = slack.methods().filesCommentsAdd(
				FilesCommentsAddRequest.builder().token(token).comment(message).file(file.getId()).build());
		if (!res.isOk()) {
			log.error("error occurred while appending message. [error:{},warning:{}]", res.getError(),
					res.getWarning());
		}
	}

	public void appendMessage(Message message, String text) throws IOException, SlackApiException {
		Slack slack = Slack.getInstance();
		ChatPostMessageResponse res = slack.methods().chatPostMessage(ChatPostMessageRequest.builder().token(token)
				.text(text).threadTs(message.get().getTs()).channel(message.get().getChannel()).build());
		if (!res.isOk()) {
			log.error("error occurred while appnding message. [error:{},warning:{}]", res.getError(),
					res.getWarning());
		}
	}

	public void appendMessage(SlackSource source, String message) throws IOException, SlackApiException {
		if (source.getType() == SlackSourceType.FILE) {
			appendMessage((TextFile) source, message);
		}
		else if (source.getType() == SlackSourceType.MESSAGE) {
			appendMessage((Message) source, message);
		} else {
			throw new IllegalArgumentException("invalid type of source");
		}
	}
}
