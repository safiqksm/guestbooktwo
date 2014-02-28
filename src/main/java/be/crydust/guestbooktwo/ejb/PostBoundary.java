package be.crydust.guestbooktwo.ejb;

import be.crydust.guestbooktwo.entities.Post;
import be.crydust.guestbooktwo.entities.Post_;
import java.util.List;
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
public class PostBoundary {

    @PersistenceContext
    EntityManager em;

    public Post create(Post post) {
        System.out.println("create");
        em.persist(post);
        em.flush();
        em.refresh(post);
        return post;
    }

    public Post readById(Long id) {
        System.out.println("readById");
        return em.find(Post.class, id);
    }

    public List<Post> readAll() {
        System.out.println("readAll");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.select(root);
        //root.join(...)
        //cq.where(...)
        //cq.orderBy(...)
        TypedQuery<Post> q = em.createQuery(cq);
        //q.setMaxResults(10);
        return q.getResultList();
    }

    public Post update(Post post) {
        System.out.println("update");
        return em.merge(post);
    }

    public void delete(Post post) {
        System.out.println("delete");
        em.remove(post);
    }

    public void deleteById(Long id) {
        System.out.println("deleteById");
        Post post = em.getReference(Post.class, id);
        if (post != null) {
            em.remove(post);
        }
    }

    /**
     * Deletes all posts containing bad words.
     *
     * @param word bad word we want to sensor
     * @return number of deleted posts
     */
    public int deleteByWord(String word) {
        System.out.println("deleteByWord");
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("word shouldn't be empty");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.select(root);
        cq.where(cb.like(root.get(Post_.message),
                "%" + CriteriaUtil.escapeSqlWildCards(word) + "%", '\\'));
        TypedQuery<Post> q = em.createQuery(cq);
        List<Post> posts = q.getResultList();
        int count = 0;
        for (Post post : posts) {
            delete(post);
            count++;
        }
        return count;
    }
}
