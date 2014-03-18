package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.ejb.FamilyBoundary;
import be.crydust.guestbooktwo.entities.Child;
import be.crydust.guestbooktwo.entities.Parent;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kristof
 */
@Named
@SessionScoped
public class FamilyBacking implements Serializable {

    private static final long serialVersionUID = 42L;

    private static final Logger log = LoggerFactory.getLogger(FamilyBacking.class);

    private List<Parent> parents = null;
    private TreeNode root = null;
    private TreeNode selectedNode = null;

    @Inject
    transient FamilyBoundary familyBoundary;

    public List<Parent> getParents() {
        log.trace("getParents");
        if (parents == null) {
            parents = familyBoundary.findAll();
        }
        return parents;
    }

    public TreeNode getTreeRoot() {
        log.trace("getTreeRoot");
        if (root == null) {
            log.trace("root == null");
            root = new DefaultTreeNode("Root", null);
            List<Parent> parents = getParents();
            for (Parent parent : parents) {
                TreeNode parentNode = new DefaultTreeNode("parent", parent, root);
                parentNode.setExpanded(true);
                for (Child child : parent.getChildren()) {
                    TreeNode childNode = new DefaultTreeNode("child", child, parentNode);
                }
            }
        }
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onDragDrop(TreeDragDropEvent event) {
        log.trace("onDragDrop");
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Dragged " + dragNode.getData(), "\nDropped on " + dropNode.getData() + "\nat " + dropIndex);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
