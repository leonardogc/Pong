package logic;

import java.util.Vector;

public class Node {
	public Node children[];
	public Particle leaf;
	public double size;
	public double center_of_mass[];
	public double center[];
	public double total_mass;
	public double threshold;
	public double g;
	public boolean empty;
	public Vector<double[]> squares;
	
	public Node(double center[],double size,double g,double threshold,Vector<double[]> squares) {
		this.center=center;
		this.size=size;
		this.g=g;
		this.threshold=threshold;
		this.leaf= null;
		this.children= new Node[4];
		this.total_mass=0;
		this.center_of_mass= new double[2];
		this.empty=true;
		
		this.center_of_mass[0]=0;
		this.center_of_mass[1]=0;
		
		this.squares=squares;
		
		this.squares.add(new double[] {this.center[0],this.center[1], this.size});
	}
	
	public void add(Particle p) {

		if(leaf==null && empty) {
			leaf = p;
			center_of_mass[0]=p.pos[0];
			center_of_mass[1]=p.pos[1];
			
			total_mass=leaf.mass;
			empty=false;
		}
		else if(leaf!=null && !empty) {
			createChildren();
			children[checkPos(leaf)].add(leaf);
			children[checkPos(p)].add(p);
			
			total_mass = p.mass + leaf.mass;
	
			center_of_mass[0]=(p.pos[0]*p.mass + leaf.pos[0]*leaf.mass)/(leaf.mass+p.mass);
			center_of_mass[1]=(p.pos[1]*p.mass + leaf.pos[1]*leaf.mass)/(leaf.mass+p.mass);
			
			leaf=null;
		}
		else if(leaf==null && !empty) {
			children[checkPos(p)].add(p);
			total_mass+=p.mass;
			
			center_of_mass[0]=0;
			center_of_mass[1]=0;
			
			for(int i=0; i < 4; i++) {
				center_of_mass[0]+=children[i].center_of_mass[0]*children[i].total_mass;
				center_of_mass[1]+=children[i].center_of_mass[1]*children[i].total_mass;
			}
			
			center_of_mass[0]=center_of_mass[0]/total_mass;
			center_of_mass[1]=center_of_mass[1]/total_mass;
		}
	}
	
	public int checkPos(Particle p) {
		if(p.pos[0]<=center[0] && p.pos[1] > center[1]) {
			return 0;
		}
		else if(p.pos[0]>center[0] && p.pos[1] > center[1]) {
			return 1;
		}
		else if(p.pos[0]>center[0] && p.pos[1] <= center[1]) {
			return 2;
		}
		else if(p.pos[0]<=center[0] && p.pos[1] <= center[1]) {
			return 3;
		}
		return -1;
	}

	public void calculate_particle_acc(Particle p) {
		if(!empty) {
			double dx=center_of_mass[0]-p.pos[0];
			double dy=center_of_mass[1]-p.pos[1];
			double d2=dx*dx+dy*dy;
			double d=Math.sqrt(d2);
			double versor[] = new double[2];
			double a;

			if(leaf != null) {
				if(p != leaf) {
					versor[0] = dx/d;
					versor[1] = dy/d;
					
					if(d < (p.diameter/2) + (leaf.diameter/2)){
						d=(p.diameter/2) + (leaf.diameter/2);
						d2=d*d;
					}

					a = (g*total_mass)/d2;

					p.acc[0] = p.acc[0] + versor[0]*a;
					p.acc[1] = p.acc[1] + versor[1]*a;
				}
			}
			else {
				if((size/d)<threshold){
					versor[0] = dx/d;
					versor[1] = dy/d;

					a = (g*total_mass)/d2;

					p.acc[0] = p.acc[0] + versor[0]*a;
					p.acc[1] = p.acc[1] + versor[1]*a;
				}
				else {
					for(int i = 0; i < 4; i++) {
						children[i].calculate_particle_acc(p);
					}	
				}
			}
		}
	}

	public void createChildren() {
		children[0] = new Node(new double[]{center[0]-size/4, center[1]+size/4},size/2,g,threshold, squares);
		children[1] = new Node(new double[]{center[0]+size/4, center[1]+size/4},size/2,g,threshold, squares);
		children[2] = new Node(new double[]{center[0]+size/4, center[1]-size/4},size/2,g,threshold, squares);
		children[3] = new Node(new double[]{center[0]-size/4, center[1]-size/4},size/2,g,threshold, squares);
	}
}
