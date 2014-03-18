package be.crydust.guestbooktwo.web;

import be.crydust.guestbooktwo.ejb.FamilyBoundary;
import be.crydust.guestbooktwo.entities.Child;
import be.crydust.guestbooktwo.entities.Parent;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author kristof
 */
@Startup
@Singleton
public class Bootstrap {

    @Inject
    FamilyBoundary familyBoundary;

    @PostConstruct
    public void init() {
        Parent parent;
        Child child;
        familyBoundary.deleteAll();
        parent = new Parent("1. Parent");
        parent.addChild(new Child("1.1. Child"));
        familyBoundary.createParent(parent);
        parent = new Parent("2. Parent");
        parent.addChild(new Child("2.1. Child"));
        parent.addChild(new Child("2.2. Child"));
        familyBoundary.createParent(parent);
        parent = new Parent("3. Parent");
        parent.addChild(new Child("3.1. Child"));
        parent.addChild(new Child("3.2. Child"));
        parent.addChild(new Child("3.3. Child"));
        familyBoundary.createParent(parent);
    }
}
