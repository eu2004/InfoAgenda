package ro.eu.infoagenda.model;

import java.util.HashMap;
import java.util.Map;

public class Info<T> {
    private InfoContent info;
    private Map<String, String> meta = new HashMap<>();

    public Info(InfoContent<T> info) {
        this.info = info;
    }

    public InfoContent<T> getInfo() {
        return info;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void addMeta(String metaKey, String metaValue) {
        this.meta.put(metaKey, metaValue);
    }
}
