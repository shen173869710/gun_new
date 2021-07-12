package com.auto.di.guan.basemodel.model.respone;

public class MeteoRespone extends ERespone{

    private String series;
    private String authorized;
    private String alias;
    private String sn;
    private String type;
    private MeteoLocation location;
    private int itemType;

    private boolean isSle;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MeteoLocation getLocation() {
        return location;
    }

    public void setLocation(MeteoLocation location) {
        this.location = location;
    }


    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public boolean isSle() {
        return isSle;
    }

    public void setSle(boolean sle) {
        isSle = sle;
    }
}
