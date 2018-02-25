package logic;

import java.util.Random;
import java.util.Vector;


public class Game {
	
public Ball ball;
public Vector<Obstacle> obstacles;
public Paddle paddle_1;
public Paddle paddle_2;

public double screen_size[]=new double[]{1366,735};
public double game_size[] = new double[] {1200, 600};
public double ball_size=25;
public double paddle_gap=5;
public double paddle_size_x=15;
public double paddle_size_y=150;
public double paddle_vel=500;
public double ball_vel=800;

public double dx=(screen_size[0]-game_size[0])/2;
public double dy=(screen_size[1]-game_size[1])/2;

public Game(){
	
	double angle = new Random().nextInt(121)-60;
	double side = new Random().nextInt(2);

	if(side==0) {
		ball = new Ball(game_size[0]/2+dx,game_size[1]/2+dy,ball_vel*Math.cos(Math.toRadians(angle)), -ball_vel*Math.sin(Math.toRadians(angle)), ball_size);
	}
	else {
		ball = new Ball(game_size[0]/2+dx,game_size[1]/2+dy,-ball_vel*Math.cos(Math.toRadians(angle)), -ball_vel*Math.sin(Math.toRadians(angle)), ball_size);
	}
	
	obstacles=new Vector<Obstacle>();
	
	Vector <double []> points = new Vector<double[]>();
	points.add(new double[] {dx,dy});
	points.add(new double[] {game_size[0]+dx,dy});
	
	Obstacle o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx,game_size[1]+dy});
	points.add(new double[] {game_size[0]+dx,game_size[1]+dy});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	paddle_1=new Paddle(paddle_size_x/2+paddle_gap+dx,game_size[1]/2+dy,paddle_size_x,paddle_size_y);
	paddle_2=new Paddle(game_size[0]-paddle_size_x/2-paddle_gap+dx,game_size[1]/2+dy,paddle_size_x,paddle_size_y);
}

public Game(Vector<Obstacle> obstacles){
	
	double angle = new Random().nextInt(121)-60;
	double side = new Random().nextInt(2);

	if(side==0) {
		ball = new Ball(game_size[0]/2+dx,game_size[1]/2+dy,ball_vel*Math.cos(Math.toRadians(angle)), -ball_vel*Math.sin(Math.toRadians(angle)), ball_size);
	}
	else {
		ball = new Ball(game_size[0]/2+dx,game_size[1]/2+dy,-ball_vel*Math.cos(Math.toRadians(angle)), -ball_vel*Math.sin(Math.toRadians(angle)), ball_size);
	}
	
	this.obstacles=obstacles;
	
	paddle_1=new Paddle(paddle_size_x/2+paddle_gap+dx,game_size[1]/2+dy,paddle_size_x,paddle_size_y);
	paddle_2=new Paddle(game_size[0]-paddle_size_x/2-paddle_gap+dx,game_size[1]/2+dy,paddle_size_x,paddle_size_y);
}

public void update(double t){
	ball.update_Pos(t);
	update_paddle_pos(t);
	
	update_obstacle_collisions();
	update_paddle_collisions();
}

public void update_paddle_pos(double t) {
	if(paddle_1.move_up && paddle_1.pos[1] > dy+paddle_1.size[1]/2) {
		paddle_1.pos[1]=paddle_1.pos[1]-paddle_vel*t;
	}
	else if(paddle_1.move_down && paddle_1.pos[1] < dy+game_size[1]-paddle_1.size[1]/2) {
		paddle_1.pos[1]=paddle_1.pos[1]+paddle_vel*t;
	}
	
	if(paddle_2.move_up && paddle_2.pos[1] > dy+paddle_2.size[1]/2) {
		paddle_2.pos[1]=paddle_2.pos[1]-paddle_vel*t;
	}
	else if(paddle_2.move_down && paddle_2.pos[1] < dy+game_size[1]-paddle_2.size[1]/2) {
		paddle_2.pos[1]=paddle_2.pos[1]+paddle_vel*t;
	}
}

public void update_paddle_collisions() {
	double[] p1;
	double[] p2;
	double[] p3;
	double[] p4;
	int result;
	
	//paddle 1
	p1 = new double[] {paddle_1.pos[0]+paddle_1.size[0]/2, paddle_1.pos[1]-paddle_1.size[1]/2};
	p2 = new double[] {paddle_1.pos[0]+paddle_1.size[0]/2, paddle_1.pos[1]+paddle_1.size[1]/2};
	p3 = new double[] {paddle_1.pos[0]-paddle_1.size[0]/2, paddle_1.pos[1]+paddle_1.size[1]/2};
	p4 = new double[] {paddle_1.pos[0]-paddle_1.size[0]/2, paddle_1.pos[1]-paddle_1.size[1]/2};

	result = check_line_paddle_collision(p1, p2);

	if(result != -1) {
		double vel = Math.sqrt(ball.vel[0]*ball.vel[0]+ball.vel[1]*ball.vel[1]);
		double angle = 0; 
		
		switch(result) {
		case -4:
			angle=60;
			break;
		case -3:
			angle=45;
			break;
		case -2:
			angle=30;
			break;
		case -1:
			angle=15;
			break;
		case 0:
			angle=0;
			break;
		case 1:
			angle=-15;
			break;
		case 2:
			angle=-30;
			break;
		case 3:
			angle=-45;
			break;
		case 4:
			angle=-60;
			break;
		}
		
		ball.vel[0]=vel*Math.cos(Math.toRadians(angle));
		ball.vel[1]=-vel*Math.sin(Math.toRadians(angle));
		return;
	}
	
	if(check_line_obstacle_collision(p2, p3)) {
		return;
	}
	if(check_line_obstacle_collision(p3, p4)) {
		return;
	}
	if(check_line_obstacle_collision(p4, p1)) {
		return;
	}
	
	if(check_point_obstacle_collision(p1)) {
		return;
	}
	if(check_point_obstacle_collision(p2)) {
		return;
	}
	if(check_point_obstacle_collision(p3)) {
		return;
	}
	if(check_point_obstacle_collision(p4)) {
		return;
	}

	//paddle 2
	p1 = new double[] {paddle_2.pos[0]-paddle_2.size[0]/2, paddle_2.pos[1]-paddle_2.size[1]/2};
	p2 = new double[] {paddle_2.pos[0]-paddle_2.size[0]/2, paddle_2.pos[1]+paddle_2.size[1]/2};
	p3 = new double[] {paddle_2.pos[0]+paddle_2.size[0]/2, paddle_2.pos[1]+paddle_2.size[1]/2};
	p4 = new double[] {paddle_2.pos[0]+paddle_2.size[0]/2, paddle_2.pos[1]-paddle_2.size[1]/2};
	
	result = check_line_paddle_collision(p1, p2);

	if(result != -1) {
		double vel = Math.sqrt(ball.vel[0]*ball.vel[0]+ball.vel[1]*ball.vel[1]);
		double angle = 0; 
		
		switch(result) {
		case -4:
			angle=60;
			break;
		case -3:
			angle=45;
			break;
		case -2:
			angle=30;
			break;
		case -1:
			angle=15;
			break;
		case 0:
			angle=0;
			break;
		case 1:
			angle=-15;
			break;
		case 2:
			angle=-30;
			break;
		case 3:
			angle=-45;
			break;
		case 4:
			angle=-60;
			break;
		}
		
		ball.vel[0]=-vel*Math.cos(Math.toRadians(angle));
		ball.vel[1]=-vel*Math.sin(Math.toRadians(angle));
		return;
	}
	
	if(check_line_obstacle_collision(p2, p3)) {
		return;
	}
	if(check_line_obstacle_collision(p3, p4)) {
		return;
	}
	if(check_line_obstacle_collision(p4, p1)) {
		return;
	}
	
	if(check_point_obstacle_collision(p1)) {
		return;
	}
	if(check_point_obstacle_collision(p2)) {
		return;
	}
	if(check_point_obstacle_collision(p3)) {
		return;
	}
	if(check_point_obstacle_collision(p4)) {
		return;
	}
}

//p1 -> top   p2 -> bottom
public int check_line_paddle_collision(double[] p1,double[] p2) { 
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

				int result=(int)((ball.pos[1]-p1[1])/(paddle_size_y/9));

				if(result == 9) {
					result=8;
				}
				
				result-=4;

				return result;
			}
		}
	}

	return -1;
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
