package de.shadowsoft.greenLicense.manager.model.software;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.UUID;

@XmlRootElement(name = "feature")
@XmlType(propOrder = {"id", "name", "value"})
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

    @XmlAttribute(name = "id")
    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "value")
    public void setValue(final String value) {
        this.value = value;
    }

    public void marshall(String pathname) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Feature.class);
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            mar.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            mar.marshal(this, new File(pathname));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    public Object unmarshall(String pathname) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Feature.class);
            return (Object) context.createUnmarshaller()
                    .unmarshal(new FileReader(pathname));
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
    
    