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
	
	public Game game;
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
		
		game=new Game();
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
		
		for(int i =0 ; i<game.obstacles.size();i++) {

			int[] x=new int[game.obstacles.get(i).points.size()];
			int[] y=new int[game.obstacles.get(i).points.size()];

			for(int i2 = 0; i2< game.obstacles.get(i).points.size(); i2++) {
				x[i2]=(int)(game.obstacles.get(i).points.get(i2)[0]);
				y[i2]=(int)(game.obstacles.get(i).points.get(i2)[1]);
			}
			
			if(game.obstacles.get(i).points.size()==2) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.ORANGE);
			}

			g.fillPolygon(x,y,game.obstacles.get(i).points.size());
			g.drawPolygon(x,y,game.obstacles.get(i).points.size());
		}
		
		g.setColor(Color.BLACK);
		
		if(creating_obstacle) {
			for(int i =0 ; i< points.size();i++) {
				g.fillOval((int)(points.get(i)[0]-2.5), (int)(points.get(i)[1]-2.5), 5, 5);
			}
		}
		
		g.fillOval((int)(game.ball.pos[0]-game.ball.diameter/2),
				(int)(game.ball.pos[1]-game.ball.diameter/2),
				(int)(game.ball.diameter), 
				(int)(game.ball.diameter));
		
		g.fillRect((int)(game.paddle_1.pos[0]-game.paddle_1.size[0]/2), (int)(game.paddle_1.pos[1]-game.paddle_1.size[1]/2), (int)game.paddle_1.size[0], (int)game.paddle_1.size[1]);
		g.fillRect((int)(game.paddle_2.pos[0]-game.paddle_2.size[0]/2), (int)(game.paddle_2.pos[1]-game.paddle_2.size[1]/2), (int)game.paddle_2.size[0], (int)game.paddle_2.size[1]);
		
		//g.drawRect((int)(game.dx), (int)(game.dy), (int)game.game_size[0], (int)game.game_size[1]);
		
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
			Vector <Obstacle> obst=game.obstacles;
			game=new Game();
			game.obstacles=obst;
			break;
		case KeyEvent.VK_R:
			playing=false;
			game=new Game();
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
						game.obstacles.add(o);
						game.run_sim=true;
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
		case KeyEvent.VK_W:
			if(!game.ai_paddle_1_enabled) {
				game.paddle_1.move_up=true;
			}
			break;
		case KeyEvent.VK_S:
			if(!game.ai_paddle_1_enabled) {
				game.paddle_1.move_down=true;
			}
			break;
		case KeyEvent.VK_UP:
			if(!game.ai_paddle_2_enabled) {
				game.paddle_2.move_up=true;
			}
			break;
		case KeyEvent.VK_DOWN:
			if(!game.ai_paddle_2_enabled) {
				game.paddle_2.move_down=true;
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){ 	
		case KeyEvent.VK_W:
			if(!game.ai_paddle_1_enabled) {
				game.paddle_1.move_up=false;
			}
			break;
		case KeyEvent.VK_S:
			if(!game.ai_paddle_1_enabled) {
				game.paddle_1.move_down=false;
			}
			break;
		case KeyEvent.VK_UP:
			if(!game.ai_paddle_2_enabled) {
				game.paddle_2.move_up=false;
			}
			break;
		case KeyEvent.VK_DOWN:
			if(!game.ai_paddle_2_enabled) {
				game.paddle_2.move_down=false;
			}
			break;
		}
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
		graphics.panel.requestFocus();
		
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
