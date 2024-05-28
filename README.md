## Sprintboot Application

Source code found in `application/src`. Instructions to run

#### Navigate to directory
`cd ./application`

#### Runs the app
`./gradlew bootRun`

Open [http://localhost:8081/](http://localhost:8081/) to ensure application is running.

#### Runs the tests
`./gradlew test`

#### Directory structure
- `CreatePost` REST service is contained in the `post` package
- Background `SendEmail` service tasks ran in the `scheduling` package
- Conceptual high level designs found in the diagram folder in the top level directory

## To create a POST via API 
Once the service is running, you should be able to send a POST request to the service via curl command.

`curl -XPOST -H "Content-type: application/json" -d '{"id": "4", "channelId": "10", "creatorId": "10", "content": "heey!"}' 'http://localhost:8081/post'`

The data is stored in the Post table which can be queried by the [h2-console](http://localhost:8081/h2-console/login.jsp). 

A background task runs in to query for new posts created and sends out the email to subscribers if new post is found.
To change the frequency at which the background task runs, navigate to `./src/main/resources/application.properties` and set the time required in the `scheduler.interval.rate` option.

## What I would do to make production ready
#### Send events to client instead of polling
- Use of server sent events to client instead of polling the queue to eliminate the need to make calls
- Currently, both services live in the same application for simplicity but ideally, they would be separated into two self-contained microservices
#### Use an event driven ledger instead of a database to store events
- H2 database is used for stored events but it would be better to use a tool that is designed to handle large quantities of data streaming (google pub/sub or kafka etc)
#### Make use of concurrency to spread up processing
- The sending of emails runs one after another. In scenarios when a creator has millions of subscribers, this would lean to very slow processing of the emails. To improve this, emails could be sent concurrently
#### Implement retry mechanism for task failures
- When tasks are in the error state, they need to be retried again at a later point
- When a task hits the max number of failures, it should be put in the dead letter queue and manually investigated
#### Additional forms of testing
- More unit tests on the components to validate their behaviour
- Integration tests on pages and large components to ensure the application works as expected
- Contract testing between services to ensure consistent communication between APIs
#### Lack of lint rules
- Enforce strict coding rules to improve code quality
#### Better use of logging
- Use logs to give an understanding of the application behaviour, particularly on network calls
- Store events and logs to a centralised (Logstash, Cloudwatch etc)
#### Centralised error handling
- When there are errors in the application, it would be good also to collect the errors and have alerting when they occurs (incorporate Sentry, Cloudwatch etc)

