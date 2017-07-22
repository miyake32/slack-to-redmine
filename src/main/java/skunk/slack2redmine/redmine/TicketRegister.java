package skunk.slack2redmine.redmine;

import java.util.List;
import java.util.Objects;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.redmine.model.TicketVo;

@Slf4j
public class TicketRegister {
	private RedmineManager redmine;

	private TicketRegister() {
		super();
	};

	public TicketRegister(String redmineUrl, String userName, String password) {
		this();
		this.redmine = RedmineManagerFactory.createWithUserAuth(redmineUrl, userName, password);
	}

	public TicketRegister(String redmineUrl, String apiAccessKey) {
		this();
		this.redmine = RedmineManagerFactory.createWithApiKey(redmineUrl, apiAccessKey);
	}

	public Issue register(TicketVo ticket) throws RedmineException {
		return register(ticket.getSubject(), ticket.getDescription(), ticket.getTracker(), ticket.getProject(),
				ticket.getNotes(), ticket.getParentTicketNo());
	}

	public Issue register(String subject, String description, String tracker, String project, String notes,
			Integer parentTicketNo) throws RedmineException {
		Objects.requireNonNull(subject);
		Objects.requireNonNull(tracker);
		Objects.requireNonNull(project);

		IssueManager issueMan = redmine.getIssueManager();
		ProjectManager projectMan = redmine.getProjectManager();

		List<Tracker> trackers = issueMan.getTrackers();
		Tracker targetTracker = trackers.stream().filter(t -> tracker.equals(t.getName())).findFirst().get();

		List<Project> projects = projectMan.getProjects();
		Project targetProject = projects.stream().filter(p -> project.equals(p.getName())).findFirst().get();

		Issue issue = new Issue();
		issue.setTracker(targetTracker);
		issue.setProjectId(targetProject.getId());
		issue.setSubject(subject);
		issue.setDescription(description);
		issue.setNotes(notes);

		if (Objects.nonNull(parentTicketNo)) {
			Issue parent = issueMan.getIssueById(parentTicketNo);
			issue.setParentId(parent.getId());
			issue.setTargetVersion(parent.getTargetVersion());
			log.info("register ticket [tracker:{},project:{},subject:{},parent:#{} {},sprint:{}]",
					targetTracker.getName(), targetProject.getName(), subject, parent.getId(), parent.getSubject(),
					parent.getTargetVersion().getName());
		} else {
			log.info("register ticket [tracker:{},project:{},subject:{}]", targetTracker.getName(),
					targetProject.getName(), subject);
		}
		return issueMan.createIssue(issue);
	}
}
