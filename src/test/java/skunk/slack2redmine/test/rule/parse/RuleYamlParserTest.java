package skunk.slack2redmine.test.rule.parse;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import skunk.slack2redmine.rule.model.TicketCreationRule;
import skunk.slack2redmine.rule.parse.RuleYamlParser;

public class RuleYamlParserTest {
	@Test
	public void parseTest() throws FileNotFoundException, YamlException {
		List<TicketCreationRule> rules = RuleYamlParser.parse("sample.yaml");
		Assert.assertEquals(rules.get(0).getTracker(), "Name of Tracker of Redmine Ticket");
		Assert.assertEquals(rules.get(1).getMappingRules().get(0).getText(), "Name of Ticket");
	}
}
