package skunk.slack2redmine.slack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import skunk.slack2redmine.slack.model.TextFile;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.methods.request.files.FilesListRequest;
import com.github.seratch.jslack.api.model.Channel;
import com.github.seratch.jslack.api.model.File;

public class TextFileRetriever {
	private String token;
	
	private TextFileRetriever(){
		super();
	}
	public TextFileRetriever(String token) {
		this();
		this.token = token;
	}
	
	public Set<File> getFiles(Collection<String> channelNames) throws IOException, SlackApiException {
		Slack slack = Slack.getInstance();

		Set<Channel> channels = slack
				.methods()
				.channelsList(
						ChannelsListRequest.builder().token(token).build())
				.getChannels().stream()
				.filter(c -> channelNames.contains(c.getName()))
				.collect(Collectors.toSet());

		Set<File> ret = new HashSet<>();
		for (Channel channel : channels) {
			List<File> files = slack
					.methods()
					.filesList(
							FilesListRequest.builder().token(token)
									.channel(channel.getId()).build())
					.getFiles();
			ret.addAll(files);
		}
		return ret;
	}
	
	public TextFile download(File file) {
		if (!file.getFiletype().equals("text")) {
			return null;
		}
		String url = file.getUrlPrivateDownload();
		try (CloseableHttpClient client = HttpClients.createDefault()){
			HttpGet req = new HttpGet(url);
			req.setHeader("Authorization", "Bearer " + token);
			
			try (CloseableHttpResponse res = client.execute(req)) {
				HttpEntity entity = res.getEntity();
				String content = EntityUtils.toString(entity);
				return new TextFile(file, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
