package skunk.slack2redmine.slack;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.users.UsersListRequest;
import com.github.seratch.jslack.api.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernameResolver {
	private String token;
	private Map<String, String> userIdUsernameMap = null;

	private UsernameResolver() {
		super();
	}

	public UsernameResolver(String token) {
		this();
		this.token = token;
	}

	public void retrieveUserInfo() throws IOException, SlackApiException {
		log.info("retrieve user information");
		Slack slack = Slack.getInstance();

		userIdUsernameMap = slack.methods()
				.usersList(UsersListRequest.builder().token(token).build()).getMembers()
				.stream().collect(Collectors.toMap(User::getId, User::getName));
	}
	
	public String resolve(String userId) {
		if (Objects.isNull(userIdUsernameMap)) {
			throw new IllegalStateException("UsernameResolver#retrieveUserInfo should be called before resolve");
		}
		return userIdUsernameMap.get(userId);
	}
	
	public boolean isReady() {
		return Objects.nonNull(userIdUsernameMap);
	}
}
