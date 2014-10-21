package be.mips.deleteme.produces.cdi2;

import java.io.Serializable;
import java.util.Date;

public class ModelDTO  implements Serializable {

    private static final long serialVersionUID = 42L;
    
    Date date;
    
    public ModelDTO() {
        System.out.println("*** ModelDTO constructor");
        this.date = new Date();
    }

    public Date getDate() {
        System.out.println("*** ModelDTO getDate");
        return date;
    }

    public void setDate(Date date) {
        System.out.println("*** ModelDTO setDate");
        this.date = date;
    }
    
}
