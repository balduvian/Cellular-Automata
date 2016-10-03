import java.io.IOException;
import java.util.Date;

public class Imager {
	
	public Window window;
	static long goal=0;
	
	public Imager() throws IOException{
		window = new Window();
		long olt=0;
		while(true){
			window.canvas.repaint();
			if(window.run){
				Date t = new Date();
				if(window.speed ==0 || (t.getTime()-goal)%window.speed == 0 && olt != t.getTime()){
					olt = t.getTime();
					window.block = window.rPS(window.block);
				
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Imager();
	}

	public static void setGoal() {
		Date t = new Date();
		goal = t.getTime();
		
	}

}
