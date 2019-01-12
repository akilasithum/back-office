package com.back.office.xml;
import com.back.office.entity.EquipmentDetails;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="Equipments")
@XmlAccessorType(XmlAccessType.FIELD)
public class Equipments {

    @XmlElement(name="Equipment")
    private List<EquipmentDetails> list;

    public List<EquipmentDetails> getList(){
        return list;
    }
}
