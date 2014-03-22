package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.ejb.PostBoundary;
import be.crydust.guestbooktwo.entities.Post;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author kristof
 */
@Named
@ViewScoped
public class PostUpdateBacking implements Serializable {

    private static final long serialVersionUID = 42L;

    // Properties
    private Post currentPost = null;

    // Services
    @Inject
    transient PostBoundary postBoundary;

    // Init
    public void init() {
        //NOOP
    }

    // Actions
    public String update() {
        postBoundary.update(getCurrentPost());
        Messages.addFlashGlobalInfo("Post updated successfully");
        return "/index";
    }

    // Getters/setters
    public Post getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(Post currentPost) {
        this.currentPost = currentPost;
    }

}
