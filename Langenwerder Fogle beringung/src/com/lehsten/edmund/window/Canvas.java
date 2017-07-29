package com.lehsten.edmund.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.nio.Buffer;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.lehsten.edmund.ImageTransforme.PerspectiveTransform;

public class Canvas extends JPanel {
	private BufferedImage image;
	private Point[] corners = new Point[4];
	private int xOffset=0,yOffset=0;
	private double xScale=1,yScale=1;
	protected BufferedImage newimage;
	private boolean transformed = false;
	private int numHorizontalLines;
	int[][] lines =new int[2][];
	public Canvas( BufferedImage im) {
		this.image=im;
		addMouseListener(new MouseListener() {



			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				//add points
				int x = (int) ((double)(e.getX()-xOffset)/xScale);
				int y = (int) ((double)(e.getY()-yOffset)/yScale);
				System.out.println("Mouse triggered: X="+e.getX()+",Y="+e.getY()+",x="+x+",y="+y);

				if(!transformed && image != null && (x>=0 && x < image.getWidth()) && (y >= 0 && y < image.getHeight())){
					if (e.getX()> getWidth()/2){//right side
						if(e.getY()>getHeight()/2){//bottom
							corners[2] = new Point(x,y);
						}else{//top
							corners[1] = new Point(x,y);
						}
					}else{//left side
						if(e.getY()>getHeight()/2){//bottom
							corners[3] = new Point(x,y);
						}else{//top
							corners[0] = new Point(x,y);
						}
					}
				}

				if(!transformed && corners[0] != null && corners[1] != null && corners[2] != null && corners[3] != null){// all corners are detected
					//adjusting left corners to include numbers
					int newY = (int) (corners[1].getY()+(1.065934066*(corners[0].getY()-corners[1].getY())));
					int newX =(int) (corners[0].getX()-((double)(0.065934066*(corners[1].getX()-corners[0].getX()))));
					corners[0] = new Point(newX, newY);

					newY = (int) (corners[2].getY()+(1.065934066*(corners[3].getY()-corners[2].getY())));
					newX = (int) (corners[3].getX()-((double)(0.065934066*(corners[2].getX()-corners[3].getX()))));
					corners[3] = new Point(newX, newY);

					//transforming image
					image = PerspectiveTransform.computeFromPoints(image, corners[0],corners[3],corners[2],corners[1]);

					System.out.println("Recalculated image \n" 
							+ corners[0] + "-> 0,0"
							+ corners[1] + "-> 0,w"
							+ corners[2] + "-> h,w"
							+ corners[3] + "-> h,0");
					transformed = true;
					repaint();
				}

			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void setImage(BufferedImage image){
		this.image = image;
		corners = new Point[4];
		transformed = false;
		revalidate();
	}
	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		if (image != null){
			setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));

			float imageRatio = ((float) (image.getHeight()))/((float) (image.getWidth()));
			float windowRatio = ((float) getHeight())/((float) getWidth());
			int height = image.getHeight(),width= image.getWidth();
			if(imageRatio > windowRatio){
				height = getHeight();
				width = (int) ((1/(imageRatio))*height);
				xOffset = (int) ((getWidth()-width)/2);
				yOffset = 0;
			}else{
				width = getWidth();
				height = (int) imageRatio*width;
				yOffset = (int) ((getHeight()-height)/2);
				xOffset = 0;
			}			xScale = ((double) width)/((double) image.getWidth());
			yScale = ((double) height)/((double) image.getHeight());
			g.drawImage(image,xOffset,yOffset,width+xOffset,height+yOffset,0,0,image.getWidth(),image.getHeight(),this);
			if(transformed){
				//increasing contrast
				BufferedImage contrast = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
				Graphics t = contrast.getGraphics();
				t.drawImage(image, 0, 0, null);
				RescaleOp rescaleOp = new RescaleOp(1f,0, null);
				rescaleOp.filter(image,contrast); 
				g.clearRect(xOffset, yOffset, width, height);
				g.drawImage(contrast,xOffset,yOffset,width+xOffset,height+yOffset,0,0,image.getWidth(),image.getHeight(),this);



				g.setColor(Color.red);
				/*
				//vertical lines
				g.drawLine((int)((float)(width)*0.0564f)+xOffset, yOffset, (int)((float)(width)*0.0564f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.1385f)+xOffset, yOffset, (int)((float)(width)*0.1385f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.2795f)+xOffset, yOffset, (int)((float)(width)*0.2795f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.3390f)+xOffset, yOffset, (int)((float)(width)*0.3390f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.4124f)+xOffset, yOffset, (int)((float)(width)*0.4124f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.5333f)+xOffset, yOffset, (int)((float)(width)*0.5333f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.6271f)+xOffset, yOffset, (int)((float)(width)*0.6271f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.6977f)+xOffset, yOffset, (int)((float)(width)*0.6974f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.7952f)+xOffset, yOffset, (int)((float)(width)*0.7949f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.8670f)+xOffset, yOffset, (int)((float)(width)*0.8667f)+xOffset, height+yOffset);
				g.drawLine((int)((float)(width)*0.9490f)+xOffset, yOffset, (int)((float)(width)*0.9487f)+xOffset, height+yOffset);

				//horizontal lines
				double rowHeight = height/numHorizontalLines; 
			for(int i = 1; i<=numHorizontalLines;i++){
				g.drawLine(xOffset,(int) (yOffset+(double)(i*rowHeight)), width+xOffset,  (int) (yOffset+(double)(i*rowHeight)));
			}*/

				//vertical line detection

				//line detection
				int[][] horizontalLines = new int[2][(numHorizontalLines-1)];
				int[] horizontalLinesDifferences = new int[image.getHeight()];
				int[] verticalLineDifferences = new int[image.getWidth()];
				int lastChanged = 0;
				int HorizontalLineThickness = image.getHeight()/100;
				System.out.println("Start first loop");
				for(int i = image.getHeight()/200; i<image.getHeight()-1;i++){

					int difference = 0;
					for(int k = 0; k<image.getWidth();k++){
						int rgb1 = contrast.getRGB(k, i);
						int r1 = (rgb1 >> 16) & 0xFF;
						int g1 = (rgb1 >> 8) & 0xFF;
						int b1 = (rgb1 & 0xFF);
						int gray1 = (r1 + g1 + b1) / 3;
						if (gray1<127){
							gray1=0;
						}else{
							gray1=255;
						}
						horizontalLinesDifferences[i-1] += gray1;
						horizontalLinesDifferences[i-2] -= gray1;
						difference += gray1;
						if (k>0){
							verticalLineDifferences[k]   += gray1;
							verticalLineDifferences[k-1] -= gray1;
						}
					}
					//horizontal line detection
					if (i-lastChanged>HorizontalLineThickness && horizontalLinesDifferences[i-2]<-37000)
						for(int j = 0; j<horizontalLines[0].length;j++){
							if(horizontalLines[0][j]>horizontalLinesDifferences[i-2]){
								System.out.println("Line At: " + i + "  Value Of:" + horizontalLinesDifferences[i-2]);
								for(int k=horizontalLines[0].length-1;k>j;k--){
									horizontalLines[0][k] = horizontalLines[0][k-1];
									horizontalLines[1][k] = horizontalLines[1][k-1];
								}
								horizontalLines[0][j] = horizontalLinesDifferences[i-2];
								horizontalLines[1][j] = i;
								lastChanged = i;
								break;

							}
						}
				}
				//vertical Line detection
				int verticalLineThickness = image.getWidth()/50;
				int[][] verticalLines = new int[2][11];
				lastChanged=0;
				for (int i = image.getWidth()/200; i<verticalLineDifferences.length;i++){
					if(i-lastChanged>verticalLineThickness && verticalLineDifferences[i]>30000)
					for(int j = 0; j< verticalLines[0].length;j++){
						if(verticalLines[0][j]<verticalLineDifferences[i]){
							System.out.println("Line At: " + i + "  Value Of:" + verticalLineDifferences[i]);
							for(int k = verticalLines[1].length-1;k>j;k--){
								verticalLines[0][k]=verticalLines[0][k-1];
								verticalLines[1][k]=verticalLines[1][k-1];
							}
							verticalLines[0][j] = verticalLineDifferences[i];
							verticalLines[1][j] = i;
							lastChanged = i;
							break;
						}
					}
				}
				
				//storing lines
				lines[0] = horizontalLines[1];
				lines[1] = verticalLines[1];
				Arrays.sort(lines[0]);
				Arrays.sort(lines[1]);
				//drawing lines
				System.out.println("start drawing loop");
				for(int i = 0; i<horizontalLines[1].length;i++){
					g.drawLine(xOffset, (int) ((horizontalLines[1][i]*yScale)+yOffset), width+xOffset,(int) ((horizontalLines[1][i]*yScale)+yOffset));
				}
				for(int i = 0; i<verticalLines[1].length;i++){
					g.drawLine((int) ((verticalLines[1][i]*xScale)+xOffset), yOffset, (int) ((verticalLines[1][i]*xScale)+xOffset), height+yOffset);
				}
			}
		}else{
			System.out.println("image == nul");
		}
	}

	public void setNumHorizontal(int number){
		numHorizontalLines = number;
	}
	
	public int[][] getLines(){
		return lines;
	}
	
	public BufferedImage getImage(){
		return image;
	}

}
