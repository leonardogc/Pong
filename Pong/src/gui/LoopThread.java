package gui;

import java.awt.event.WindowEvent;

public class LoopThread extends Thread{
	
	 private boolean running;
	 private double max_fps;
	 private GraphicsAndListeners g;

	public LoopThread(GraphicsAndListeners g){
		   running=false;
	       max_fps=400;
	       this.g=g;
	}
	
	   public void setRunning(boolean running){
	       this.running=running;
	   }

	   
	    @Override
	    public void run() {
	        long startTime;
	        long frameDuration;
	        long targetTime=(long)(1000000000/max_fps);
	        long waitTime;
	        long waitTimeMill;
	        int waitTimeNano;
	        long totalTime=0;
	        int frameCounter=0;
	        double averageFps;
	        
	        int counter=0;

	        while(running){
	            startTime= System.nanoTime();

	            if(g.playing){
	            	g.game.update(1/max_fps);
	            	
	            	g.repaint();
	            	
	            	if(g.take_pictures) {
	            		if(counter % (int)(max_fps/60) == 0){

	            			g.takePicture();

	            			/*if(g.pictureNumber == 1801) {
	            				running=false;
	            			}*/
	            		}
	            		
	            		counter++;
	            	}

	            }
	            else {
	            	g.repaint();
	            }
	            
	           
	            frameDuration=System.nanoTime()-startTime;
	            waitTime=targetTime-frameDuration;
	            waitTimeMill = waitTime/1000000;
	            waitTimeNano = (int)(waitTime - waitTimeMill*1000000);

	            try{
	                if(waitTime>0){
	                    this.sleep(waitTimeMill, waitTimeNano);
	                }
	            }catch(Exception e){ e.printStackTrace();}

	            
	            totalTime=totalTime+(System.nanoTime()-startTime);
	            frameCounter++;

	            if(frameCounter==max_fps){
	                averageFps=((double)frameCounter*1000)/((double)totalTime/1000000);
	                frameCounter=0;
	                totalTime=0;
	                ///uncomment to print the average fps
	                 System.out.println("FPS: "+averageFps);
	            }

	        }
	        
	        g.graphics.frame.dispatchEvent(new WindowEvent(g.graphics.frame, WindowEvent.WINDOW_CLOSING));

	    }

}
