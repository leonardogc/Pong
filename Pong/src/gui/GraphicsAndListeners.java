package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.Obstacle;
import logic.Game;

public class GraphicsAndListeners extends JPanel implements KeyListener, MouseListener{
	
	public Game space;
	public GraphicInterface graphics;
	public boolean playing;
	private LoopThread thread;
	public int pictureNumber; //only used for taking pictures
	public boolean take_pictures;
	
	public Vector<double[]> points;
	public boolean creating_obstacle;
	
	
	public GraphicsAndListeners(GraphicInterface g){
		addKeyListener(this);
		addMouseListener(this);
	
		creating_obstacle=false;
		
		space=new Game();
		this.graphics=g;
		
		playing=false;
		
		pictureNumber=1;
		take_pictures=false;
		
		thread=new LoopThread(this);
		thread.setRunning(true);
		thread.start();
	}
	
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);

		g.fillOval((int)(space.ball.pos[0]-space.ball.diameter/2),
				(int)(space.ball.pos[1]-space.ball.diameter/2),
				(int)(space.ball.diameter), 
				(int)(space.ball.diameter));

		if(creating_obstacle) {
			for(int i =0 ; i< points.size();i++) {
				g.fillOval((int)(points.get(i)[0]-2.5), (int)(points.get(i)[1]-2.5), 5, 5);
			}
		}

		for(int i =0 ; i<space.obstacles.size();i++) {
			g.setColor(Color.BLACK);

			int[] x=new int[space.obstacles.get(i).points.size()];
			int[] y=new int[space.obstacles.get(i).points.size()];

			for(int i2 = 0; i2< space.obstacles.get(i).points.size(); i2++) {
				x[i2]=(int)(space.obstacles.get(i).points.get(i2)[0]);
				y[i2]=(int)(space.obstacles.get(i).points.get(i2)[1]);
			}

			g.drawPolygon(x,y,space.obstacles.get(i).points.size());
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){ 	
		case KeyEvent.VK_RIGHT:
			if(playing){
				playing=false; 
			}
			else{
			playing=true; 
			}
			
			System.out.println("Playing: " + playing);
		break;  
		case KeyEvent.VK_LEFT:
			playing=false;
			space=new Game();
			break;
		case KeyEvent.VK_P:
			this.take_pictures=!this.take_pictures;
			
			if(this.take_pictures) {
				System.out.println("Taking Pictures");
			}
			else {
				System.out.println("Stopped Taking Pictures");
			}
			break;
		case KeyEvent.VK_O:
			if(!playing) {
				if(creating_obstacle) {
					if(points.size() > 2) {
						Obstacle o = new Obstacle(points);
						space.obstacles.add(o);
					}
					creating_obstacle = !creating_obstacle;
					System.out.println("Obstacle Created!");
				}
				else{
					points= new Vector<double[]>();
					creating_obstacle = !creating_obstacle;
					System.out.println("Creating Obstacle!");
				}
			}
			break;
		}

		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		graphics.panel.requestFocusInWindow();
		
		if(creating_obstacle) {
			points.add(new double[] {arg0.getX(), arg0.getY()});
		}
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	
	public void takePicture() {
	    BufferedImage img = new BufferedImage(graphics.panel.getWidth(), graphics.panel.getHeight(), BufferedImage.TYPE_INT_RGB);
	    graphics.panel.print(img.getGraphics()); // or: panel.printAll(...);
	    try {
	        ImageIO.write(img, "png", new File("images/"+pictureNumber+".png"));
	        pictureNumber++;
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}

}
