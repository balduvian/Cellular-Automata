import java.io.IOException;
import java.util.Date;
//lolol
public class Imager {
	
	public Window window;
	static long goal=0;
	
	public Imager() throws IOException{
		window = new Window();
		long olt=0;
		goal = (long)(new Date().getTime()+window.vars[3]);
		while(true){
			window.canvas.repaint();
			if(window.run){
				if(new Date().getTime()>=goal){
					goal = (long)(new Date().getTime()+window.vars[3]);
					window.block = window.generation(window.block);
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
