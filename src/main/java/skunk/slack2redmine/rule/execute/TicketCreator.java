package skunk.slack2redmine.rule.execute;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.redmine.model.TicketVo;
import skunk.slack2redmine.rule.model.SlackSourceCondition;
import skunk.slack2redmine.rule.model.SlackToRedmineMappingRule;
import skunk.slack2redmine.rule.model.TicketCreationRule;
import skunk.slack2redmine.slack.model.SlackSource;

@Slf4j
public class TicketCreator {
	public static TicketVo create(SlackSource source, List<TicketCreationRule> rules) {
		Optional<TicketCreationRule> ruleOpt = rules.stream().filter(ruleCand -> test(source, ruleCand)).findFirst();
		if (!ruleOpt.isPresent()) {
			return null;
		}

		TicketCreationRule rule = ruleOpt.get();
		TicketVo ticket = new TicketVo();

		ticket.setTracker(rule.getTracker());
		ticket.setProject(rule.getProject());
		ticket.setParentTicketNo(rule.getParentTicketNo());
		setTicketParams(ticket, rule, source);

		return ticket;
	}

	private static boolean test(SlackSource source, TicketCreationRule rule) {
		Set<SlackSourceCondition> conditions = rule.getSlackSourceConditions();
		for (SlackSourceCondition condition : conditions) {
			boolean isConditionMatched = false;
			Pattern regex = Pattern.compile(condition.getRegex());

			switch (condition.getField()) {
			case CONTENT:
				isConditionMatched = regex.matcher(source.getContent()).find();
				break;
			case FILENAME:
				isConditionMatched = regex.matcher(source.getFileName()).find();
				break;
			case USER:
				isConditionMatched = regex.matcher(source.getUser()).find();
				break;
			}
			if (!isConditionMatched) {
				return false;
			}
		}
		return true;
	}

	private static void setTicketParams(TicketVo ticket, TicketCreationRule rule, SlackSource source) {
		List<SlackToRedmineMappingRule> mappingRules = rule.getMappingRules();
		StringBuilder subject = new StringBuilder();
		StringBuilder description = new StringBuilder();
		StringBuilder notes = new StringBuilder();
		for (SlackToRedmineMappingRule mappingRule : mappingRules) {
			StringBuilder fieldValueBuilder = null;
			String sourceText = null;

			switch (mappingRule.getDestination()) {
			case SUBJECT:
				fieldValueBuilder = subject;
				break;
			case DESCRIPTION:
				fieldValueBuilder = description;
				break;
			case NOTES:
				fieldValueBuilder = notes;
			}
			if (Objects.nonNull(mappingRule.getSource())) {
				switch (mappingRule.getSource()) {
				case CONTENT:
					sourceText = source.getContent();
					break;
				case FILENAME:
					sourceText = source.getFileName();
					break;
				case USER:
					sourceText = source.getUser();
					break;
				}
			}

			appendFieldValue(fieldValueBuilder, mappingRule, sourceText);
		}
		ticket.setSubject(subject.toString());
		ticket.setDescription(description.toString());
		ticket.setNotes(notes.toString());
	}

	private static void appendFieldValue(StringBuilder fieldValueBuilder, SlackToRedmineMappingRule rule,
			String sourceText) {
		switch (rule.getType()) {
		case FIXED_TEXT:
			fieldValueBuilder.append(rule.getText());
			return;

		case REGEX_MATCH:
			Pattern regex = Pattern.compile(rule.getRegex());
			Matcher matcher = regex.matcher(sourceText);
			if (matcher.find()) {
				fieldValueBuilder.append(matcher.group());
				return;
			}
			log.error("failed to create ticket param [regex:{},sourceValue:{}]", regex.pattern(), sourceText);
		}
		throw new IllegalStateException();
	}
}
