package com.devortex.vortextoolbox.helper;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
 
public class XMLHandler extends DefaultHandler{
 
	Boolean currentElement = false;
	String currentValue = null;
	private String sStartElement;
	private String sDownloadAttribute;
	private String sImageUrlElement;
	private String sNameElement;
	private ArrayList<IconsList> iconsListArray;
	private IconsList iconsList;

	public XMLHandler(String startElement, String downloadAttribute, String imageURLElement, String nameElement)
	{
		iconsList = new IconsList();
		iconsListArray = new ArrayList<IconsList>();
		sStartElement = startElement;
		sDownloadAttribute = downloadAttribute;
		sImageUrlElement = imageURLElement;
		sNameElement = nameElement;
	}
	
	public ArrayList<IconsList> getIconsList() {
		return iconsListArray;
	}

	/** Called when tag starts ( ex:- <name>AndroidPeople</name>
	* -- <name> )*/
	@Override
	public void startElement(String uri, String localName, String qName,
	Attributes attributes) throws SAXException {

		currentElement = true;
	
		if (localName.equals(sStartElement))
		{
			/** Start */
			String attr = attributes.getValue(sDownloadAttribute);
			iconsList.setDownloadURL(attr);
		}

	}

	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
	* -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException {

		currentElement = false;
	
		/** set value */
		if (localName.equalsIgnoreCase(sNameElement))
		{
			iconsList.setName(currentValue);
		}
		else if (localName.equalsIgnoreCase(sImageUrlElement))
		{
			iconsList.setImageURL(currentValue);
		}
		else if (localName.equalsIgnoreCase(sStartElement))
		{
			iconsListArray.add(iconsList);
			iconsList = new IconsList();
		}
	}
		
	
		/** Called to get tag characters ( ex:- <name>AndroidPeople</name>
		* -- to get AndroidPeople Character ) */
		@Override
		public void characters(char[] ch, int start, int length)
		throws SAXException {
	
			if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
			}

	}
}