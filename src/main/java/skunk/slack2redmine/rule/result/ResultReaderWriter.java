package skunk.slack2redmine.rule.result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import skunk.slack2redmine.rule.result.model.TicketCreationResult;
import skunk.slack2redmine.slack.model.type.SlackSourceType;

@Slf4j
public class ResultReaderWriter {
	private Set<TicketCreationResult> resultSet = null;
	private String filePath;

	private ResultReaderWriter() {
		super();
	}

	public ResultReaderWriter(String filePath) {
		this();
		this.filePath = filePath;
	}

	public Set<TicketCreationResult> read() throws IOException {
		log.info("start to read result file {}", filePath);
		resultSet = new HashSet<>();
		try (FileReader fr = new FileReader(filePath)) {
			BufferedReader br = new BufferedReader(fr);
			String line;
			while (Objects.nonNull(line = br.readLine())) {
				resultSet.add(TicketCreationResult.deserialize(line));
			}
			return resultSet;
		} catch (FileNotFoundException e) {
			log.warn("file {} is not found. Please confirm that this is first execution.", filePath);
			return resultSet;
		}
	}

	public void write(TicketCreationResult result) throws IOException {
		write(Arrays.asList(result));
	}

	public void write(Collection<TicketCreationResult> results) throws IOException {
		if (Objects.isNull(resultSet)) {
			read();
		}
		File file = new File(filePath);
		if (!file.exists()) {
			log.info("created file {}", filePath);
			file.createNewFile();
		}
		try (FileWriter fw = new FileWriter(file, true)) {
			PrintWriter pw = new PrintWriter(fw);
			for (TicketCreationResult result : results) {
				if (resultSet.contains(result)) {
					log.warn("duplicated result {}", result);
					continue;
				}
				pw.println(result.serialize());
				resultSet.add(result);
			}
		} catch (IOException e) {
			log.error("error occurred while writing result");
			throw e;
		}
	}

	public boolean isExistInResult(SlackSourceType type, String id) throws IOException {
		if (Objects.isNull(resultSet)) {
			read();
		}
		TicketCreationResult test = new TicketCreationResult();
		test.setSourceType(type);
		test.setSourceId(id);
		return resultSet.contains(test);
	}
}
