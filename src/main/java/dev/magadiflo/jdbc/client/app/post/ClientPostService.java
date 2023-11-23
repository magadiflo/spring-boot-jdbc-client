package dev.magadiflo.jdbc.client.app.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service("jdbcClientService")
public class ClientPostService implements IPostService {
    private static final Logger LOG = LoggerFactory.getLogger(ClientPostService.class);
    private final JdbcClient jdbcClient;

    public ClientPostService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Post> findAll() {
        LOG.info("List using jdbcClient");
        return this.jdbcClient.sql("SELECT id, title, slug, date, time_to_read, tags FROM posts")
                .query(Post.class)
                .list();
    }

    @Override
    public Optional<Post> findById(String id) {
        return this.jdbcClient.sql("SELECT id, title, slug, date, time_to_read, tags FROM posts WHERE id = :id")
                .param("id", id)
                .query(Post.class)
                .optional();
    }

    @Override
    public void create(Post post) {
        int rowAffected = this.jdbcClient.sql("INSERT INTO posts(id, title, slug, date, time_to_read, tags) VALUES(?,?,?,?,?,?)")
                .params(post.id(), post.title(), post.slug(), post.date(), post.timeToRead(), post.tags())
                .update();
        Assert.state(rowAffected == 1, "Failed to create post " + post.title());
        LOG.info("Post was created: " + post.title());
    }

    @Override
    public void update(Post post, String id) {
        int rowAffected = this.jdbcClient.sql("UPDATE posts SET title = ?, slug = ?, date = ?, time_to_read = ?, tags = ? WHERE id = ?")
                .param(List.of(post.title(), post.slug(), post.date(), post.timeToRead(), post.tags(), id))
                .update();
        Assert.state(rowAffected == 1, "Failed to update post " + post.title());
    }

    @Override
    public void delete(String id) {
        int rowAffected = this.jdbcClient.sql("DELETE FROM posts WHERE id = :id")
                .param("id", id)
                .update();
        Assert.state(rowAffected == 1, "Failed to delete post " + id);
    }
}
