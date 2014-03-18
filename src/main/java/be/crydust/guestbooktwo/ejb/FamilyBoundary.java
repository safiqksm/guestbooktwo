package be.crydust.guestbooktwo.ejb;

import be.crydust.guestbooktwo.entities.Child;
import be.crydust.guestbooktwo.entities.Child_;
import be.crydust.guestbooktwo.entities.Parent;
import be.crydust.guestbooktwo.entities.Parent_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kristof
 */
@Stateless
public class FamilyBoundary {

    private static final Logger log = LoggerFactory.getLogger(FamilyBoundary.class);
    
    @PersistenceContext
    EntityManager em;

    public Parent createParent(Parent parent) {
        log.trace("createParent");
        em.persist(parent);
        return parent;
    }

    public Parent updateParent(Parent parent) {
        log.trace("updateParent");
        return em.merge(parent);
    }

    public Parent findParentById(Long id) {
        log.trace("findParentById");
        return em.find(Parent.class, id);
    }

    public Child createChild(Child child) {
        log.trace("createChild");
        Parent parent = child.getParent();
        if (parent != null) {
            parent = em.merge(parent);
            parent.addChild(child);
        } else {
            em.persist(child);
        }
        return child;
    }

    public Child findChildById(Long id) {
        log.trace("findChildById");
        return em.find(Child.class, id);
    }

    public Child addChildToParent(Child child, Parent parent) {
        log.trace("addChildToParent");
        parent = em.merge(parent);
        parent.addChild(child);
        return child;
    }

    public Parent findParentByName(String name) {
        log.trace("findParentByName");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Parent> cq = cb.createQuery(Parent.class);
        Root<Parent> root = cq.from(Parent.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Parent_.name), name));
        TypedQuery<Parent> q = em.createQuery(cq);
        q.setMaxResults(1);
        List<Parent> parents = q.getResultList();
        if (parents.isEmpty()) {
            return null;
        }
        return parents.get(0);
    }

    public Child findChildByName(String name) {
        log.trace("findChildByName");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Child> cq = cb.createQuery(Child.class);
        Root<Child> root = cq.from(Child.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Child_.name), name));
        TypedQuery<Child> q = em.createQuery(cq);
        q.setMaxResults(1);
        List<Child> children = q.getResultList();
        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }

    public Parent createFamilyInBoundary(String parentName, String childName) {
        log.trace("createFamilyInBoundary");
        Parent parent = new Parent(parentName);
        Child child = new Child(childName);
        parent = em.merge(parent);
        parent.addChild(child);
        return parent;
    }

    public List<Parent> findAll() {
        log.trace("findAll");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Parent> cq = cb.createQuery(Parent.class);
        cq.from(Parent.class);
        return em.createQuery(cq).getResultList();
    }

    public void deleteAll() {
        log.trace("deleteAll");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Parent> cq = cb.createQuery(Parent.class);
        cq.from(Parent.class);
        List<Parent> parents = em.createQuery(cq).getResultList();
        for (Parent parent : parents) {
            em.remove(parent);
        }
    }

}
