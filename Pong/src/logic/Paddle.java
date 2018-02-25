package logic;

public class Paddle {
	public double x;
	public double y;
	public double size_x;
	public double size_y;
	
	//x and y are the position of the geometric center 
	public Paddle(double x, double y, double size_x, double size_y) {
		this.x=x;
		this.y=y;
		this.size_x=size_x;
		this.size_y=size_y;
	}
}
