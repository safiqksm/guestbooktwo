package be.crydust.guestbooktwo.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author kristof
 */
@Entity
public class Child implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @ManyToOne
    /*
    Adding the JoinColumn annotation fails with hibernate 4.3.1.
    It does work with hibernate 4.3.0, so I'm leaving it in.
org.apache.openejb.OpenEjbContainer$AssembleApplicationException: org.apache.openejb.OpenEJBException: Creating application failed: /Users/kristof/Projects/guestbooktwo/target: javax.persistence.JoinColumn.foreignKey()Ljavax/persistence/ForeignKey;
	at org.hibernate.cfg.AnnotationBinder.bindManyToOne(AnnotationBinder.java:2881)
	at org.hibernate.cfg.AnnotationBinder.processElementAnnotations(AnnotationBinder.java:1795)
	at org.hibernate.cfg.AnnotationBinder.processIdPropertiesIfNotAlready(AnnotationBinder.java:963)
    */
    @JoinColumn
    private Parent parent;

    public Child() {
    }

    public Child(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Child)) {
            return false;
        }
        Child other = (Child) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Child{" + "id=" + id + ", name=" + name + '}';
    }

  
}
