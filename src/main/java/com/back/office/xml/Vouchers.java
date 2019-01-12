package com.back.office.xml;
import com.back.office.entity.Voucher;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="vouchers")
@XmlAccessorType(XmlAccessType.FIELD)
public class Vouchers {

    @XmlElement(name="voucher")
    private List<Voucher> list;

    public List<Voucher> getList(){
        return list;
    }
}
