import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//WHEN GITHUB DOSN"T WORSZ
public class Window extends JFrame{
	
	Canvas canvas;
	boolean run=false;
	JButton r;
	JButton n;
	JButton c;
	int[][] block = new int[256][256];
	int size = 2;
	Color[] colors = {Color.WHITE,Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.MAGENTA};
	int[][] dom={{},{5,3},{1,4},{2,5},{3,1},{4,2}};
	int cl=1;
	//int permitted = 2;
	//int speed =15; //for the IMAGER timer
	//double variation = 0; //BETWEEN 0 AND 1
	String[] names = {"Permitted","Varitaion","Speed"};
	double[] vars = {2,0,10};
	double[] inc = {1,0.1,5};
	int offx;
	int offy;
	
	public int[][] rPS(int[][] a){
		int[][] temp = new int[a.length][a[0].length];
		for(int y=0;y<a.length;y++){
			for(int x=0;x<a[y].length;x++){
				if(Math.random()>vars[1]){ //RANDOM LOCK EDIT HERE
					for(int i=1;i<=dom.length;i++){
						if(a[y][x]==0){
							for(int u=1;u<colors.length;u++){
								if(getCNeighbors(block,y,x)[0]==8){
									break;
								}
								if(getCNeighbors(block,y,x)[u]>vars[0]){
									temp[y][x] = u;
									break;
								}
							}
						}
						else if(a[y][x]==i){
							for(int u=0;u<dom[a[y][x]].length;u++){
								if(getCNeighbors(block,y,x)[dom[a[y][x]][u]]>vars[0]){
									temp[y][x] = dom[a[y][x]][u];
								}
							}
						}
					}
				}
			}
		}
		for(int y=0;y<a.length;y++){
			for(int x=0;x<a[y].length;x++){
				if(temp[y][x]>0){
					a[y][x] = temp[y][x];
				}
			}
		}
		return a;
	}
	
	public int getBest(int[] l,int self){ //finds greatest value in a list of ints
		int n=self,best=0;
		for(int x=0;x<l.length;x++){
			if(l[x]>best && x!=self){
				n = x;
				best = l[x];
			}
		}
		return n;
	}
	
	public int[] getCNeighbors(int[][] a, int py, int px){ //gets the color that most surrounds the tile at position in a list
		int[] n = new int[colors.length];//one value for each color next to the start block (includes white)
		int nn =0;//Final return value
		for(int y=-1;y<=1;y++){
			for(int x=-1;x<=1;x++){
				try{
					n[a[py+y][px+x]]++; //finds the color of the tile that it's looking at and adds it to (list n) in it's index
				}catch(Exception e){}
			}
		}
		n[a[py][px]]--; //makes sure it removes itself from neighbors list
		/*int best=0;
		for(int x=0;x<n.length;x++){ //goes through and finds best color in list
			if(a[py][px]==0){ //So white doesn't automatically go away
				if(n[x]>best){
					nn = x;
					best = n[x];
				}
			}else{
				if(n[x]>best && x!=a[py][px]){ //makes sure it can't return itself
					nn = x;
					best = n[x];
				}
			}
		}*/
		return n;
	}
	
	public int[][] generation(int[][] a){
		int[][] temp = new int[a.length][a[1].length];
		for(int y=0;y<a.length;y++){
			for(int x=0;x<a[y].length;x++){
				if(a[y][x]==1){
					if(getNeighbors(a,y,x)>3 || getNeighbors(a,y,x)<2){
						temp[y][x]=1;
					}
				}
				else{
					if(getNeighbors(a,y,x)==3){
						temp[y][x]=2;
					}
				}
			}
		}
		for(int y=0;y<a.length;y++){
			for(int x=0;x<a[y].length;x++){
				if(temp[y][x]==1){
					a[y][x]=0;
				}
				if(temp[y][x]==2){
					a[y][x]=1;
				}
			}
		}
		return a;
	}
		
	public int getNeighbors(int[][] a, int py, int px){
		int n=0;
		for(int y=-1;y<=1;y++){
			for(int x=-1;x<=1;x++){
				try{
					if(a[py+y][px+x]==1){
						n++;
					}
				}catch(Exception e){}
			}
		}
		if(a[py][px]==1){
			return n-1;
		}else{
			return n;
		}
	}
	
	public class Canvas extends JPanel{
		public void paintComponent(Graphics g){
			//t.setText(Boolean.toString(c));
			super.paintComponent(g);
			setBackground(Color.GRAY);
			g.setColor(Color.WHITE);
			g.fillRect((getWidth()/2-(block[0].length*(size/2))), (getHeight()/2-(block.length*(size/2))), (block[0].length*size), (block.length*size));
			for(int y=0;y<block.length;y++){
				for(int x=0;x<block[y].length;x++){
					if(block[y][x]>0){
					g.setColor(colors[block[y][x]]);
					int py = (y*size+getHeight()/2-(block.length*(size/2)));
					int px = (x*size+getWidth()/2-(block[0].length*(size/2)));
					g.fillRect(px, py, size, size);
					}
				}
			}
			for(int i=0;i<colors.length;i++){ //DRAW COLOR SELECTORS
				g.setColor(colors[i]);
				g.fill3DRect((int)(-48+getWidth()/2-block[0].length*(size/2)), ((i*48)+getHeight()/2-block.length*(size/2)), 32, 32,cl==i);
			}
			
			g.setFont(new Font(Font.SANS_SERIF, 0, 20));
			for(int i=0;i<vars.length;i++){
				g.setColor(Color.WHITE);
				g.fillRect(((block[0].length*size)+16+getWidth()/2-block[0].length*(size/2)), ((i*48)+getHeight()/2-block.length*(size/2)), 32, 16);
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(((block[0].length*size)+16+getWidth()/2-block[0].length*(size/2)), ((i*48+16)+getHeight()/2-block.length*(size/2)), 32, 16);
				g.setColor(Color.BLACK);
				g.drawString("^", ((block[0].length*size)+28+getWidth()/2-block[0].length*(size/2)), ((i*48+16)+getHeight()/2-block.length*(size/2)));
				g.drawString("v", ((block[0].length*size)+28+getWidth()/2-block[0].length*(size/2)), ((i*48+16+14)+getHeight()/2-block.length*(size/2)));
				g.drawString(names[i]+": "+Double.toString(vars[i]), ((block[0].length*size)+52+getWidth()/2-block[0].length*(size/2)), ((i*48+22)+getHeight()/2-block.length*(size/2)));
			}
		}
	}
	
	public Window(){
		setTitle("Rock Paper Scissors");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(890, 640);
		canvas = new Canvas();
		canvas.addMouseListener(new MListen());
		canvas.addMouseMotionListener(new MListen2());
		r = new JButton("Start");
		n = new JButton("Next");
		c = new JButton("Clear");
		r.addActionListener(new SS());
		n.addActionListener(new SK());
		c.addActionListener(new SC());
		JPanel bc = new JPanel();
		bc.setLayout(new GridLayout());
		bc.add(r);
		bc.add(n);
		bc.add(c);
		//bc.add(t);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
	    cp.add(canvas,BorderLayout.CENTER);
	    cp.add(bc, BorderLayout.SOUTH);
	    //setResizable(false);
		setVisible(true);
	}
	
	public class SS implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(run){
				r.setText("Start");
				run=false;
				Imager.setGoal();
			}else{
				r.setText("Stop");
				run=true;
			}
		}
	}
	
	public class SK implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(run==false){
				rPS(block);
			}
		}
	}
	
	public class SC implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			run=false;
			r.setText("Start");
			for(int y=0;y<block.length;y++){
				for(int x=0;x<block[y].length;x++){
					try{
					block[y][x]=0;
					}catch(Exception e){};
				}
			}
		}
	}
	
	public void drawin(MouseEvent e,int m){ //DRAWING                   DRAWING
			try{
				int s = 3;
				int x = (int)( (e.getX())/size - (canvas.getWidth()/2)/size + (block[0].length*(size/2))/size );
				int y = (int)( (e.getY())/size - (canvas.getHeight()/2)/size + (block.length*(size/2))/size );
				for(int dy=(int)(Math.floor(s/2)*-1);dy<=(int)(Math.floor(s/2));dy++){
					for(int dx=(int)(Math.floor(s/2)*-1);dx<=(int)(Math.floor(s/2));dx++){
						block[y+dy][x+dx]=m;
					}
				}
			}catch(Exception ex){}
	}

	public class MListen2 implements MouseMotionListener{

		public void mouseDragged(MouseEvent e) {
			drawin(e,cl);
		}
		public void mouseMoved(MouseEvent e) {
		}
	}
	
	public class MListen implements MouseListener{
		public void mouseClicked(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
			Rectangle m = new Rectangle(e.getX(),e.getY(),1,1);
			for(int i=0;i<colors.length;i++){ //LOOK FOR COLOR SELECTORS
				Rectangle r = new Rectangle((-48+canvas.getWidth()/2-block[0].length*(size/2)), ((i*48)+canvas.getHeight()/2-block.length*(size/2)), 32, 32);
				if(m.intersects(r)){
					cl = i;
				}
			}
			for(int i=0;i<vars.length;i++){
				Rectangle r = new Rectangle(((block[0].length*size)+8+getWidth()/2-block[0].length*(size/2)), ((i*48-32)+getHeight()/2-block.length*(size/2)), 32, 16);
				Rectangle r2 = new Rectangle(((block[0].length*size)+8+getWidth()/2-block[0].length*(size/2)), ((i*48-16)+getHeight()/2-block.length*(size/2)), 32, 16);
				if(m.intersects(r)){
					vars[i]+=inc[i];
				}
				if(m.intersects(r2)){
					vars[i]-=inc[i];
				}
			}
			drawin(e,cl);
		}
		public void mouseReleased(MouseEvent e) {
		
		}

	}
	
}
