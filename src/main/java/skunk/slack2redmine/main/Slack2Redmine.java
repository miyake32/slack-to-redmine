package skunk.slack2redmine.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.redmine.TicketRegister;
import skunk.slack2redmine.redmine.model.TicketVo;
import skunk.slack2redmine.rule.execute.TicketCreator;
import skunk.slack2redmine.rule.model.TicketCreationRule;
import skunk.slack2redmine.rule.parse.RuleYamlParser;
import skunk.slack2redmine.rule.result.ResultReaderWriter;
import skunk.slack2redmine.rule.result.model.TicketCreationResult;
import skunk.slack2redmine.slack.MessageAppender;
import skunk.slack2redmine.slack.MessageRetriever;
import skunk.slack2redmine.slack.TextFileRetriever;
import skunk.slack2redmine.slack.UsernameResolver;
import skunk.slack2redmine.slack.model.SlackSource;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@Slf4j
public class Slack2Redmine {
	public static void main(String[] args) throws IOException {
		ArgumentsHolder.readArgs(args);

		ResultReaderWriter result = new ResultReaderWriter(ArgumentsHolder.getResultFile());
		List<TicketCreationRule> rules = null;
		try {
			rules = RuleYamlParser.parse(ArgumentsHolder.getRuleFile());
		} catch (FileNotFoundException | YamlException e) {
			log.error("failed to parse rule-file:{}", ArgumentsHolder.getRuleFile(), e);
			System.exit(1);
		}
		TicketRegister ticketRegister = new TicketRegister(ArgumentsHolder.getRedmineUrl(),
				ArgumentsHolder.getRedmineUserName(), ArgumentsHolder.getRedminePassword());
		MessageAppender messageAppender = new MessageAppender(ArgumentsHolder.getSlackApiToken());

		Set<SlackSource> sources = new HashSet<>();
		UsernameResolver usernameResolver = new UsernameResolver(ArgumentsHolder.getSlackApiToken());
		if (rules.stream().filter(r -> r.getType() == SlackSourceType.FILE).findFirst().isPresent()) {
			try {
				sources.addAll(new TextFileRetriever(ArgumentsHolder.getSlackApiToken(), usernameResolver)
						.getSlackSources(ArgumentsHolder.getSlackChannels()));
			} catch (IOException | SlackApiException e) {
				log.error("failed to retrieve slack files");
				System.exit(2);
			}
		}
		if (rules.stream().filter(r -> r.getType() == SlackSourceType.MESSAGE).findFirst().isPresent()) {
			try {
				sources.addAll(new MessageRetriever(ArgumentsHolder.getSlackApiToken(), usernameResolver)
						.getSlackSources(ArgumentsHolder.getSlackChannels()));
			} catch (IOException | SlackApiException e) {
				log.error("failed to retrieve slack messages");
				System.exit(2);
			}
		}

		for (SlackSource source : sources) {
			if (result.isExistInResult(source.getType(), source.getId())) {
				continue;
			}
			TicketVo ticket = TicketCreator.create(source, rules);
			if (Objects.isNull(ticket)) {
				continue;
			}

			TicketCreationResult creationResult = new TicketCreationResult();
			creationResult.setCreatedDateTime();
			creationResult.setSourceType(source.getType());
			creationResult.setSourceId(source.getId());

			if (ArgumentsHolder.isDryRun() || ArgumentsHolder.creatingBaseline()) {
				log.info(ticket.toString());
				creationResult.setTicketNo(-1);
			} else {
				Issue registeredTicket = null;
				try {
					registeredTicket = ticketRegister.register(ticket);
				} catch (RedmineException e) {
					log.error("exception occurred while registering ticket {}", ticket, e);
					System.exit(3);
				}
				creationResult.setTicketNo(registeredTicket.getId());
			}

			if (ArgumentsHolder.isDryRun()) {
				log.info(creationResult.toString());
			} else {
				try {
					result.write(creationResult);
				} catch (IOException e) {
					log.error("exception occurred while writing results {}", creationResult, e);
					System.exit(4);
				}
			}
			StringBuilder message = new StringBuilder();
			message.append("slack2redmine created ticket ");
			message.append(ArgumentsHolder.getRedmineUrl());
			if (!ArgumentsHolder.getRedmineUrl().endsWith("/")) {
				message.append("/");
			}
			message.append("issues/");
			message.append(creationResult.getTicketNo());

			if (ArgumentsHolder.isDryRun() || ArgumentsHolder.creatingBaseline()) {
				log.info("Message: {}", message.toString());
			} else {
				try {
					messageAppender.appendMessage(source, message.toString());
				} catch (IOException | SlackApiException e) {
					log.error("exception occurred while sending message [sourceType:{},sourceId:{},message:{}]",
							source.getType().toString(), source.getId(), message.toString());
					System.exit(5);
				}
			}
		}
	}
}
