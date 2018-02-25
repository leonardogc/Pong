package logic;

import java.util.Vector;


public class Game {
	
public Ball ball;
public Vector<Obstacle> obstacles;
public Paddle p1;
public Paddle p2;

public int screen_size[]=new int[]{1366,735};
public int game_size[] = new int[] {1000, 500};
public int dx=(screen_size[0]-game_size[0])/2;
public int dy=(screen_size[1]-game_size[1])/2;

public Game(){
	obstacles=new Vector<Obstacle>();
	ball =new Ball(game_size[0]/2+dx,game_size[1]/2+dy,-800,0,20);
}

public void update(double t){
	ball.update_Pos(t);
	update_obstacle_collisions();
}

public void update_obstacle_collisions() {
	for(int i=0; i < obstacles.size();i++) {
		if(check_obstacle_collision(obstacles.get(i))) {
			break;
		}
	}
}

public boolean check_obstacle_collision(Obstacle o) {
	//sides
	for(int i = 0; i < o.points.size(); i++) {
		if(i+1 == o.points.size()) {
			if(check_line_obstacle_collision(o.points.get(i),o.points.get(0))) {
				return true;
			}
		}
		else {
			if(check_line_obstacle_collision(o.points.get(i),o.points.get(i+1))) {
				return true;
			}
		}
	}
	
	//points
	
	for(int i = 0; i < o.points.size(); i++) {
		if(check_point_obstacle_collision(o.points.get(i))) {
			return true;
		}
	}
	
	return false;
}


public boolean check_line_obstacle_collision(double[] p1,double[] p2) {
	double vector_x=0;
	double vector_y=0;
	
	double vector_t=0;
	
	double eVel=0;
	
	double x=0;
	double y=0;
	double k=0;
	
	double distance;
	
	vector_x=p2[0]-p1[0];
	vector_y=p2[1]-p1[1];

	vector_t=Math.sqrt(Math.pow(vector_x,2) + Math.pow(vector_y,2));

	vector_x/=vector_t;
	vector_y/=vector_t;
	
	k=vector_x*(ball.pos[0]-p1[0])+vector_y*(ball.pos[1]-p1[1]);

	x=p1[0]+k*vector_x;
	y=p1[1]+k*vector_y;

	if(Math.sqrt(Math.pow(ball.pos[0]-x,2) + Math.pow(ball.pos[1]-y,2)) <= ball.diameter/2) {
		distance=Math.sqrt(Math.pow(p2[0]-p1[0],2) + Math.pow(p2[1]-p1[1],2));
		if(Math.sqrt(Math.pow(p2[0]-x,2) + Math.pow(p2[1]-y,2)) <= distance && Math.sqrt(Math.pow(p1[0]-x,2) + Math.pow(p1[1]-y,2)) <= distance) {
			vector_x= x-ball.pos[0];
			vector_y= y-ball.pos[1];
			
			vector_t=Math.sqrt(Math.pow(vector_x,2) + Math.pow(vector_y,2));

			vector_x/=vector_t;
			vector_y/=vector_t;

			eVel=ball.vel[0]*vector_x+ball.vel[1]*vector_y;

			if(eVel > 0) {
				ball.vel[0]+=-2*eVel*vector_x;
				ball.vel[1]+=-2*eVel*vector_y;
				return true;
			}
		}
	}

	return false;
}


public boolean check_point_obstacle_collision(double[] p1) {
	double ex=0;
	double ey=0;
	double et=0;
	
	double eVel=0;
	
	if(Math.sqrt(Math.pow(p1[0]-ball.pos[0],2) + Math.pow(p1[1]-ball.pos[1],2)) <= ball.diameter/2) {
		ex=p1[0]-ball.pos[0];
		ey=p1[1]-ball.pos[1];

		et=Math.sqrt(Math.pow(ex,2) + Math.pow(ey,2));

		ex/=et;
		ey/=et;

		eVel=ball.vel[0]*ex+ball.vel[1]*ey;

		if(eVel > 0) {
			ball.vel[0]+=-2*eVel*ex;
			ball.vel[1]+=-2*eVel*ey;
			return true;
		}
	}
	
	return false;
}

}
