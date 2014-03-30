package be.crydust.guestbooktwo.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Greeting {
    
    private String who;

    public Greeting() {
        this("nobody");
    }

    public Greeting(String who) {
        this.who = who;
    }

    @XmlElement
    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
