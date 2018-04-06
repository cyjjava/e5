package com.e3;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CYJ on 2018/3/5.
 */
public class EUDatagridResult implements Serializable {


    /**
     * total : 28
     * rows : [{"productid":"FI-SW-01","productname":"Koi","unitcost":10,"status":"P","listprice":36.5,"attr1":"Large","itemid":"EST-1"},{"productid":"K9-DL-01","productname":"Dalmation","unitcost":12,"status":"P","listprice":18.5,"attr1":"Spotted Adult Female","itemid":"EST-10"},{"productid":"RP-SN-01","productname":"Rattlesnake","unitcost":12,"status":"P","listprice":38.5,"attr1":"Venomless","itemid":"EST-11"},{"productid":"RP-SN-01","productname":"Rattlesnake","unitcost":12,"status":"P","listprice":26.5,"attr1":"Rattleless","itemid":"EST-12"},{"productid":"RP-LI-02","productname":"Iguana","unitcost":12,"status":"P","listprice":35.5,"attr1":"Green Adult","itemid":"EST-13"},{"productid":"FL-DSH-01","productname":"Manx","unitcost":12,"status":"P","listprice":158.5,"attr1":"Tailless","itemid":"EST-14"},{"productid":"FL-DSH-01","productname":"Manx","unitcost":12,"status":"P","listprice":83.5,"attr1":"With tail","itemid":"EST-15"},{"productid":"FL-DLH-02","productname":"Persian","unitcost":12,"status":"P","listprice":23.5,"attr1":"Adult Female","itemid":"EST-16"},{"productid":"FL-DLH-02","productname":"Persian","unitcost":12,"status":"P","listprice":89.5,"attr1":"Adult Male","itemid":"EST-17"},{"productid":"AV-CB-01","productname":"Amazon Parrot","unitcost":92,"status":"P","listprice":63.5,"attr1":"Adult Male","itemid":"EST-18"}]
     */

    private long total;
    private List<?> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
    }

    public EUDatagridResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public EUDatagridResult() {

    }
}
