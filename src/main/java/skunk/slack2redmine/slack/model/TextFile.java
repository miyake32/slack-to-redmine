package skunk.slack2redmine.slack.model;

public class TextFile {
	private com.github.seratch.jslack.api.model.File file;
	private String content;
	
	public com.github.seratch.jslack.api.model.File getFile() {
		return file;
	}
	public void setFile(com.github.seratch.jslack.api.model.File file) {
		this.file = file;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public TextFile(com.github.seratch.jslack.api.model.File file, String content) {
		super();
		this.file = file;
		this.content = content;
	}
	
}
