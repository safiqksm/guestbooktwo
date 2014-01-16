package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.ejb.PostBoundary;
import be.crydust.guestbooktwo.entities.Post;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author kristof
 */
@Named
@RequestScoped
public class PostBacking implements Serializable {

    // Properties
    private String author = "Anonymous";
    private String message = "Lorem ipsum expletive dolor sit amet.";
    private String word = "expletive";

    // Services
    @Inject
    PostBoundary postBoundary;

    // Init
    public void init() {
        //NOOP
    }

    // Actions
    public void create() {
        Post post = new Post();
        post.setAuthor(getAuthor());
        post.setMessage(getMessage());
        postBoundary.create(post);
    }

    public void delete(Long id) {
        postBoundary.deleteById(id);
    }

    public void deleteByWord() {
        postBoundary.deleteByWord(getWord());
    }

    // Getters/setters
    public List<Post> getPosts() {
        return postBoundary.readAll();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
