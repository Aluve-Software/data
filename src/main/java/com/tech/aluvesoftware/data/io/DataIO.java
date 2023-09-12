package com.tech.aluvesoftware.data.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

import static java.lang.invoke.MethodHandles.lookup;
import static org.apache.logging.log4j.LogManager.getLogger;
import static org.apache.logging.log4j.message.ParameterizedMessage.ERROR_PREFIX;

public class DataIO {
    static final Logger logger = getLogger(lookup().lookupClass());
    private final Gson gson = new Gson();


    public <T> T convertJsonToObject(String jsonString, Class<T> chosenClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, chosenClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String simpleJsonRequest(Object o){
        return gson.toJson(o);
    }

    public String marshallEnvelopeObject(Object ob){
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ob.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

            jaxbMarshaller.marshal(ob, sw);
            jaxbMarshaller.marshal(ob, System.out);
        } catch (JAXBException e) {
            logger.info(String.format("%s%s", ERROR_PREFIX, e.getMessage()));
        }
        return sw.toString();
    }
}