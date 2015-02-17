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

public class ValuesDocument {

    public Document build(List<String> values) throws Exception {
        Document document = getNewDocument();
        Element valuesElement = document.createElement( "uniqueValues" );
        document.appendChild( valuesElement);

		for (String value : values) {
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
