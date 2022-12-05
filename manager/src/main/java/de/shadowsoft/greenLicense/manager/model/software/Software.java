package de.shadowsoft.greenLicense.manager.model.software;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@XmlRootElement(name = "software")
@XmlType(propOrder = {"name", "version", "id", "keyPairId", "feature"})
public class Software {
    private List<Feature> features;
    private String id;
    private String keyPairId;
    private String name;
    private String version;

    public Software() {
        features = new ArrayList<>();
        id = UUID.randomUUID().toString();
        keyPairId = "";
        name = "";
        version = "";
    }

    public void addFeature(Feature feature) {
        removeFeature(feature);
        features.add(feature);
    }

    @Override
    public Software clone() {
        Software software = new Software();
        software.setId(id);
        software.setKeyPairId(keyPairId);
        software.setName(name);
        software.setVersion(version);
        for (Feature feature : features) {
            software.getFeatures().add(feature.clone());
        }
        return software;
    }

    @Override
    public String toString() {
        return name + " (" + version + ")";
    }

    public List<Feature> getFeatures() {
        return features;
    }

    @XmlElement(name = "feature")
    public void setFeatures(final List<Feature> features) {
        this.features = features;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(final String id) {
        this.id = id;
    }

    public String getKeyPairId() {
        return keyPairId;
    }

    @XmlAttribute(name = "keyPairId")
    public void setKeyPairId(final String keyPairId) {
        this.keyPairId = keyPairId;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    @XmlAttribute(name = "version")
    public void setVersion(final String version) {
        this.version = version;
    }

    public void removeFeature(String id) {
        Iterator<Feature> it = features.iterator();
        while (it.hasNext()) {
            Feature item = it.next();
            if (item.getId().equals(id)) {
                it.remove();
            }
        }
    }

    public void removeFeature(Feature feature) {
        removeFeature(feature.getId());
    }

    public void marshall(String pathname) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(Software.class);
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
            context = JAXBContext.newInstance(Software.class);
            return (Object) context.createUnmarshaller()
                    .unmarshal(new FileReader(pathname));
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
    
    