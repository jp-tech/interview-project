package com.happio.application.task.repository;

import com.happio.application.task.model.EmailTask;
import com.happio.application.task.model.EmailTaskStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;
import java.util.Date;

@Repository
public class TaskRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public TaskRepository (NamedParameterJdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Transactional
    public void create(int postId) {
        String query = """
                        INSERT INTO EmailTasks
                        (post_id)
                        VALUES
                        (:post_id)
                        """;
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("post_id", postId);
        jdbcTemplate.update(query, parameters);
    }

    public List<EmailTask> getAndUpdateAwaitingTasks(){
        String query = """
                        SELECT id, post_id, attempts, status, updated_on
                        FROM EmailTasks
                        WHERE status = (:status)
                        """;
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("status", "AWAITING");
        Stream<EmailTask> databaseResponse = jdbcTemplate.queryForStream(
                query,
                parameters,
                (ResultSet rs, int rowNum) -> mapObject(rs)
        );
        List<EmailTask> tasks = databaseResponse.toList();
        if (tasks.size() > 0){
            jdbcTemplate.update(
                    "UPDATE EmailTasks SET status='"+ EmailTaskStatus.IN_PROGRESS.toString()+"' WHERE status=(:status)",
                    new MapSqlParameterSource().addValue("status", EmailTaskStatus.AWAITING.toString())
            );
        }
        databaseResponse.close();
        return tasks;
    }

    public void setErrorStatus(int taskId) {
        jdbcTemplate.update(getUpdateQuery(EmailTaskStatus.ERROR), getSqlParameters(taskId));
    }

    public void setSuccessStatus(int taskId) {
        jdbcTemplate.update(getUpdateQuery(EmailTaskStatus.COMPLETED), getSqlParameters(taskId));
    }

    private MapSqlParameterSource getSqlParameters(int taskId){
        MapSqlParameterSource sqlParameters = new MapSqlParameterSource();
        sqlParameters.addValue("id", taskId);
        sqlParameters.addValue("updated_on", new Date());
        return sqlParameters;
    }

    private String getUpdateQuery(EmailTaskStatus newStatus){
        String query = "UPDATE EmailTasks SET updated_on=(:updated_on), status='"+newStatus;
        query += "' WHERE id=(:id) AND status='"+EmailTaskStatus.IN_PROGRESS+"'";
        return query;
    }

    private EmailTask mapObject(ResultSet resultSet) throws SQLException {
        return new EmailTask(
                resultSet.getInt("id"),
                resultSet.getInt("post_id"),
                resultSet.getString("status")
        );
    }
}
