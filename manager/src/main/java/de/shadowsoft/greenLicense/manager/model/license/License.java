package de.shadowsoft.greenLicense.manager.model.license;

import de.shadowsoft.greenLicense.manager.model.software.Software;
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
import java.util.UUID;

@XmlRootElement(name = "license")
@XmlType(propOrder = { "id", "name", "software", "systemId"})
public class License {

    private String id;
    private String name;
    private Software software;
    private String systemId;

    public License() {
        id = UUID.randomUUID().toString();
        systemId = "";
        name = "";
        software = new Software();
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

    public Software getSoftware() {
        return software;
    }

    @XmlElement(name = "software")
    public void setSoftware(final Software software) {
        this.software = software;
    }

    public String getSystemId() {
        return systemId;
    }

    @XmlElement(name = "systemId")
    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }


    public void marshall(String pathname) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(License.class);
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
            context = JAXBContext.newInstance(License.class);
            return (Object) context.createUnmarshaller()
                    .unmarshal(new FileReader(pathname));
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return "License{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", software=" + software +
                ", systemId='" + systemId + '\'' +
                '}';
    }
}
    
    