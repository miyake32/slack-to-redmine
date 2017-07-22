package skunk.slack2redmine.slack.model;

import com.github.seratch.jslack.api.model.File;

import lombok.AllArgsConstructor;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@AllArgsConstructor
public class TextFile implements SlackSource {
	private com.github.seratch.jslack.api.model.File file;
	private String content;
	
	public com.github.seratch.jslack.api.model.File getFile() {
		return file;
	}
	public String getContent() {
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
	
	
	
}
