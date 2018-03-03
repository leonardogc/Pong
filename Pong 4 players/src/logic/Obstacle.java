package logic;

import java.util.Vector;

public class Obstacle {
	public Vector<double[]> points;
	public double[] point_of_rotation;
	
	public Obstacle(Vector<double[]> points) {
		this.points=points;
		
		double point_of_rotation_x=0;
		double point_of_rotation_y=0;
		
		for(int i=0; i < points.size(); i++) {
			point_of_rotation_x+=points.get(i)[0];
			point_of_rotation_y+=points.get(i)[1];
		}
		
		point_of_rotation_x/=points.size();
		point_of_rotation_y/=points.size();
		
		point_of_rotation = new double[] {point_of_rotation_x, point_of_rotation_y};
	}
}
