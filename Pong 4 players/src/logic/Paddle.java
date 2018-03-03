package logic;

import java.util.Vector;

public class Paddle {
	public enum Pointing{
		up, down, left, right
	}
	
	public Obstacle p;
	public Pointing pointing;
	public double pos[];
	public boolean move_positive;
	public boolean move_negative;
	
	
	public Paddle(double paddle_size_y,double paddle_radius, double number_of_points,double dx, double dy, Pointing pointing) {
		this.pointing=pointing;
		this.pos= new double[] {dx, dy};
		this.move_positive=false;
		this.move_negative=false;
		
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
	
	public double[] size;
	public boolean move_up;
	public boolean move_down;
	
	//x and y are the position of the geometric center 
	public Paddle(double x, double y, double size_x, double size_y) {
		pos = new double[2];
		pos[0]=x;
		pos[1]=y;
		
		size = new double[2];
		size[0]=size_x;
		size[1]=size_y;
		
		move_up=false;
		move_down=false;
	}
}
