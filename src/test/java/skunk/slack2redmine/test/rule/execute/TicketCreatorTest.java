package skunk.slack2redmine.test.rule.execute;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.github.seratch.jslack.api.model.File;

import skunk.slack2redmine.redmine.model.TicketVo;
import skunk.slack2redmine.rule.execute.TicketCreator;
import skunk.slack2redmine.rule.model.SlackSourceCondition;
import skunk.slack2redmine.rule.model.SlackToRedmineMappingRule;
import skunk.slack2redmine.rule.model.TicketCreationRule;
import skunk.slack2redmine.rule.model.type.RedmineTicketField;
import skunk.slack2redmine.rule.model.type.SlackSourceField;
import skunk.slack2redmine.rule.model.type.SlackToRedmineMappingType;
import skunk.slack2redmine.slack.model.SlackSource;
import skunk.slack2redmine.slack.model.TextFile;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

public class TicketCreatorTest {
	private static final SlackSource textFile = new TextFile(
			File.builder().username("miyake_yut").filetype("text").name("sample_text_file_1")
					.urlPrivateDownload("https://sample.download.com/download").build(),
			"Content of file\r\nHogeFugaError occurred while running process\r\n\r\nPlease check it.\r\nBefore HogeFugaError, SPAM happened.");
	private static TicketCreationRule getPositiveRule1() {
		TicketCreationRule rule = new TicketCreationRule(SlackSourceType.FILE, 999999, "Task(タスク)", "Expense Reimbursement Report Management");
		rule.addSlackSourceCondition(new SlackSourceCondition(SlackSourceField.CONTENT, "[SPAM]{4} happen"));
		rule.addSlackSourceCondition(new SlackSourceCondition(SlackSourceField.FILENAME, "sample_text_file_1"));
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.REGEX_MATCH, "running [a-z]+", "fixed text", SlackSourceField.CONTENT, RedmineTicketField.SUBJECT));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.FIXED_TEXT, null, " fixed text1", null, RedmineTicketField.SUBJECT));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.REGEX_MATCH, "(?s)^.*$", null, SlackSourceField.CONTENT, RedmineTicketField.DESCRIPTION));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.FIXED_TEXT, null, "fixed text1", null, RedmineTicketField.NOTES));;
		return rule;
	}
	private static TicketCreationRule getPositiveRule2() {
		TicketCreationRule rule = new TicketCreationRule(SlackSourceType.FILE, 999999, "Task(タスク)", "Expense Reimbursement Report Management");
		rule.addSlackSourceCondition(new SlackSourceCondition(SlackSourceField.CONTENT, "[SPAM]{4} happen"));
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.REGEX_MATCH, "running [a-z]+", "fixed text", SlackSourceField.CONTENT, RedmineTicketField.SUBJECT));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.FIXED_TEXT, null, " fixed text2", null, RedmineTicketField.SUBJECT));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.REGEX_MATCH, "(?s)^.*$", null, SlackSourceField.CONTENT, RedmineTicketField.DESCRIPTION));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.FIXED_TEXT, null, "fixed text2", null, RedmineTicketField.NOTES));;
		return rule;
	}
	private static TicketCreationRule getNegativeRule1() {
		TicketCreationRule rule = new TicketCreationRule(SlackSourceType.FILE, 999999, "Task(タスク)", "Expense Reimbursement Report Management");
		rule.addSlackSourceCondition(new SlackSourceCondition(SlackSourceField.CONTENT, "[SPAM]{4} happen"));
		rule.addSlackSourceCondition(new SlackSourceCondition(SlackSourceField.CONTENT, "this character sequence doesn't appear in content"));
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.REGEX_MATCH, "running [a-z]+", "fixed text", SlackSourceField.CONTENT, RedmineTicketField.SUBJECT));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.FIXED_TEXT, null, " fixed text3", null, RedmineTicketField.SUBJECT));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.REGEX_MATCH, "(?s)^.*$", null, SlackSourceField.CONTENT, RedmineTicketField.DESCRIPTION));;
		rule.addMappingRule(new SlackToRedmineMappingRule(SlackToRedmineMappingType.FIXED_TEXT, null, "fixed text3", null, RedmineTicketField.NOTES));;
		return rule;
	}
	@Test
	public void createTest() {
		TicketVo ticket = TicketCreator.create(textFile, Arrays.asList(getNegativeRule1(), getPositiveRule1(), getPositiveRule2()));
		Assert.assertEquals("running process fixed text1", ticket.getSubject());
		Assert.assertEquals("Content of file\r\nHogeFugaError occurred while running process\r\n\r\nPlease check it.\r\nBefore HogeFugaError, SPAM happened.", ticket.getDescription());
		Assert.assertEquals("Task(タスク)", ticket.getTracker());
		Assert.assertEquals("Expense Reimbursement Report Management", ticket.getProject());
		Assert.assertEquals("fixed text1", ticket.getNotes());
		Assert.assertEquals(Integer.valueOf(999999), ticket.getParentTicketNo());
	}
}
