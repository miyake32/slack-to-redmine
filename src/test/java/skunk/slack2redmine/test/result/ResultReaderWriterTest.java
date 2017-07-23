package skunk.slack2redmine.test.result;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import skunk.slack2redmine.rule.result.ResultReaderWriter;
import skunk.slack2redmine.rule.result.model.TicketCreationResult;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

public class ResultReaderWriterTest {

	@Test
	public void readWriteTest() throws IOException {
		String filePath = "slack2redmine-read-write-test-result.txt";
		ResultReaderWriter r = new ResultReaderWriter(filePath);
		r.read();
		
		TicketCreationResult result1 = new TicketCreationResult();
		result1.setCreatedDateTime();
		result1.setSourceType(SlackSourceType.FILE);
		result1.setSourceId("test01");
		result1.setTicketNo(1);
		
		TicketCreationResult result2 = new TicketCreationResult();
		result2.setCreatedDateTime();
		result2.setSourceType(SlackSourceType.FILE);
		result2.setSourceId("test02");
		result2.setTicketNo(2);
		
		r.write(result1);
		r.write(Arrays.asList(result1, result2));
		System.out.println(r.read());
		Assert.assertEquals(2, r.read().size());
		Assert.assertTrue(r.isExistInResult(SlackSourceType.FILE, "test02"));
		Assert.assertFalse(r.isExistInResult(SlackSourceType.MESSAGE, "test01"));
		
		new File(filePath).delete();
	}
}
