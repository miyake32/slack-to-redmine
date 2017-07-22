package skunk.slack2redmine.rule.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@Getter
@Slf4j
@EqualsAndHashCode
@ToString
public class TicketCreationRule {
	private SlackSourceType type;
	// このルールによるチケット作成の対象となるSlackのソースを絞り込む条件
	// 複数指定した場合はAND条件
	private Set<SlackSourceCondition> slackSourceConditions = new HashSet<>();
	// SlackソースからRedmineチケットを作成する際のマッピングルール
	// 同じフィールドに対して複数のテキストをマッピングした場合は結合される
	private List<SlackToRedmineMappingRule> mappingRules = new ArrayList<>();
	private Integer parentTicketNo;
	private String tracker;
	private String project;

	private TicketCreationRule() {
		super();
	}
	public TicketCreationRule(SlackSourceType type, Integer parentTicketNo, String tracker, String project) {
		this();
		this.type = type;
		this.parentTicketNo = parentTicketNo;
		this.tracker = tracker;
		this.project = project;
	}
	
	public void addSlackSourceCondition(SlackSourceCondition cond) {
		if (!cond.isValid(type)) {
			log.error("SlackSourceCondition is not valid. {}", cond);
			throw new IllegalArgumentException();
		}
		this.slackSourceConditions.add(cond);
	}
	
	public void addMappingRule(SlackToRedmineMappingRule rule) {
		if (!rule.isValid(type)) {
			log.error("SlackToRedmineMappingRule is not valid. {}", rule);
			throw new IllegalArgumentException();
		}
		this.mappingRules.add(rule);
	}
}
