package sax;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/*
 * Created by maxim on 19.04.14.
 */

public class Sax {
    public static Object readXML(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SaxHandler saxHandler = new SaxHandler();
            saxParser.parse(xmlFile, saxHandler);

            return saxHandler.getObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
