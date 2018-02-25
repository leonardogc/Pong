package logic;

import java.awt.Color;

public class Particle {
	public double pos[];
	public double vel[];
	public double acc[];
	public double mass;
	public double diameter;
	public double scale=3.2;
	public Color color;
	
	
	public Particle(double posX,double posY,double velX,double velY,double mass,int color){
		this.pos = new double[2];
		this.vel = new double[2];
		this.acc = new double[2];
		
		this.pos[0]=posX;
		this.pos[1]=posY;
		
		this.vel[0]=velX;
		this.vel[1]=velY;
		
		this.acc[0]=0;
		this.acc[1]=0;
		
		this.mass=mass;
		
		this.diameter=2*Math.pow(mass/4, (double)1/3)*scale;
		
		if(this.diameter>60){
			this.diameter=60;
		}
		
		if(color==0) {
			this.color=Color.RED;
		}
		else if(color==1) {
			this.color=Color.BLUE;
		}
		else if(color==2) {	
			this.color=Color.GREEN;
		}
		else if(color==3){
			this.color=Color.MAGENTA;
		}
	}
	
	public Particle(double posX,double posY,double velX,double velY,double mass,double diameter,int color){
		this.pos = new double[2];
		this.vel = new double[2];
		this.acc = new double[2];
		
		this.pos[0]=posX;
		this.pos[1]=posY;
		
		this.vel[0]=velX;
		this.vel[1]=velY;
		
		this.acc[0]=0;
		this.acc[1]=0;
		
		this.mass=mass;
		this.diameter=diameter;
		
		if(color==0) {
			this.color=Color.RED;
		}
		else if(color==1) {
			this.color=Color.BLUE;
		}
		else if(color==2) {	
			this.color=Color.GREEN;
		}
		else if(color==3){
			this.color=Color.MAGENTA;
		}
	}
	
	
	public void updateVel_Pos_setAcc0(double t){
		vel[0]=vel[0]+acc[0]*t;
		vel[1]=vel[1]+acc[1]*t;
		
		acc[0]=0;
		acc[1]=0;
		
		pos[0]=pos[0]+vel[0]*t;
		pos[1]=pos[1]+vel[1]*t;
	}
}
