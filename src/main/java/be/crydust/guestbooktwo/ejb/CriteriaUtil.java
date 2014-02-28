package be.crydust.guestbooktwo.ejb;

/**
 *
 * @author kristof
 */
public final class CriteriaUtil {

    private CriteriaUtil() {
        // NOOP
    }
    
    public static String escapeSqlWildCards(String input) {
        return input.replaceAll("([\\\\%_])", "\\\\$1");
    }

}
