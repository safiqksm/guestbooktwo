package be.crydust.guestbooktwo.ejb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author kristof
 */
public class CriteriaUtilTest {

    @Test
    public void testEscapeSqlWildCards() {
        assertThat(CriteriaUtil.escapeSqlWildCards(""), is(""));
        assertThat(CriteriaUtil.escapeSqlWildCards("a"), is("a"));
        assertThat(CriteriaUtil.escapeSqlWildCards("\\"), is("\\\\"));
        assertThat(CriteriaUtil.escapeSqlWildCards("%"), is("\\%"));
        assertThat(CriteriaUtil.escapeSqlWildCards("_"), is("\\_"));
        assertThat(CriteriaUtil.escapeSqlWildCards("?"), is("?"));
        assertThat(CriteriaUtil.escapeSqlWildCards("*"), is("*"));
        assertThat(CriteriaUtil.escapeSqlWildCards("a\\%_?*"), is("a\\\\\\%\\_?*"));
    }

}
