package com.github.bertramn.issues;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class ModelTest {

    @Test
    public void testSimpleRoundTrip() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Model.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        Model in = new Model();
        in.setCode("MMM-0000");
        in.setDescription("something");
        in.setStatus(Status.OFF);

        StringWriter sw = new StringWriter();

        marshaller.marshal(in, sw);

        System.out.println(sw.toString());

        Unmarshaller unmarshaller = jc.createUnmarshaller();

        Model out = (Model) unmarshaller.unmarshal(new StringReader(sw.toString()));

        System.out.println(out);

    }
}
