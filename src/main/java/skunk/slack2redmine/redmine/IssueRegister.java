package skunk.slack2redmine.redmine;

import java.util.List;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Tracker;

public class IssueRegister {
	private RedmineManager redmine;
	
	private IssueRegister(){
		super();
	};
	public IssueRegister(String redmineUrl, String userName, String password) {
		this();
		this.redmine = RedmineManagerFactory.createWithUserAuth(redmineUrl, userName, password);
	}
	public IssueRegister(String redmineUrl, String apiAccessKey) {
		this();
		this.redmine = RedmineManagerFactory.createWithApiKey(redmineUrl, apiAccessKey);
	}


	public Issue register(String subject, String description, String tracker, String project, Integer parentTicketNo) throws RedmineException {
		IssueManager issueMan = redmine.getIssueManager();
		ProjectManager projectMan = redmine.getProjectManager();
		
		List<Tracker> trackers = issueMan.getTrackers();
		Tracker targetTracker = trackers.stream().filter(t -> tracker.equals(t.getName())).findFirst().get();
		
		List<Project> projects = projectMan.getProjects();
		Project targetProject = projects.stream().filter(p -> project.equals(p.getName())).findFirst().get();
		
		Issue parent = issueMan.getIssueById(parentTicketNo);
		
		Issue issue = new Issue();
		issue.setTracker(targetTracker);
		issue.setProjectId(targetProject.getId());
		issue.setSubject(subject);
		issue.setDescription(description);
		issue.setParentId(parent.getId());
		issue.setTargetVersion(parent.getTargetVersion());
		
		return issueMan.createIssue(issue);
	}
}
