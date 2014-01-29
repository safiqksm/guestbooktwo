package be.crydust.guestbooktwo.ejb;

import be.crydust.guestbooktwo.entities.Post;
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
public class PostBoundaryIT {

    private static EJBContainer ejbContainer;

    @Inject
    PostBoundary cut;

    @BeforeClass
    public static void startTheContainer() {

        Properties p = new Properties();
        p.put("testDB", "new://Resource?type=DataSource");
        p.put("testDB.JdbcDriver", "org.h2.Driver");
        p.put("testDB.JdbcUrl", "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1");
        p.put("guestbooktwoPU.openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
        p.put("guestbooktwoPU.eclipselink.ddl-generation", "drop-and-create-tables");
        p.put("guestbooktwoPU.hibernate.hbm2ddl.auto", "create-drop");
        p.put("guestbooktwoPU.hibernate.transaction.jta.platform", "org.apache.openejb.hibernate.OpenEJBJtaPlatform2");
        p.put("guestbooktwoPU.hibernate.transaction.manager_lookup_class", "org.apache.openejb.hibernate.TransactionManagerLookup");

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

    @Test(expected = Exception.class)
    public void testDeleteByWordNull() {
        cut.deleteByWord(null);
    }

    @Test(expected = Exception.class)
    public void testDeleteByWordEmpty() {
        cut.deleteByWord("");
    }

    @Test
    public void testDeleteByWordNothingToDelete() {
        assertThat(cut.deleteByWord("expletive"), is(0));
    }

    @Test
    public void testDeleteByWordOneToDelete() {
        Post post1 = new Post();
        post1.setAuthor("tester");
        post1.setMessage("message");
        cut.create(post1);
        assertThat(cut.deleteByWord("expletive"), is(0));
        Post post2 = new Post();
        post2.setAuthor("tester");
        post2.setMessage("message expletive");
        cut.create(post2);
        assertThat(cut.deleteByWord("expletive"), is(1));
        assertThat(cut.deleteByWord("expletive"), is(0));
    }

}
