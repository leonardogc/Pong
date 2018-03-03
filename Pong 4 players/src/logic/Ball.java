package logic;

public class Ball {
	public double pos[];
	public double vel[];
	public double diameter;
	
	public Ball(double posX,double posY,double velX,double velY,double diameter){
		this.pos = new double[2];
		this.vel = new double[2];
		
		this.pos[0]=posX;
		this.pos[1]=posY;
		
		this.vel[0]=velX;
		this.vel[1]=velY;
		
		this.diameter=diameter;
	}
	
	
	public void update_Pos(double t){
		pos[0]=pos[0]+vel[0]*t;
		pos[1]=pos[1]+vel[1]*t;
	}
}
