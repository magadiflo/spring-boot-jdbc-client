package dev.magadiflo.jdbc.client.app.post;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    List<Post> findAll();

    Optional<Post> findById(String id);

    void create(Post post);

    void update(Post post, String id);

    void delete(String id);
}
