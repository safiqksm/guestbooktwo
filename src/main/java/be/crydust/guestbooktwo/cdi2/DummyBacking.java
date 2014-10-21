package be.mips.deleteme.produces.cdi2;

import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class DummyBacking implements Serializable {

    private static final long serialVersionUID = 42L;
    
    public String xxx = "JA";

    public String getXxx() {
        return xxx;
    }

    public void setXxx(String xxx) {
        this.xxx = xxx;
    }
    
}
