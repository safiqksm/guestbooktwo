package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.ejb.PostBoundary;
import be.crydust.guestbooktwo.entities.Post;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kristof
 */
@Named
@SessionScoped
public class PostBacking implements Serializable {

    private static final long serialVersionUID = 42L;

    // Properties
    private Post currentPost = new Post(
            "Anonymous",
            "Lorem ipsum expletive dolor sit amet.");
    @NotNull
    @Size(min = 1)
    private String word = "expletive";

    // Services
    @Inject
    transient PostBoundary postBoundary;

    // Init
    public void init() {
        //NOOP
    }

    // Actions
    public void create() {
        postBoundary.create(getCurrentPost());
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

    public Post getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(Post currentPost) {
        this.currentPost = currentPost;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
