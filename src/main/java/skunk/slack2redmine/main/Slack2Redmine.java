package skunk.slack2redmine.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import skunk.slack2redmine.redmine.IssueRegister;
import skunk.slack2redmine.slack.TextFileRetriever;
import skunk.slack2redmine.slack.model.TextFile;

import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.model.File;
import com.taskadapter.redmineapi.RedmineException;

public class Slack2Redmine {
	public static void main(String[] args) throws IOException,
			SlackApiException, RedmineException {
		ArgumentsHolder.readArgs(args);
		
		TextFileRetriever textFileRetriever = new TextFileRetriever(ArgumentsHolder.getSlackApiToken());
		Set<File> files = textFileRetriever.getFiles(Arrays.asList("autopost_exception"));
		File sample = files.stream().findAny().get();
		TextFile textFileSample = textFileRetriever.download(sample);
		
		IssueRegister issueRegister = null;
		switch (ArgumentsHolder.getRedmineAuthMethod()) {
		case API_TOKEN:
			issueRegister = new IssueRegister(ArgumentsHolder.getRedmineUrl(), ArgumentsHolder.getRedmineApiToken());
			break;
		case USER_PASSWORD:
			issueRegister = new IssueRegister(ArgumentsHolder.getRedmineUrl(), ArgumentsHolder.getRedmineUserName(), ArgumentsHolder.getRedminePassword());
			break;
		default:
			System.out.println("Insufficient redmine auth information");
			System.exit(1);
		}
		
		System.out.println(textFileSample);
	}
}
