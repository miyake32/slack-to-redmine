package skunk.slack2redmine.slack;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.github.seratch.jslack.api.methods.SlackApiException;

import skunk.slack2redmine.slack.model.SlackSource;

public interface SlackSourceRetriever {
	public List<SlackSource> getSlackSources(Collection<String> channelNames) throws IOException, SlackApiException;
}
