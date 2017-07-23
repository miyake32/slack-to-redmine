package skunk.slack2redmine.rule.parse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.rule.model.SlackSourceCondition;
import skunk.slack2redmine.rule.model.SlackToRedmineMappingRule;
import skunk.slack2redmine.rule.model.TicketCreationRule;
import skunk.slack2redmine.rule.model.type.RedmineTicketField;
import skunk.slack2redmine.rule.model.type.SlackSourceField;
import skunk.slack2redmine.rule.model.type.SlackToRedmineMappingType;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@Slf4j
public class RuleYamlParser {
	@SuppressWarnings("rawtypes")
	public static List<TicketCreationRule> parse(String filePath) throws FileNotFoundException, YamlException {
		log.info("read {}", filePath);
		YamlReader reader = new YamlReader(new FileReader(filePath));
		Object obj1stLayer = reader.read();
		List list = (List) obj1stLayer;
		List<TicketCreationRule> rules = new ArrayList<>();
		for (Object obj2ndLayer : list) {
			Map map = (Map) obj2ndLayer;
			rules.add(createRuleFrom(map));
		}
		return rules;
	}

	@SuppressWarnings("rawtypes")
	private static TicketCreationRule createRuleFrom(Map map) {
		// type cannot be null
		String typeStr = (String) map.get("type");
		SlackSourceType type = null;
		if (typeStr.equalsIgnoreCase("file")) {
			type = SlackSourceType.FILE;
		} else if (typeStr.equalsIgnoreCase("message")) {
			type = SlackSourceType.MESSAGE;
		}
		Objects.requireNonNull(type, "type cannot be null");

		// parentTicketNo can be null
		String parentTicketNoStr = (String) map.get("parentTicketNo");
		Integer parentTicketNo = null;
		if (Objects.nonNull(parentTicketNoStr)) {
			parentTicketNo = Integer.parseInt(parentTicketNoStr);
		}

		// tracker cannot be null
		String tracker = (String) map.get("tracker");
		Objects.requireNonNull(tracker, "tracker cannot be null");

		// project cannot be null
		String project = (String) map.get("project");
		Objects.requireNonNull(project, "project cannot be null");

		TicketCreationRule rule = new TicketCreationRule(type, parentTicketNo, tracker, project);

		List slackSourceConditions = (List) map.get("conditions");
		for (Object obj : slackSourceConditions) {
			Map slackSourceConditionMap = (Map) obj;
			rule.addSlackSourceCondition(createConditionFrom(slackSourceConditionMap));
		}

		List mappingRules = (List) map.get("mapping");
		for (Object obj : mappingRules) {
			Map mappingRuleMap = (Map) obj;
			rule.addMappingRule(createMappingRuleFrom(mappingRuleMap));
		}
		return rule;
	}

	@SuppressWarnings("rawtypes")
	private static SlackSourceCondition createConditionFrom(Map map) {
		// field cannot be null
		String fieldStr = (String) map.get("field");
		SlackSourceField field = SlackSourceField.of(fieldStr);
		Objects.requireNonNull(field, "field in condition cannot be null");

		// tracker cannot be null
		String regex = (String) map.get("regex");
		Objects.requireNonNull(regex, "regex in condition cannot be null");

		return new SlackSourceCondition(field, regex);
	}

	@SuppressWarnings("rawtypes")
	private static SlackToRedmineMappingRule createMappingRuleFrom(Map map) {
		// type cannot be null
		String typeStr = (String) map.get("type");
		SlackToRedmineMappingType type = SlackToRedmineMappingType.of(typeStr);
		Objects.requireNonNull(type, "type of mapping cannot be null");

		// regex can be null
		String regex = (String) map.get("regex");

		// text can be null
		String text = (String) map.get("text");

		// source can be null
		String sourceStr = (String) map.get("srcField");
		SlackSourceField source = SlackSourceField.of(sourceStr);
		
		// destination cannot be null
		String destinationStr = (String) map.get("destField");
		RedmineTicketField destination = RedmineTicketField.of(destinationStr);
		Objects.requireNonNull(destination, "destField in mapping cannot be null");

		return new SlackToRedmineMappingRule(type, regex, text, source, destination);
	}
}
