package dev.magadiflo.jdbc.client.app.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplatePostService implements IPostService {
    private static final Logger LOG = LoggerFactory.getLogger(TemplatePostService.class);
    private final JdbcTemplate jdbcTemplate;

    public TemplatePostService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Post> rowMapper = (rs, rowNum) -> new Post(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("slug"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("time_to_read"),
            rs.getString("tags")
    );

    @Override
    public List<Post> findAll() {
        String sql = """
                SELECT id, title, slug, date, time_to_read, tags
                FROM posts
                """;
        return this.jdbcTemplate.query(sql, this.rowMapper);
    }

    @Override
    public Optional<Post> findById(String id) {
        String sql = """
                SELECT id, title, slug, date, time_to_read, tags
                FROM posts
                WHERE id = ?
                """;
        Post post = null;
        try {
            post = this.jdbcTemplate.queryForObject(sql, this.rowMapper, id);
        } catch (DataAccessException ex) {
            LOG.info("Post not found: " + id);
            return Optional.empty();
        }
        return Optional.ofNullable(post);
    }

    @Override
    public void create(Post post) {
        String sql = """
                INSERT INTO posts(id, title, slug, date, time_to_read, tags)
                VALUES(?,?,?,?,?,?)
                """;
        int rowsAffected = this.jdbcTemplate.update(sql, post.id(), post.title(), post.slug(), post.date(), post.timeToRead(), post.tags());
        if (rowsAffected == 1) {
            LOG.info("New Post Created: " + post.title());
        }
    }

    @Override
    public void update(Post post, String id) {
        String sql = """
                UPDATE posts SET title = ?, slug = ?, date = ?, time_to_read = ?, tags = ?
                WHERE id = ?
                """;
        int rowsAffected = this.jdbcTemplate.update(sql, post.title(), post.slug(), post.date(), post.timeToRead(), post.tags(), id);
        if (rowsAffected == 1) {
            LOG.info("Post Updated: " + post.title());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        int rowsAffected = this.jdbcTemplate.update(sql, id);
        if (rowsAffected == 1) {
            LOG.info("Post Deleted: " + id);
        }
    }
}
