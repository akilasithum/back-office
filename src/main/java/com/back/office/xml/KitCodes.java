package com.back.office.xml;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="KITNumbers")
@XmlAccessorType(XmlAccessType.FIELD)
public class KitCodes {

    @XmlElement(name="KITNumber")
    private List<com.back.office.entity.KitCodes> KITNumber;

    public List<com.back.office.entity.KitCodes> getList(){
        return KITNumber;
    }
}
