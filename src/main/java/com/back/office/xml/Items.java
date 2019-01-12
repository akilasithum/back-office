package com.back.office.xml;
import com.back.office.entity.ItemDetails;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="items")
@XmlAccessorType(XmlAccessType.FIELD)
public class Items {

    @XmlElement(name="item")
    private List<ItemDetails> item;

    public List<ItemDetails> getList(){
        return item;
    }
}
