package skunk.slack2redmine.slack;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.methods.request.files.FilesListRequest;
import com.github.seratch.jslack.api.model.Channel;
import com.github.seratch.jslack.api.model.File;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.slack.model.SlackSource;
import skunk.slack2redmine.slack.model.TextFile;

@Slf4j
public class TextFileRetriever implements SlackSourceRetriever {
	private String token;

	private TextFileRetriever() {
		super();
	}

	public TextFileRetriever(String token) {
		this();
		this.token = token;
	}

	public Set<File> getFiles(Collection<String> channelNames) throws IOException, SlackApiException {
		Slack slack = Slack.getInstance();

		Set<Channel> channels = slack.methods().channelsList(ChannelsListRequest.builder().token(token).build())
				.getChannels().stream().filter(c -> channelNames.contains(c.getName())).collect(Collectors.toSet());

		Set<File> ret = new HashSet<>();
		for (Channel channel : channels) {
			log.info("retrieve files from {}", channel);
			List<File> files = slack.methods()
					.filesList(FilesListRequest.builder().token(token).channel(channel.getId()).build()).getFiles();
			if (Objects.isNull(files)) {
				continue;
			}
			ret.addAll(files);
		}
		return ret;
	}

	public SlackSource download(File file) {
		if (!file.getFiletype().equals("text")) {
			return null;
		}
		String url = file.getUrlPrivateDownload();
		log.info("download {}. url: {}", file.getName(), url);
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpGet req = new HttpGet(url);
			req.setHeader("Authorization", "Bearer " + token);

			try (CloseableHttpResponse res = client.execute(req)) {
				HttpEntity entity = res.getEntity();
				String content = EntityUtils.toString(entity, StandardCharsets.UTF_8);
				return new TextFile(file, content);
			} catch (Exception e) {
				log.error("failed to download from {}", url, e);
			}
		} catch (Exception e) {
			log.error("failed to connect {}", url, e);
		}
		return null;
	}

	public List<SlackSource> getSlackSources(Collection<String> channelNames) throws IOException, SlackApiException {
		return getFiles(channelNames).stream().map(this::download).filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}
