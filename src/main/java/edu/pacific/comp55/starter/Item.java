package edu.pacific.comp55.starter;

import acm.graphics.GImage;
import acm.graphics.GLabel;

public class Item {
	private GImage sprite;
	private String itemType;
	private GLabel message;
	private static final String folder = "/images/"; // images folder path
	
	public Item(GImage image, String name) {
		sprite = image;
		itemType = name;
		message = new GLabel("");
	}
	
	public void setSprite(GImage newImage) {
		sprite = newImage;
	}
	
	public GImage getSprite() {
		return sprite;
	}
	
	public void setLabel(String s) {
		message.setLabel(s);
	}
	
	public GLabel getLabel() {
		return message;
	}
	
	public void setItemType(String s) {
		itemType = s;
	}
	
	public String getItemType() {
		return itemType;
	}
	
	public static void main(String[] args) {

	}

	public static String getFolder() {
		return folder;
	}

}
