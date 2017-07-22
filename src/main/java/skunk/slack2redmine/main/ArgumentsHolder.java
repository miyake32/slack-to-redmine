package skunk.slack2redmine.main;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArgumentsHolder {
	private static String slackApiToken;
	private static String redmineUrl;
	private static String redmineApiToken;
	private static String redmineUserName;
	private static String redminePassword;
	private static Set<String> slackChannels = new HashSet<>();
	private static String ruleFile;
	private static String resultFile;

	public enum RedmineAuthMethod {
		NONE, USER_PASSWORD, API_TOKEN;
	}

	public static void readArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-s":
			case "--slack-token":
				log.info("read slack-token");
				i++;
				if (i >= args.length) {
					log.error("invalid argument");
					System.exit(1);
				}
				ArgumentsHolder.setSlackApiToken(args[i]);
				break;

			case "-r":
			case "--redmine-url":
				log.info("read redmine-url");
				i++;
				if (i >= args.length) {
					log.error("invalid argument");
					System.exit(1);
				}
				ArgumentsHolder.setRedmineUrl(args[i]);
				break;
			case "--redmine-user":
				log.info("read redmine-user");
				i++;
				if (i >= args.length) {
					log.error("invalid argument");
					System.exit(1);
				}
				ArgumentsHolder.setRedmineUserName(args[i]);
				break;
			case "--redmine-password":
				log.info("read redmine-password");
				i++;
				if (i >= args.length) {
					log.error("invalid argument");
					System.exit(1);
				}
				ArgumentsHolder.setRedminePassword(args[i]);
				break;
			case "--redmine-token":
				log.info("read redmine-token");
				i++;
				if (i >= args.length) {
					log.error("invalid argument");
					System.exit(1);
				}
				ArgumentsHolder.setRedmineApiToken(args[i]);
				break;
			case "--rule-file":
				log.info("read rule-file");
				i++;
				if (i >= args.length) {
					log.error("invalid argument");
					System.exit(1);
				}
				ArgumentsHolder.setRuleFile(args[i]);
				break;
			case "--result-file":
				log.info("read result-file");
				i++;
				if (i >= args.length) {
					log.error("invalid argument");
					System.exit(1);
				}
				ArgumentsHolder.setResultFile(args[i]);
				break;
			default:
				slackChannels.add(args[i]);
				i++;
				break;
			}
		}
	}

	public static RedmineAuthMethod getRedmineAuthMethod() {
		if (Objects.nonNull(redmineApiToken)) {
			return RedmineAuthMethod.API_TOKEN;
		}
		if (Objects.nonNull(redmineUserName) && Objects.nonNull(redminePassword)) {
			return RedmineAuthMethod.USER_PASSWORD;
		}
		return RedmineAuthMethod.NONE;
	}

	public static String getSlackApiToken() {
		return slackApiToken;
	}

	private static void setSlackApiToken(String slackApiToken) {
		ArgumentsHolder.slackApiToken = slackApiToken;
	}

	public static String getRedmineUrl() {
		return redmineUrl;
	}

	private static void setRedmineUrl(String redmineUrl) {
		ArgumentsHolder.redmineUrl = redmineUrl;
	}

	public static String getRedmineApiToken() {
		return redmineApiToken;
	}

	private static void setRedmineApiToken(String redmineApiToken) {
		ArgumentsHolder.redmineApiToken = redmineApiToken;
	}

	public static String getRedmineUserName() {
		return redmineUserName;
	}

	private static void setRedmineUserName(String redmineUserName) {
		ArgumentsHolder.redmineUserName = redmineUserName;
	}

	public static String getRedminePassword() {
		return redminePassword;
	}

	private static void setRedminePassword(String redminePassword) {
		ArgumentsHolder.redminePassword = redminePassword;
	}

	public static Set<String> getSlackChannels() {
		return slackChannels;
	}

	private static void setRuleFile(String ruleFile) {
		ArgumentsHolder.ruleFile = ruleFile;
	}

	public static String getRuleFile() {
		return ruleFile;
	}

	private static void setResultFile(String resultFile) {
		ArgumentsHolder.resultFile = resultFile;
	}

	public static String getResultFile() {
		return resultFile;
	}
}
