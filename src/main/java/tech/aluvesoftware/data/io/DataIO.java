package tech.aluvesoftware.data.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

import static java.lang.invoke.MethodHandles.lookup;
import static org.apache.logging.log4j.LogManager.getLogger;
import static org.apache.logging.log4j.message.ParameterizedMessage.ERROR_PREFIX;

public class DataIO {
    private static final Logger logger = getLogger(lookup().lookupClass());
    private static final Gson gson = new Gson();

    private DataIO() {
    }


    /**
     * Converts a JSON string into a Java object of the specified class using the Jackson ObjectMapper.
     *
     * @param jsonString  The JSON string to be converted.
     * @param chosenClass The class type into which the JSON should be converted.
     * @param <T>         The type of the object to convert to.
     * @return An object of the specified class representing the JSON data.
     * @throws RuntimeException if there is an error during the conversion.
     */
    public static <T> T convertJsonToObject(String jsonString, Class<T> chosenClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, chosenClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a Java object into a JSON string using the Gson library.
     *
     * @param o The Java object to be converted to JSON.
     * @return A JSON string representation of the Java object.
     */
    public static String convertObjectToJson(Object o) {
        return gson.toJson(o);
    }

    /**
     * Marshals a Java object into an XML string using JAXB.
     *
     * @param ob The Java object to be marshaled into XML.
     * @return An XML string representation of the Java object, or null if an error occurs.
     */
    public static String marshallEnvelopeObject(Object ob) {
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

    /**
     * Unmarshals an XML string into an object of the specified class using JAXB.
     *
     * @param xml         The XML string to unmarshal.
     * @param chosenClass The class type into which the XML should be unmarshaled.
     * @param <T>         The type of the object to unmarshal.
     * @return An object of the specified class unmarshaled from the XML string, or null if an error occurs.
     */
    public static <T> T unmarshalEnvelopeObject(String xml, Class<T> chosenClass) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(chosenClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            return chosenClass.cast(jaxbUnmarshaller.unmarshal(reader));
        } catch (JAXBException jaxbException) {
            logger.info(String.format("%s%s", ERROR_PREFIX, jaxbException.getMessage()));
        }
        return null;
    }
}