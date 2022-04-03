import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Life_x2 extends JFrame implements MouseListener, KeyListener, Runnable{
    public static int w = 512;
    public static int h = 512;
    public static int k = w*h;
    public static byte[] pole = new byte[k];
    public static byte[] npole = new byte[k];
    public static Life_x2 win = new Life_x2();
    public static long delay = 100L;
    public static boolean timer = true;
    public static String slname = "save.life";
    public static String[] cline;
    public static boolean mes_b = true;
    public static String mes_s = "Hello Life!!!";

    public static void main(String[] args){
        cline = args;
        if(commline()){
            if (!loadLife(slname)) randLife(false);
        } else randLife(false);
        win.setUndecorated(true);
        win.setSize(w*8+20,h*8+70);
        win.setDefaultCloseOperation(3);
        win.setResizable(false);
        win.setTitle("Life");
        win.setBackground(new Color(0x101010));
        win.setVisible(true);
        win.addMouseListener(win);
        win.addKeyListener(win);        
        runing();   
    }

    public static boolean commline(){
        if(cline.length>0){
            slname = cline[0];
            return true;
        }
        slname = "save.life"; 
        return false;           
    }

    public static void message(String mes){
        mes_s = mes;
        mes_b = true;
        System.out.println(mes);
    }

    @Override
    public void paint(Graphics g){
        for(int i=0; i < k; i++){
            int x = ((i & (w-1))<<3)+10;
            int y = ((int)(i / w)<<3)+40;
            switch(pole[i]){ 
                case 0: g.setColor(Color.BLACK); break;
                case 1: g.setColor(Color.WHITE); break;
                case 2: g.setColor(Color.RED); break;
                case 3: g.setColor(Color.ORANGE); break;
                case 4: g.setColor(Color.YELLOW); break;
                case -1: g.setColor(Color.DARK_GRAY); 
            }
            g.fillRect(x,y,7,7);                   
        }
        if(mes_b){
            g.setColor(Color.BLACK);
            g.fillRect(10,h*8+41, w*8, 20);
            g.setColor(Color.ORANGE);
            g.drawString(mes_s, 15, h*8+56);
            mes_b = false;
        }
    }

    public static void steping(){
        byte c;
        int z;
        for(int i = 0; i < k; i++){
            npole[i] = 0;
            c = 0;
            z = i & (w-1);
            if((i - w) >= 0){
                if(z > 0) if(pole[i-(w+1)] > 0) c++;
                if(z < (w-1)) if(pole[i-(w-1)] > 0) c++;               
                if(pole[i-w] > 0) c++;                        
            }
            if((i + w) < k){
                if(z > 0) if(pole[i+(w-1)] > 0) c++;
                if(z < (w-1)) if(pole[i+(w+1)] > 0) c++;
                if(pole[i+w] > 0) c++;                
            }
            if(z > 0) if(pole[i-1] > 0) c++;
            if(z < (w-1)) if(pole[i+1] > 0) c++;

            if((c > 3)||(c < 2)){
                if(pole[i] < 1){
                    npole[i] = 0;
                }else{
                    npole[i] = -1;
                }
            } else {
                if(c == 3 || pole[i] > 0) npole[i] = c;
                if(c == 3 && pole[i] == 0) npole[i] = 4;
                 
            }
        }
        byte[] tmp = pole;
        pole = npole;
        npole = tmp;
        win.repaint();
    }  

    public static void randLife(boolean rev){
        for(int i = 0; i < w*h; i++) {
            if(Math.random()*12 > 11){
                 pole[i] = 1;
            }else if(rev)pole[i] = 0; 
        }
    }

    public void mousePressed(MouseEvent p){
        int x = (p.getX()-10) >> 3;
        int y = (p.getY()-40) >> 3;
        System.out.println("x="+ x +" y=" + y);
        if(x>=0 && x<w && y>=0 && y<h){
            int z=x+(y*w);
            if(z<k){
                if(pole[z]>0){
                    pole[z] = 0;
                } else pole[z] = 1;
            }
            win.repaint();
        }
    }
    
    public void keyPressed(KeyEvent v){
        switch(v.getKeyCode()){
            case 'A': randLife(false);   //Add random pixels
            break;
            case 'N': randLife(true);    //New random pixels
            break;
            case 'S': saveLife(slname);        //Save to file
            break;
            case 'L': loadLife(slname);
            break; 
            case 'C': for(int i=0; i<k; i++)pole[i]=0;   //Clear
            break;
            case 32: if(timer){         //Pause\Start run of game
                timer = false; 
            }else{
                timer = true;
            }
            break;
            case 107: if(delay>0) delay -= 5;  //Up - speed up
            break;
            case 109: delay += 5;               //Down - speed down 
            break;
            case '0': commline();
            message("save/load file name: " + slname);
            break;
            case '1':                   //Set save\load file name
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': slname = String.format("save%c.life", (char)v.getKeyCode());
            message("save/load file name: " + slname);
            break;
            case 38: win.setLocation(win.getX(), win.getY()+10); break;
            case 40: win.setLocation(win.getX(), win.getY()-10); break;
            case 39: win.setLocation(win.getX()-10, win.getY()); break;
            case 37: win.setLocation(win.getX()+10, win.getY()); break;
            default: steping(); message("Kode: " + v.getKeyCode());
        }
        win.repaint();               
    }
    
    public static void saveLife(String name){
        byte[] buf = new byte[k >> 3];
        for(int i=0; i < buf.length; i++){
            buf[i] = 0;
            for(int l=0; l<8; l++){
                buf[i]<<=1;
                if(pole[(i<<3)+l]>0) buf[i]++;
            }
        }
        try{
            FileOutputStream f = new FileOutputStream(name, false);
            f.write(buf);
            f.close();
            message("Save to file: " + name);
        }catch(IOException e){message("save error");}    
    }

    public static boolean loadLife(String name){
        try{
            FileInputStream f = new FileInputStream(name);
            byte[] buf = new byte[k>>3];
            if(f.read(buf, 0, buf.length)>0){
                timer = false;
                message("Loading from: " + name);
                for(int i=0; i < buf.length; i++){
                    for(int l=7; l>=0; l--){
                        if((buf[i] & 1)>0){
                            pole[(i<<3)+l] = 1;
                        } else if(pole[(i<<3)+l] > 0) pole[(i<<3)+l] = -1;   
                        buf[i]>>=1;
                    }
                }
            } else {
                f.close();
                return false;
            } 
            f.close();
        }catch(IOException e){message("File not loading."); return false;}
        return true; 
    }

    public static void runing(){
            new Thread(win).start();
    }

    public void run(){
        while(true){
            try{
                Thread.sleep(delay);
            }catch(Exception e){}
            if(timer)steping();
        }
    }

    public void keyReleased(KeyEvent v){}
    
    public void keyTyped(KeyEvent v){}
    
    public void mouseClicked(MouseEvent p) {}
  
    public void mouseEntered(MouseEvent p) {}
    
    public void mouseExited(MouseEvent p) {}

    public void mouseReleased(MouseEvent p) {}
}