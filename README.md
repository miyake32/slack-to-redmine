# slack-to-redmine

## How to use
1. execute `mvn package` to create jar.
2. run slack-to-redmine with following command.

java -jar slack-to-redmine-1.0-SNAPSHOT-jar-with-dependencies.jar  --slack-token TOKEN --redmine-url REDMINE_URL --redmine-user REDMINE_USER --redmine-password REDMINE_PASSWORD --rule-file PATH_TO_RULE_FILE --result-file PATH_TO_RESULT_FILE CHANNEL1 [CHANNEL2 CHANNEL3 ...]
- `--slack-token` : token to access slack. You can get token in [Legacy tokens](https://api.slack.com/custom-integrations/legacy-tokens)
- `--redmine-url` : url of redmine in which you want to create tickets
- `--redmine-user` `--redmine-password` : redmine auth information. you can use `--redmine-token` instead.
- `--redmine-token` : you can use `--redmine-user` and `--redmine-password` instead.
- `--rule-file` : path to rule file, which defines slack-to-redmine mapping rules. Please refer to sample.yaml to create rule file.
- `--result-file` : path to result file, which is used to manage processed slack messages and created ticket. always specify same result file to avoid ticket duplication.
- `--dry-run` : *optional* use dry run mode. ticket won't be created and ticket information will be printed in log.
- `--baseline` : *optional* only write result file and ticket won't be created. In future executions, ticket won't be created for slack messages written in result file.