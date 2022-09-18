package ro.eu.infoagenda.model;

public class WeatherDetails {
    private String localCurrentTemperature;
    private String realFeelTemperature;
    private String windKmPerH;
    private String overAll;
    private String pressure;

    public WeatherDetails() {
    }

    public WeatherDetails(String localCurrentTemperature, String realFeelTemperature, String windKmPerH, String overAll, String pressure) {
        this.localCurrentTemperature = localCurrentTemperature;
        this.realFeelTemperature = realFeelTemperature;
        this.windKmPerH = windKmPerH;
        this.overAll = overAll;
        this.pressure = pressure;
    }

    public String getLocalCurrentTemperature() {
        return localCurrentTemperature;
    }

    public void setLocalCurrentTemperature(String localCurrentTemperature) {
        this.localCurrentTemperature = localCurrentTemperature;
    }

    public String getRealFeelTemperature() {
        return realFeelTemperature;
    }

    public void setRealFeelTemperature(String realFeelTemperature) {
        this.realFeelTemperature = realFeelTemperature;
    }

    public String getWindKmPerH() {
        return windKmPerH;
    }

    public void setWindKmPerH(String windKmPerH) {
        this.windKmPerH = windKmPerH;
    }

    public String getOverAll() {
        return overAll;
    }

    public void setOverAll(String overAll) {
        this.overAll = overAll;
    }

    public String getPressure() {
        return pressure;
    }

    @Override
    public String toString() {
        return "WeatherDetails{" +
                "localCurrentTemperature='" + localCurrentTemperature + '\'' +
                ", realFeelTemperature='" + realFeelTemperature + '\'' +
                ", windKmPerH='" + windKmPerH + '\'' +
                ", overAll='" + overAll + '\'' +
                ", pressure='" + pressure + '\'' +
                '}';
    }

    public String getPreasure() {
        return pressure;
    }
}
