package com.example.hdb.mobileprogramming;

import java.text.Collator;
import java.util.Comparator;

public class Listview{
    private String dateStr ;
    private String cateStr ;
    private String bDStr ;
    private String moneyStr ;
    private String methodStr;

    public void setDate(String date) {
        dateStr = date ;
    }
    public void setCate(String cate) {
        cateStr = cate ;
    }
    public void setBD(String bD) {
        bDStr = bD ;
    }
    public void setMoney(String money) {
        moneyStr = money ;
    }
    public void setMethod(String method) {
        methodStr = method ;
    }

    public String getDate() {
        return this.dateStr;
    }
    public String getCate() {
        return this.cateStr;
    }
    public String getBD() {
        return this.bDStr;
    }
    public String getMoney() {
        return this.moneyStr;
    }
    public String getMethod() {
        return this.methodStr;
    }

    public static final Comparator<Listview> ALPHA_COMPARATOR = new Comparator<Listview>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(Listview lhs, Listview rhs) {
            return sCollator.compare(lhs.dateStr, rhs.dateStr);
        }
    };
}