package be.crydust.guestbooktwo.ejb;

import be.crydust.guestbooktwo.entities.Child;
import be.crydust.guestbooktwo.entities.Parent;
import java.util.Properties;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kristof
 */
public class FamilyBoundaryIT {

    private static EJBContainer ejbContainer;

    @Inject
    FamilyBoundary cut;

    @BeforeClass
    public static void startTheContainer() {
//        Properties p = new Properties();
        Properties p = (Properties) System.getProperties().clone();
        p.put("testDB", "new://Resource?type=DataSource");
        p.put("testDB.JdbcDriver", "org.h2.Driver");
        p.put("testDB.JdbcUrl", "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1");
        p.put("guestbooktwoPU.openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
//        p.put("guestbooktwoPU.openjpa.ConnectionFactoryProperties", "PrettyPrint=true, PrettyPrintLineLength=72, PrintParameters=True");
//        p.put("guestbooktwoPU.openjpa.ConnectionFactoryProperties", "PrintParameters=True");
        p.put("guestbooktwoPU.eclipselink.ddl-generation", "drop-and-create-tables");
        p.put("guestbooktwoPU.eclipselink.logging.level", "fine");
        p.put("guestbooktwoPU.eclipselink.logging.logger", "be.crydust.org.eclipse.persistence.logging.SLF4JLogger");
        p.put("guestbooktwoPU.hibernate.hbm2ddl.auto", "create-drop");
        ejbContainer = EJBContainer.createEJBContainer(p);
    }

    @Before
    public void setUp() throws Exception {
        ejbContainer.getContext().bind("inject", this);
    }

    @AfterClass
    public static void afterClass() {
        if (ejbContainer != null) {
            ejbContainer.close();
        }
    }

    @Test
    public void testCreateFamily() {
        
        Parent parent = new Parent("Alice");
        Child child = new Child("Bob");
        parent = cut.createParent(parent);
        child.setParent(parent);
        child = cut.createChild(child);
        assertThat(parent.getId(), is(not(nullValue())));
        assertThat(child.getId(), is(not(nullValue())));
        assertThat(parent.getName(), is("Alice"));
        assertThat(child.getName(), is("Bob"));
        assertThat(child.getParent(), is(parent));
        // retrieve Alice again to get up to date children
        parent = cut.findParentById(parent.getId());
        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren(), contains(child));
    }

    @Test
    public void testCreateFamily2() {
        
        Parent parent = new Parent("Carol");
        Child child = new Child("Dave");
        parent.addChild(child);
        parent = cut.createParent(parent);
        assertThat(parent.getId(), is(not(nullValue())));
        assertThat(child.getId(), is(not(nullValue())));
        assertThat(parent.getName(), is("Carol"));
        assertThat(child.getName(), is("Dave"));
        assertThat(child.getParent(), is(parent));
        // no need to retrieve alice again to get up to date children
        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren(), contains(child));

        Child child2 = new Child("Eve");
        child2.setParent(parent);
        child2 = cut.createChild(child2);
        // retrieve Carol again to get up to date children
        parent = cut.findParentById(parent.getId());
        assertThat(child2.getParent(), is(parent));
        assertThat(parent.getChildren().size(), is(2));
        assertThat(parent.getChildren(), contains(child, child2));
    }

    @Test
    public void testCreateFamilyInBoundary() {
        
        Parent parent = cut.createFamilyInBoundary("Frank", "Mallet");
        Child child = parent.getChildren().get(0);
        assertThat(parent.getId(), is(not(nullValue())));
        assertThat(child.getId(), is(not(nullValue())));
        assertThat(parent.getName(), is("Frank"));
        assertThat(child.getName(), is("Mallet"));
        assertThat(child.getParent(), is(parent));
        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren(), contains(child));
    }
    
    @Test
    public void testCreateOrphan() {
        
        Child child = new Child("Oscar");
        cut.createChild(child);
        assertThat(child.getParent(), is(nullValue()));
        assertThat(child.getId(), is(not(nullValue())));
        assertThat(child.getName(), is("Oscar"));
        
        Parent parent = new Parent("Peggy");
        parent = cut.createParent(parent);
        assertThat(parent.getId(), is(not(nullValue())));
        assertThat(parent.getName(), is("Peggy"));

        parent.addChild(child);
        parent = cut.updateParent(parent);
        
        assertThat(child.getParent(), is(parent));
        assertThat(child.getId(), is(not(nullValue())));
        assertThat(child.getName(), is("Oscar"));
        
        assertThat(parent.getId(), is(not(nullValue())));
        assertThat(parent.getName(), is("Peggy"));
        
        assertThat(parent.getChildren().size(), is(1));
        assertThat(parent.getChildren(), contains(child));
    }
}
