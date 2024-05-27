package com.happio.application.post.repository;

import com.happio.application.post.model.Post;
import com.happio.application.task.model.EmailTask;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PostRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public PostRepository (NamedParameterJdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Transactional
    public int create(Post post) {
        String query = """
              INSERT INTO Posts
              (channel_id, content, creator_id)
              VALUES
              (:channel_id, :content, :creator_id)
        """;
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", post.getId())
                .addValue("channel_id", post.getChannelId())
                .addValue("content", post.getContent())
                .addValue("creator_id", post.getCreatorId());

        return jdbcTemplate.update(query, parameters);
    }

    public Optional<Post> getPostById(String postId) {
        String query = "SELECT id, channel_id, creator_id, content FROM Posts WHERE id = (:postId);";
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("postId", postId);
        Stream<Post> databaseResponse = jdbcTemplate.queryForStream(
                query,
                parameters,
                (ResultSet rs, int rowNum) -> mapObject(rs)
        );
        Optional<Post> post = databaseResponse.findFirst();
        databaseResponse.close();
        return post;
    }

    private Post mapObject(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getInt("channel_id"),
                resultSet.getInt("creator_id"),
                resultSet.getString("content")
        );
    }


}
