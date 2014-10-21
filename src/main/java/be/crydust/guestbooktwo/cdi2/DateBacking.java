package be.mips.deleteme.produces.cdi2;

import be.crydust.guestbooktwo.cdi2.ModelQualifier;
import java.io.Serializable;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class DateBacking implements Serializable {

    private static final long serialVersionUID = 42L;

    @Inject
    @ModelQualifier
    ModelDTO modelDTO;

    public DateBacking() {
        System.out.println("*** DateBacking constructor");
    }

    public String getDate() {
        System.out.println("*** DateBacking.getDate");
        return modelDTO == null ? "NULL" : modelDTO.getDate().toString();
//        return "aarg";
    }

    public void setDate() {
        System.out.println("*** DateBacking.setDate");
    }
}
