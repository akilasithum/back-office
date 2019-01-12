package com.back.office.xml;
import com.back.office.entity.CurrencyDetails;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="currencies")
@XmlAccessorType(XmlAccessType.FIELD)
public class Currencies {

    @XmlElement(name="currency")
    private List<CurrencyDetails> currency;

    public List<CurrencyDetails> getList(){
        return currency;
    }
}
