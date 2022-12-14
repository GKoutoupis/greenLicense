package de.shadowsoft.greenLicense.manager.model.software;

import java.util.UUID;

public class Feature {
    private String id;
    private String name;
    private String value;

    public Feature() {
        id = UUID.randomUUID().toString();
        name = "";
        value = "";
    }

    public Feature(String name, String value){
        id = UUID.randomUUID().toString();
        this.name = name;
        this.value = value;
    }

    @Override
    public Feature clone() {
        Feature feature = new Feature();
        feature.setId(id);
        feature.setName(name);
        feature.setValue(value);
        return feature;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
    
    