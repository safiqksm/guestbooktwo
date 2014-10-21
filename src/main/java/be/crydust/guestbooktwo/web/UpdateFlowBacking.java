package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.ejb.PostBoundary;
import be.crydust.guestbooktwo.entities.Post;
import java.io.Serializable;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Messages;

/**
 *
 * @author kristof
 */
@Named
@FlowScoped("updateFlow")
public class UpdateFlowBacking implements Serializable {

    private static final long serialVersionUID = 42L;

    // Properties
    private Post currentPost = null;

    // Services
    @Inject
    transient PostBoundary postBoundary;

    public UpdateFlowBacking() {
        System.out.println("UpdateFlowBacking constructor");
    }

    // Init
    public void init() {
        //NOOP
    }

    // Actions
    public String update() {
        postBoundary.update(getCurrentPost());
        Messages.addFlashGlobalInfo("Post updated successfully");
        return "index?faces-redirect=true";
    }

    // Getters/setters
    public Post getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(Post currentPost) {
        this.currentPost = currentPost;
    }
}
