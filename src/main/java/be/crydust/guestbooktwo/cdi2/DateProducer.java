package be.mips.deleteme.produces.cdi2;

import be.crydust.guestbooktwo.cdi2.ModelQualifier;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@Stateless
public class DateProducer implements Serializable {

    private static final long serialVersionUID = 42L;
    
    @Inject
    DateService dateService;
    
    public DateProducer() {
        System.out.println("*** DateProducer constructor");
    }

    @Produces
    @ModelQualifier
    @SessionScoped
    public ModelDTO fetch() {
        System.out.println("*** DateProducer.fetch");
        return dateService.getDate();
    }
}
