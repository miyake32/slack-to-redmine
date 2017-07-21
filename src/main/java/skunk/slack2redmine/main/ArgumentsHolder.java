package skunk.slack2redmine.main;

import java.util.Objects;

public class ArgumentsHolder {
	private static String slackApiToken;
	private static String redmineUrl;
	private static String redmineApiToken;
	private static String redmineUserName;
	private static String redminePassword;
	
	public enum RedmineAuthMethod {
		NONE, USER_PASSWORD, API_TOKEN;
	}
	
    public static void readArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
            case "-s":
            case "--slack-token":
                i++;
                if (i >= args.length) {
                    System.out.println("invalid argument");
                    System.exit(1);
                }
                ArgumentsHolder.setSlackApiToken(args[i]);
                break;

            case "-r":
            case "--redmine-url":
                i++;
                if (i >= args.length) {
                    System.out.println("invalid argument");
                    System.exit(1);
                }
                ArgumentsHolder.setRedmineUrl(args[i]);
                break;
            case "--redmine-user":
                i++;
                if (i >= args.length) {
                    System.out.println("invalid argument");
                    System.exit(1);
                }
                ArgumentsHolder.setRedmineUserName(args[i]);
                break;
            case "--redmine-password":
                i++;
                if (i >= args.length) {
                    System.out.println("invalid argument");
                    System.exit(1);
                }
                ArgumentsHolder.setRedminePassword(args[i]);
                break;
            case "--redmine-token":
                i++;
                if (i >= args.length) {
                    System.out.println("invalid argument");
                    System.exit(1);
                }
                ArgumentsHolder.setRedmineApiToken(args[i]);
                break;
            default:
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

	public static void setSlackApiToken(String slackApiToken) {
		ArgumentsHolder.slackApiToken = slackApiToken;
	}

	public static String getRedmineUrl() {
		return redmineUrl;
	}

	public static void setRedmineUrl(String redmineUrl) {
		ArgumentsHolder.redmineUrl = redmineUrl;
	}

	public static String getRedmineApiToken() {
		return redmineApiToken;
	}

	public static void setRedmineApiToken(String redmineApiToken) {
		ArgumentsHolder.redmineApiToken = redmineApiToken;
	}

	public static String getRedmineUserName() {
		return redmineUserName;
	}

	public static void setRedmineUserName(String redmineUserName) {
		ArgumentsHolder.redmineUserName = redmineUserName;
	}

	public static String getRedminePassword() {
		return redminePassword;
	}

	public static void setRedminePassword(String redminePassword) {
		ArgumentsHolder.redminePassword = redminePassword;
	}
}
