package logic;

import java.util.Vector;

public class Paddle {
	public enum Pointing{
		up, down, left, right
	}
	
	public Obstacle p;
	public Pointing pointing;
	public double pos[];
	public double pos_buffer[];
	public boolean ai;
	
	public static final double paddle_size_y=150;
	public static final double paddle_radius=150;
	public static final double number_of_points=8;
	public static final double paddle_vel=500;
	
	
	public Paddle(double dx, double dy, Pointing pointing, boolean ai) {
		this.pointing=pointing;
		this.pos= new double[] {dx, dy};
		this.pos_buffer= new double[] {dx, dy};
		this.ai=ai;
		
		double y;
		double x;
		
		double v1_x;
		double v1_y;
		double v1_t;
		
		double v2_x;
		double v2_y;
		
		double angle_step;
		
		double rotation=0;
		
		y=Math.sqrt(Math.pow(paddle_radius,2)-Math.pow(paddle_size_y/2, 2));
		x=Math.sqrt(Math.pow(paddle_radius,2)-Math.pow(y, 2));
		
		v1_x=x;
		v1_y=y;
		
		v1_t=Math.sqrt(v1_x*v1_x+v1_y*v1_y);
		
		
		angle_step= (Math.PI - 2*Math.acos(v1_x/v1_t))/(number_of_points-1);
		
		Vector<double[]>points = new Vector<double[]>();
		
		for(int i=0; i< number_of_points/2; i++) {
			points.add(new double[] {v1_x, -(v1_y-y)}); // negative because of y axis inverted
			
			v2_x=Math.cos(angle_step)*v1_x-Math.sin(angle_step)*v1_y;
			v2_y=Math.sin(angle_step)*v1_x+Math.cos(angle_step)*v1_y;
			
			v1_x=v2_x;
			v1_y=v2_y;
		}
		
		for(int i=points.size()-1; i >= 0; i--) {
			points.add(new double[] {-points.get(i)[0], points.get(i)[1]});
		}
		
		switch(pointing) {
		case up:
			rotation=0;
			break;
		case down:
			rotation=Math.PI;
			break;
		case right:
			rotation=Math.PI/2;
			break;
		case left:
			rotation=-Math.PI/2;
			break;
		}
		
		double buffer_x;
		double buffer_y;
		
		for(int i=0; i < points.size(); i++) {
			buffer_x=Math.cos(rotation)*points.get(i)[0]-Math.sin(rotation)*points.get(i)[1];
			buffer_y=Math.sin(rotation)*points.get(i)[0]+Math.cos(rotation)*points.get(i)[1];
			
			points.get(i)[0]=buffer_x;
			points.get(i)[1]=buffer_y;
		}
		
		for(int i=0; i < points.size(); i++) {
			points.get(i)[0]+=dx;
			points.get(i)[1]+=dy;
		}
		
		p = new Obstacle(points);
	}
	
	public void update_pos(double t) {
		double v1_x;
		double v1_y;
		
		double v1_t;
		
		v1_x=pos_buffer[0]-pos[0];
		v1_y=pos_buffer[1]-pos[1];
		
		switch(pointing) {
		case up:
			v1_y=0;
			break;
		case down:
			v1_y=0;
			break;
		case right:
			v1_x=0;
			break;
		case left:
			v1_x=0;
			break;
		}

		v1_t=Math.sqrt(v1_x*v1_x+v1_y*v1_y);

		if(v1_t > 1.25) {
			v1_x/=v1_t;
			v1_y/=v1_t;
		}
		else {
			v1_x=0;
			v1_y=0;
		}

		v1_x*=paddle_vel*t;
		v1_y*=paddle_vel*t;

		for(int i=0; i< p.points.size(); i++) {
			p.points.get(i)[0]+=v1_x;
			p.points.get(i)[1]+=v1_y;
		}
		
		pos[0]+=v1_x;
		pos[1]+=v1_y;
	}
	
	public void update_pos() {
		double v1_x;
		double v1_y;
		
		v1_x=pos_buffer[0]-pos[0];
		v1_y=pos_buffer[1]-pos[1];
		
		switch(pointing) {
		case up:
			v1_y=0;
			break;
		case down:
			v1_y=0;
			break;
		case right:
			v1_x=0;
			break;
		case left:
			v1_x=0;
			break;
		}

		for(int i=0; i< p.points.size(); i++) {
			p.points.get(i)[0]+=v1_x;
			p.points.get(i)[1]+=v1_y;
		}
		
		pos[0]+=v1_x;
		pos[1]+=v1_y;
	}
	
	public void setBuffer(double x, double y) {
		if(x > Game.dx+Game.game_size[0]-paddle_size_y/2) {
			x=Game.dx+Game.game_size[0]-paddle_size_y/2;
		}
		else if(x < Game.dx+paddle_size_y/2) {
			x=Game.dx+paddle_size_y/2;
		}
		
		if(y > Game.dy+Game.game_size[1]-paddle_size_y/2) {
			y = Game.dy+Game.game_size[1]-paddle_size_y/2;
		}
		else if(y < Game.dy+paddle_size_y/2) {
			y = Game.dy+paddle_size_y/2;
		}
		
		pos_buffer[0]=x;
		pos_buffer[1]=y;
	}
}
