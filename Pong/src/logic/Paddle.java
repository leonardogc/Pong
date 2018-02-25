package logic;

public class Paddle {
	public double[] pos;
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
