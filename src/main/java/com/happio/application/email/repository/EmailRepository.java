package com.happio.application.email.repository;

import com.happio.application.email.model.SentEmail;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class EmailRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public EmailRepository(NamedParameterJdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public Optional<SentEmail> getSentEmail(int emailId){
        String query = "SELECT id, sent_to, sent_from, email_type FROM SentEmails WHERE id = (:emailId);";
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("emailId", emailId);
        Stream<SentEmail> databaseResponse = jdbcTemplate.queryForStream(
                query,
                parameters,
                (ResultSet rs, int rowNum) -> mapObject(rs)
        );
        Optional<SentEmail> email = databaseResponse.findFirst();
        databaseResponse.close();
        return email;
    }

    public SentEmail mapObject(ResultSet resultSet) throws SQLException {
        return new SentEmail(
                resultSet.getInt("id"),
                resultSet.getString("sent_to"),
                resultSet.getString("sent_from"),
                resultSet.getString("email_type")
        );
    }

    public void saveEmail(String sentTo, String sentFrom, String emailType) {
        String query = """
                        INSERT INTO SentEmails 
                        (sent_to, sent_from, email_type) 
                        VALUES 
                        (:sent_to, :sent_from, :email_type)
                        """;
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("sent_to", sentTo)
                .addValue("sent_from", sentFrom)
                .addValue("email_type", emailType);
        jdbcTemplate.update(query, parameters);
    }
}
