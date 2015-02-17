/*
 * Copyright 2014 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */

package au.org.emii.geoserver.extensions.filters.layer.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import javax.sql.DataSource;
import java.util.*;




public class ValuesDocument {




    public Document build(Set values) throws Exception {
        Document document = getNewDocument();
        Element valuesElement = document.createElement( "uniqueValues" );
        document.appendChild( valuesElement);

        for (String value : convert(values)) {
              Element element = document.createElement( "value" );
              element.appendChild(document.createTextNode(value));
              valuesElement.appendChild(element);
        }
        return document;
    }

    private Document getNewDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        return docBuilder.newDocument();
    }


    private List<String> convert(Set result) {

        // Set result = new TreeSet(visitor.getUnique());
        // all elts are guaranteed to be the same type
        Class clazz = result.iterator().next().getClass();

        // list stuff should probably be done near the document formatter, since it's an output type.
        List<String> result2 = new ArrayList<String>();

        if (clazz.equals(Boolean.class)) {
            for(Object value : result) {
                result2.add(Boolean.toString((Boolean)value));
            }
        }
        else if (clazz.equals(Integer.class)) {
            for(Object value : result) {
                result2.add(Integer.toString((Integer)value));
            }
        }
        else if (clazz.equals(Long.class)) {
            for(Object value : result) {
                result2.add(Long.toString((Long)value));
            }
        }
        else if (clazz.equals(Float.class)) {
            for(Object value : result) {
                result2.add(Float.toString((Float)value));
            }
        }
        else if (clazz.equals(Double.class)) {
            for(Object value : result) {
                result2.add(Double.toString((Double)value));
            }
        }
        else if (clazz.equals(String.class)) {
            for(Object value : result) {
                result2.add((String)value);
            }
        }
        else if (clazz.equals(java.sql.Date.class)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            for(Object value : result) {
                result2.add(df.format((Date)value ));
            }
        }
        else {
           throw new RuntimeException("Unrecognized type" );
        }

        return result2;
    }

 
/*
    private Document getNewDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        return docBuilder.newDocument();
    }

    private void appendFilter(Document document, Node filtersElement, Filter filter) {
        Element filterElement = appendChild(document, filtersElement, "filter");

        appendChild(document, filterElement, "name").appendChild(document.createTextNode(filter.getName()));
        appendChild(document, filterElement, "type").appendChild(document.createTextNode(filter.getType()));
        appendChild(document, filterElement, "label").appendChild(document.createTextNode(filter.getLabel()));
        appendChild(document, filterElement, "visualised").appendChild(document.createTextNode(filter.getVisualised().toString()));
    }

    private Element appendChild(Document document, Node parent, String name) {
        Element element = document.createElement(name);
        parent.appendChild(element);

        return element;
    }
*/
}
