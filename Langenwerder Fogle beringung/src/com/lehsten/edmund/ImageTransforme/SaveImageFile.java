package com.lehsten.edmund.ImageTransforme;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class SaveImageFile {
	private BufferedImage original, newImage;
	private int[] verticalLines,horizontalLines;
	private String birdName;
	private int from,to;
	private Point[] corners;
	
	public BufferedImage getOriginal() {
		return original;
	}
	public void setOriginal(BufferedImage original) {
		this.original = original;
	}
	public BufferedImage getNewImage() {
		return newImage;
	}
	public void setNewImage(BufferedImage newImage) {
		this.newImage = newImage;
	}
	public int[] getVerticalLines() {
		return verticalLines;
	}
	public void setVerticalLines(int[] verticalLines) {
		this.verticalLines = verticalLines;
	}
	public int[] getHorizontalLines() {
		return horizontalLines;
	}
	public void setHorizontalLines(int[] horizontalLines) {
		this.horizontalLines = horizontalLines;
	}
	public String getBirdName() {
		return birdName;
	}
	public void setBirdName(String birdName) {
		this.birdName = birdName;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public Point[] getCorners() {
		return corners;
	}
	public void setCorners(Point[] corners) {
		this.corners = corners;
	}
}
