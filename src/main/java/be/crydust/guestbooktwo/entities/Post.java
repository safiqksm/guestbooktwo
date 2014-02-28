package be.crydust.guestbooktwo.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kristof
 */
@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;
    @Column
    @NotNull
    @Size(min = 2, max = 64)
    String author;
    @Column
    @NotNull
    @Size(min = 2, max = 256)
    String message;

    public Post() {
    }

    public Post(String author, String message) {
        this();
        this.author = author;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", author=" + author + ", message=" + message + '}';
    }

}
