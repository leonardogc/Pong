package logic;

import java.util.Random;
import java.util.Vector;

import logic.Paddle.Pointing;


public class Game {
	
public Ball ball;
public Vector<Obstacle> obstacles;
public Vector<Paddle> paddles;

public static final double screen_size[]=new double[]{1366,735};
public static final double game_size[] = new double[] {600, 600};
public static final double wall_length=300;
public static final double ball_size=25;
public static final double paddle_gap=20;
public static final double ball_vel=100;
public static final double obstacle_rotations_per_second=0.1;

public static final double dx=(screen_size[0]-game_size[0])/2;
public static final double dy=(screen_size[1]-game_size[1])/2;

public boolean run_sim;

public Game(){
	run_sim = true;
	
	double angle = new Random().nextInt(360);

	ball = new Ball(game_size[0]/2+dx,game_size[1]/2+dy,ball_vel*Math.cos(Math.toRadians(angle)), -ball_vel*Math.sin(Math.toRadians(angle)), ball_size);
	
	obstacles=new Vector<Obstacle>();
	
	Vector <double []> points = new Vector<double[]>();
	Obstacle o;
	
	points.add(new double[] {dx,dy});
	points.add(new double[] {dx,dy-wall_length});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx,dy});
	points.add(new double[] {dx-wall_length,dy});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx+game_size[0],dy});
	points.add(new double[] {dx+game_size[0],dy-wall_length});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx+game_size[0],dy});
	points.add(new double[] {dx+game_size[0]+wall_length,dy});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx+game_size[0],game_size[1]+dy});
	points.add(new double[] {dx+game_size[0],game_size[1]+dy+wall_length});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx+game_size[0],game_size[1]+dy});
	points.add(new double[] {dx+game_size[0]+wall_length,game_size[1]+dy});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx,game_size[1]+dy});
	points.add(new double[] {dx,game_size[1]+dy+wall_length});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	points = new Vector<double[]>();
	points.add(new double[] {dx,game_size[1]+dy});
	points.add(new double[] {dx-wall_length,game_size[1]+dy});
	
	o = new Obstacle(points);
	obstacles.add(o);
	
	paddles = new Vector<Paddle>();
	
	paddles.add(new Paddle(dx+game_size[0]/2, dy+game_size[1]+paddle_gap, Pointing.up, false));
	paddles.add(new Paddle(dx+game_size[0]/2, dy-paddle_gap, Pointing.down, false));
	paddles.add(new Paddle(dx-paddle_gap, dy+game_size[1]/2, Pointing.right, false));
	paddles.add(new Paddle(dx+game_size[0]+paddle_gap, dy+game_size[1]/2, Pointing.left, false));
	
}

public void update(double t){
	ball.update_Pos(t);
	update_paddle_pos(t);
	update_obstacle_pos(t);
	
	update_obstacle_collisions();
	update_paddle_collisions();
	
	
	if(run_sim && (paddles.get(0).ai || paddles.get(1).ai || paddles.get(2).ai || paddles.get(3).ai)) {
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
	
	
	while(!(g.ball.pos[0] > Game.dx - Game.paddle_gap +g.ball.diameter/2 &&
			g.ball.pos[0] < Game.dx + Game.game_size[0] + Game.paddle_gap -g.ball.diameter/2 &&
			g.ball.pos[1] > Game.dy - Game.paddle_gap +g.ball.diameter/2 &&
			g.ball.pos[1] < Game.dy + Game.game_size[1] + Game.paddle_gap -g.ball.diameter/2)) {
		
		g.ball.update_Pos(t);
		g.update_obstacle_pos(t);
		g.update_obstacle_collisions();
		
		if(g.ball.pos[0] > Game.dx + Game.game_size[0] + Game.paddle_gap +g.ball.diameter/2 || 
				g.ball.pos[0] < Game.dx - Game.paddle_gap -g.ball.diameter/2 ||
				g.ball.pos[1] > Game.dy + Game.game_size[1] + Game.paddle_gap +g.ball.diameter/2 || 
				g.ball.pos[1] < Game.dy - Game.paddle_gap -g.ball.diameter/2) {
			break;
		}
	}

	while (g.ball.pos[0] > Game.dx - Game.paddle_gap +g.ball.diameter/2 &&
			g.ball.pos[0] < Game.dx + Game.game_size[0] + Game.paddle_gap -g.ball.diameter/2 &&
			g.ball.pos[1] > Game.dy - Game.paddle_gap +g.ball.diameter/2 &&
			g.ball.pos[1] < Game.dy + Game.game_size[1] + Game.paddle_gap -g.ball.diameter/2) {
		g.ball.update_Pos(t);
		g.update_obstacle_pos(t);
		g.update_obstacle_collisions();
	}
	
	
	int hit = new Random().nextInt(7)-3;

	if(g.ball.pos[0] <= Game.dx - Game.paddle_gap +g.ball.diameter/2) {
		for(int i =0; i< paddles.size(); i++) {
			if(!paddles.get(i).ai && paddles.get(i).pointing == Pointing.right) {
				paddles.get(i).setBuffer(g.ball.pos[0], g.ball.pos[1]+hit*Paddle.paddle_size_y/7);
			}
		}
	}
	else if(g.ball.pos[0] >= Game.dx + Game.game_size[0] + Game.paddle_gap -g.ball.diameter/2) {
		for(int i =0; i< paddles.size(); i++) {
			if(!paddles.get(i).ai && paddles.get(i).pointing == Pointing.left) {
				paddles.get(i).setBuffer(g.ball.pos[0], g.ball.pos[1]+hit*Paddle.paddle_size_y/7);
			}
		}
	}
	else if(g.ball.pos[1] <= Game.dy - Game.paddle_gap +g.ball.diameter/2) {
		for(int i =0; i< paddles.size(); i++) {
			if(!paddles.get(i).ai && paddles.get(i).pointing == Pointing.down) {
				paddles.get(i).setBuffer(g.ball.pos[0]+hit*Paddle.paddle_size_y/7, g.ball.pos[1]);
			}
		}
	}
	else if(g.ball.pos[1] >= Game.dy + Game.game_size[1] + Game.paddle_gap - g.ball.diameter/2) {
		for(int i =0; i< paddles.size(); i++) {
			if(!paddles.get(i).ai && paddles.get(i).pointing == Pointing.up) {
				paddles.get(i).setBuffer(g.ball.pos[0]+hit*Paddle.paddle_size_y/7, g.ball.pos[1]);
			}
		}
	}

	run_sim=false;
}

public void update_paddle_pos(double t) {
	for(int i=0; i< paddles.size(); i++) {
		if(paddles.get(i).ai) {
			paddles.get(i).update_pos(t);
		}
		else {
			paddles.get(i).update_pos();
		}
	}
}

public void update_paddle_collisions() {
	for(int i=0; i < paddles.size();i++) {
		if(check_obstacle_collision(paddles.get(i).p)) {
			run_sim=true;
			break;
		}
	}
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
