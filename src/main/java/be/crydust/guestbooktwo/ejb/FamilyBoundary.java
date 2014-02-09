package be.crydust.guestbooktwo.ejb;

import be.crydust.guestbooktwo.entities.Child;
import be.crydust.guestbooktwo.entities.Child_;
import be.crydust.guestbooktwo.entities.Parent;
import be.crydust.guestbooktwo.entities.Parent_;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author kristof
 */
@Stateless
public class FamilyBoundary {

    @PersistenceContext
    EntityManager em;

    public Parent createParent(Parent parent) {
        System.out.println("createParent");
        em.persist(parent);
        em.flush();
        em.refresh(parent);
        return parent;
    }

    public Parent updateParent(Parent parent) {
        System.out.println("updateParent");
        parent = em.merge(parent);
        em.flush();
        em.refresh(parent);
        return parent;
    }

    public Parent findParentById(Long id) {
        System.out.println("findParentById");
        return em.find(Parent.class, id);
    }

    public Child createChild(Child child) {
        System.out.println("createChild");
        Parent parent = child.getParent();
        if (parent != null) {
            if (!em.contains(parent)) {
                parent = em.merge(parent);
            }
            parent.addChild(child);
            em.persist(parent);
        } else {
            em.persist(child);
        }
        em.flush();
        em.refresh(child);
        return child;
    }

    public Child findChildById(Long id) {
        System.out.println("findChildById");
        return em.find(Child.class, id);
    }

    public Child addChildToParent(Child child, Parent parent) {
        System.out.println("addChildToParent");
        child.setParent(parent);
        em.persist(child);
        em.flush();
        em.refresh(child);
        return child;
    }

    public Parent findParentByName(String name) {
        System.out.println("findParentByName");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Parent> cq = cb.createQuery(Parent.class);
        Root<Parent> root = cq.from(Parent.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Parent_.name), name));
        TypedQuery<Parent> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    public Child findChildByName(String name) {
        System.out.println("findChildByName");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Child> cq = cb.createQuery(Child.class);
        Root<Child> root = cq.from(Child.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Child_.name), name));
        TypedQuery<Child> q = em.createQuery(cq);
        return q.getSingleResult();
    }

    public Parent createFamilyInBoundary(String parentName, String childName) {
        System.out.println("createFamilyInBoundary");
        Parent parent = new Parent(parentName);
        Child child = new Child(childName);
        parent = createParent(parent);
        child.setParent(parent);
        createChild(child);
        // refresh to retrieve all children
        em.refresh(parent);
        return parent;
    }

}
