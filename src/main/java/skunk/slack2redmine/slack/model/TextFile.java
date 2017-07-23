package skunk.slack2redmine.slack.model;

import java.util.Objects;

import com.github.seratch.jslack.api.model.File;

import lombok.AllArgsConstructor;
import skunk.slack2redmine.slack.TextFileRetriever;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@AllArgsConstructor
public class TextFile extends SlackSource {
	private com.github.seratch.jslack.api.model.File file;
	private String content;
	private TextFileRetriever textFileRetriever;

	public TextFile(com.github.seratch.jslack.api.model.File file, TextFileRetriever textFileRetriever) {
		super();
		this.file = file;
		this.textFileRetriever = textFileRetriever;
		this.content = null;
	}
	public TextFile(com.github.seratch.jslack.api.model.File file, String content) {
		super();
		this.file = file;
		this.textFileRetriever = null;
		this.content = content;
	}
	public com.github.seratch.jslack.api.model.File getFile() {
		return file;
	}
	
	public String getContent() {
		if (Objects.isNull(content)) {
			content = textFileRetriever.download(file);
		}
		return content;
	}

	@Override
	public SlackSourceType getType() {
		return SlackSourceType.FILE;
	}

	@Override
	public String getUser() {
		return this.file.getUsername();
	}

	@Override
	public String getFileName() {
		return this.file.getName();
	}

	@Override
	public String getId() {
		return this.file.getId();
	}

	public File get() {
		return this.file;
	}

	@Override
	public String getSourceUrl() {
		return this.file.getPermalink();
	}
}
