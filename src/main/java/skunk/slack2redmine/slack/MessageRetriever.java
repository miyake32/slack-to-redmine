package skunk.slack2redmine.slack;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsHistoryRequest;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.model.Channel;
import com.github.seratch.jslack.api.model.Message;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.slack.model.SlackSource;

@Slf4j
public class MessageRetriever implements SlackSourceRetriever {
	private String token;
	private UsernameResolver usernameResolver;

	private MessageRetriever() {
		super();
	}

	public MessageRetriever(String token, UsernameResolver usernameResolver) {
		this();
		this.token = token;
		this.usernameResolver = usernameResolver;
	}

	public Set<Message> getMessages(Collection<String> channelNames) throws IOException, SlackApiException {
		Slack slack = Slack.getInstance();

		Set<Channel> channels = slack.methods().channelsList(ChannelsListRequest.builder().token(token).build())
				.getChannels().stream().filter(c -> channelNames.contains(c.getName())).collect(Collectors.toSet());

		Set<Message> ret = new HashSet<>();
		for (Channel channel : channels) {
			log.info("retrieve files from {}", channel);
			List<Message> messages = slack.methods()
					.channelsHistory(
							ChannelsHistoryRequest.builder().channel(channel.getId()).token(token).count(1000).build())
					.getMessages();
			if (Objects.isNull(messages)) {
				continue;
			}
			ret.addAll(messages);
		}
		return ret;
	}

	public skunk.slack2redmine.slack.model.Message wrapMessage(Message message) {
		String username = usernameResolver.resolve(message.getUser());
		return new skunk.slack2redmine.slack.model.Message(message, username);
	}

	@Override
	public List<SlackSource> getSlackSources(Collection<String> channelNames) throws IOException, SlackApiException {
		if (!usernameResolver.isReady()) {
			usernameResolver.retrieveUserInfo();
		}
		return getMessages(channelNames).stream().map(this::wrapMessage).filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}