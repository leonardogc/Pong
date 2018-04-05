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
public double paddle_1_vel=500;
public double paddle_2_vel=500;
public double ball_vel=800;
public double obstacle_rotations_per_second=0.1;

public double dx=(screen_size[0]-game_size[0])/2;
public double dy=(screen_size[1]-game_size[1])/2;

public boolean ai_paddle_1_enabled = true;
public boolean ai_paddle_2_enabled = false;

public static final double exit_sim_on_seconds = 0.5; 

public boolean run_sim;
public double target_y_paddle_1;
public double target_y_paddle_2;

public Game(){
	run_sim = true;
	
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

public void update(double t){
	ball.update_Pos(t);
	update_paddle_pos(t);
	update_obstacle_pos(t);
	
	update_obstacle_collisions();
	update_paddle_collisions();
	
	
	if(run_sim && (ai_paddle_1_enabled || ai_paddle_2_enabled)) {
		run_sim(t);
	}
}

public void update_obstacle_pos(double t) {
	double angle = obstacle_rotations_per_second*2*Math.PI*t;
	
	double v1_x;
	double v1_y;
	
	double v2_x;
	double v2_y;
	
	for(int i =0 ; i<obstacles.size();i++) {
		if(obstacles.get(i).points.size() > 2) {
			for(int i2=0; i2 < obstacles.get(i).points.size(); i2++) {
				v1_x=obstacles.get(i).points.get(i2)[0]-obstacles.get(i).point_of_rotation[0];
				v1_y=obstacles.get(i).points.get(i2)[1]-obstacles.get(i).point_of_rotation[1];
				
				v2_x=Math.cos(angle)*v1_x-Math.sin(angle)*v1_y;
				v2_y=Math.sin(angle)*v1_x+Math.cos(angle)*v1_y;
				
				obstacles.get(i).points.get(i2)[0]=obstacles.get(i).point_of_rotation[0]+v2_x;
				obstacles.get(i).points.get(i2)[1]=obstacles.get(i).point_of_rotation[1]+v2_y;
			}
		}
	}
	
}

public void run_sim(double t) {
	Game g = new Game();
	
	for(int i=0; i< obstacles.size(); i++) {
		Vector <double[]> points=new Vector<double[]>();
		
		for(int i2=0; i2< obstacles.get(i).points.size();i2++) {
			double[] point = new double[obstacles.get(i).points.get(i2).length];
			
			for(int i3=0; i3< obstacles.get(i).points.get(i2).length ;i3++) {
				point[i3]=obstacles.get(i).points.get(i2)[i3];
			}
			
			points.add(point);
		}
		
		g.obstacles.add(new Obstacle(points));
	}
	
	g.ball.pos=new double[] {ball.pos[0], ball.pos[1]};
	g.ball.vel=new double[] {ball.vel[0], ball.vel[1]};
	
	long startTime = System.nanoTime();
	
	
	while(!(g.ball.pos[0] > g.paddle_1.pos[0] +g.paddle_1.size[0]/2+g.ball.diameter/2 && g.ball.pos[0] < g.paddle_2.pos[0]-g.paddle_2.size[0]/2-g.ball.diameter/2)) {
		g.ball.update_Pos(t);
		g.update_obstacle_pos(t);
		g.update_obstacle_collisions();
		
		if(g.ball.pos[0] > g.dx+g.game_size[0] || g.ball.pos[0] < g.dx) {
			break;
		}
		
		if((double)(System.nanoTime()-startTime)/1000000000 > exit_sim_on_seconds) {
			break;
		}
	}

	while(g.ball.pos[0] > g.paddle_1.pos[0] +g.paddle_1.size[0]/2+g.ball.diameter/2 && g.ball.pos[0] < g.paddle_2.pos[0]-g.paddle_2.size[0]/2-g.ball.diameter/2) {
		g.ball.update_Pos(t);
		g.update_obstacle_pos(t);
		g.update_obstacle_collisions();
		
		if((double)(System.nanoTime()-startTime)/1000000000 > exit_sim_on_seconds) {
			break;
		}
	}
		
	
	if(g.ball.pos[0] <= g.paddle_1.pos[0] +g.paddle_1.size[0]/2+g.ball.diameter/2) {
		int hit = new Random().nextInt(7)-3;
		target_y_paddle_1=g.ball.pos[1]+hit*g.paddle_1.size[1]/9;
		target_y_paddle_2=g.paddle_2.pos[1];
	}
	else if(g.ball.pos[0] >= g.paddle_2.pos[0]-g.paddle_2.size[0]/2-g.ball.diameter/2){
		int hit = new Random().nextInt(7)-3;
		target_y_paddle_2=g.ball.pos[1]+hit*g.paddle_2.size[1]/9;
		target_y_paddle_1=g.paddle_1.pos[1];
	}
	
	run_sim=false;
}

public void update_paddle_pos(double t) {
	if(ai_paddle_1_enabled) {
		if(paddle_1.pos[1] < target_y_paddle_1-1) {
			paddle_1.move_down=true;
			paddle_1.move_up=false;
		}
		else if(paddle_1.pos[1] > target_y_paddle_1+1) {
			paddle_1.move_down=false;
			paddle_1.move_up=true;
		}
		else {
			paddle_1.move_down=false;
			paddle_1.move_up=false;
		}
	}
	
	if(ai_paddle_2_enabled) {
		if(paddle_2.pos[1] < target_y_paddle_2-1) {
			paddle_2.move_down=true;
			paddle_2.move_up=false;
		}
		else if(paddle_2.pos[1] > target_y_paddle_2+1) {
			paddle_2.move_down=false;
			paddle_2.move_up=true;
		}
		else {
			paddle_2.move_down=false;
			paddle_2.move_up=false;
		}
	}
	
	if(paddle_1.move_up && paddle_1.pos[1] > dy+paddle_1.size[1]/2) {
		paddle_1.pos[1]=paddle_1.pos[1]-paddle_1_vel*t;
	}
	else if(paddle_1.move_down && paddle_1.pos[1] < dy+game_size[1]-paddle_1.size[1]/2) {
		paddle_1.pos[1]=paddle_1.pos[1]+paddle_1_vel*t;
	}
	
	
	if(paddle_2.move_up && paddle_2.pos[1] > dy+paddle_2.size[1]/2) {
		paddle_2.pos[1]=paddle_2.pos[1]-paddle_2_vel*t;
	}
	else if(paddle_2.move_down && paddle_2.pos[1] < dy+game_size[1]-paddle_2.size[1]/2) {
		paddle_2.pos[1]=paddle_2.pos[1]+paddle_2_vel*t;
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
		run_sim=true;
		return;
	}
	
	if(check_line_obstacle_collision(p2, p3)) {
		run_sim=true;
		return;
	}
	if(check_line_obstacle_collision(p3, p4)) {
		run_sim=true;
		return;
	}
	if(check_line_obstacle_collision(p4, p1)) {
		run_sim=true;
		return;
	}
	
	if(check_point_obstacle_collision(p1)) {
		run_sim=true;
		return;
	}
	if(check_point_obstacle_collision(p2)) {
		run_sim=true;
		return;
	}
	if(check_point_obstacle_collision(p3)) {
		run_sim=true;
		return;
	}
	if(check_point_obstacle_collision(p4)) {
		run_sim=true;
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
		run_sim=true;
		return;
	}
	
	if(check_line_obstacle_collision(p2, p3)) {
		run_sim=true;
		return;
	}
	if(check_line_obstacle_collision(p3, p4)) {
		run_sim=true;
		return;
	}
	if(check_line_obstacle_collision(p4, p1)) {
		run_sim=true;
		return;
	}
	
	if(check_point_obstacle_collision(p1)) {
		run_sim=true;
		return;
	}
	if(check_point_obstacle_collision(p2)) {
		run_sim=true;
		return;
	}
	if(check_point_obstacle_collision(p3)) {
		run_sim=true;
		return;
	}
	if(check_point_obstacle_collision(p4)) {
		run_sim=true;
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
