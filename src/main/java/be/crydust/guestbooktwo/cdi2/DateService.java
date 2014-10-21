package be.mips.deleteme.produces.cdi2;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class DateService implements Serializable {

    private static final long serialVersionUID = 42L;
    
    public DateService() {
        System.out.println("*** DateService constructor");
    }

    public ModelDTO getDate() {
        System.out.println("*** DateService.getDate");
        return new ModelDTO();
    }
}
