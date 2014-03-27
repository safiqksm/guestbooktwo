package be.crydust.guestbooktwo.rest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ComplicatedGreeting {

    private String aString;
    private String aNotherString;
    private Integer aInteger;
    private Long aLong;
    private BigInteger aBigInteger;
    private Float aFloat;
    private Double aDouble;
    private BigDecimal aBigDecimal;
    private Boolean aBoolean;
    private Date aDate;
    private Greeting aGreeting;
    private List<Greeting> greetings;

    public ComplicatedGreeting() {
    }

    public static ComplicatedGreeting createComplicatedGreeting() {
        return new ComplicatedGreeting(
                null,
                "a:áâäà c:ç e:éêëè i:íîïì\u0131 I:\u0130 o:óôöò u:úûüù euro:\u20AC tree:\u2F4A",
                1,
                Long.MAX_VALUE,
                new BigInteger("9999999999999999999999"),
                0.1F,
                0.1,
                new BigDecimal("9999999999999999999999.1"),
                Boolean.TRUE,
                new Date(),
                new Greeting("a"),
                Arrays.asList(
                new Greeting("b"),
                new Greeting("c")));
    }

    public ComplicatedGreeting(String aString, String aNotherString, Integer aInteger, Long aLong, BigInteger aBigInteger, Float aFloat, Double aDouble, BigDecimal aBigDecimal, Boolean aBoolean, Date aDate, Greeting aGreeting, List<Greeting> greetings) {
        this.aString = aString;
        this.aNotherString = aNotherString;
        this.aInteger = aInteger;
        this.aLong = aLong;
        this.aBigInteger = aBigInteger;
        this.aFloat = aFloat;
        this.aDouble = aDouble;
        this.aBigDecimal = aBigDecimal;
        this.aBoolean = aBoolean;
        this.aDate = aDate;
        this.aGreeting = aGreeting;
        this.greetings = greetings;
    }

    @XmlElement(nillable = true)
    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    @XmlElement(nillable = true)
    public String getaNotherString() {
        return aNotherString;
    }

    public void setaNotherString(String aNotherString) {
        this.aNotherString = aNotherString;
    }

    @XmlElement(nillable = true)
    public Integer getaInteger() {
        return aInteger;
    }

    public void setaInteger(Integer aInteger) {
        this.aInteger = aInteger;
    }

    @XmlElement(nillable = true)
    public Long getaLong() {
        return aLong;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    @XmlElement(nillable = true)
    public BigInteger getaBigInteger() {
        return aBigInteger;
    }

    public void setaBigInteger(BigInteger aBigInteger) {
        this.aBigInteger = aBigInteger;
    }

    @XmlElement(nillable = true)
    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    @XmlElement(nillable = true)
    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    @XmlElement(nillable = true)
    public BigDecimal getaBigDecimal() {
        return aBigDecimal;
    }

    public void setaBigDecimal(BigDecimal aBigDecimal) {
        this.aBigDecimal = aBigDecimal;
    }

    @XmlElement(nillable = true)
    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    @XmlElement(nillable = true)
    public Date getaDate() {
        return aDate;
    }

    public void setaDate(Date aDate) {
        this.aDate = aDate;
    }

    @XmlElement(nillable = true)
    public Greeting getaGreeting() {
        return aGreeting;
    }

    public void setaGreeting(Greeting aGreeting) {
        this.aGreeting = aGreeting;
    }

    @XmlElement(nillable = true)
    public List<Greeting> getGreetings() {
        return greetings;
    }

    public void setGreetings(List<Greeting> greetings) {
        this.greetings = greetings;
    }
}
