package com.auto.di.guan.basemodel.model.respone;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class EDepthRespone implements MultiItemEntity {
    private String averageWindSpeed;
    private String rainfall;
    private String solarRadiationIntensity;
    private String airTemperature;
    private String solarRadiationAmount;
    private String relativeHumidity;
    private String outsideVoltage;
    private String windDirection;
    private String battery;
    private String atmosphericPressure;
    private String temperature;
    private String datetime;
    private String dapthName;
    private String moisture;

    private int type;

    public String getAverageWindSpeed() {
        return averageWindSpeed;
    }

    public void setAverageWindSpeed(String averageWindSpeed) {
        this.averageWindSpeed = averageWindSpeed;
    }

    public String getRainfall() {
        return rainfall;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    public String getSolarRadiationIntensity() {
        return solarRadiationIntensity;
    }

    public void setSolarRadiationIntensity(String solarRadiationIntensity) {
        this.solarRadiationIntensity = solarRadiationIntensity;
    }

    public String getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(String airTemperature) {
        this.airTemperature = airTemperature;
    }

    public String getSolarRadiationAmount() {
        return solarRadiationAmount;
    }

    public void setSolarRadiationAmount(String solarRadiationAmount) {
        this.solarRadiationAmount = solarRadiationAmount;
    }

    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public String getOutsideVoltage() {
        return outsideVoltage;
    }

    public void setOutsideVoltage(String outsideVoltage) {
        this.outsideVoltage = outsideVoltage;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getAtmosphericPressure() {
        return atmosphericPressure;
    }

    public void setAtmosphericPressure(String atmosphericPressure) {
        this.atmosphericPressure = atmosphericPressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDapthName() {
        return dapthName;
    }

    public void setDapthName(String dapthName) {
        this.dapthName = dapthName;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }


    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
