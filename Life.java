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
import java.awt.image.BufferedImage;

public class Life extends JFrame implements MouseListener, KeyListener, Runnable{
    public static final int ww = 1024;
    public static final int wh = 800;
    public static int lx = 0;
    public static int ly = 0;
    public static final int lw = 1024;
    public static final int lh = 1024;
    public static final int lk = lw*lh;
    public static int ps = 5;
    public static byte[] pole = new byte[lk+8];
    public static byte[] npole = new byte[lk+8];
    public static Life win = new Life();
    public static long delay = 100L;
    public static boolean timer = true;
    public static String slname = "save.life";
    public static String[] cline;
    public static boolean mes_b = true;
    public static String mes_s = "Hello Life!!!";
    public static boolean prnt = true;
    public static boolean pixadd = false;
    public static boolean clear = false;
    public static BufferedImage img = new BufferedImage(ww, wh, 1);
    public static int ctrl = 1;
    public static final int[] col = {0xFF404040, 0x22000000, 0xFFFFFFFF, 0xEEFF3300, 0xEEFFAA00, 0xEEFFFF00};
    public static int px;
    public static int py;
    public static boolean pw = false;


    public static void main(String[] args){
        cline = args;
        if(commline()){
            if (!loadLife(slname)) randLife();
        } else randLife();
        win.setSize(ww+20,wh+70);
        win.setDefaultCloseOperation(3);
        win.setResizable(false);
        win.setTitle("Life");
        win.setVisible(true);
        win.setBackground(Color.BLACK);
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
        if(prnt) {
            pixelimgrect(lw, lx, ly, ww / ps, wh / ps, ps, pole, 0xFF080808);
            g.drawImage(img, 10, 40, null);
            
            if(mes_b){
                g.setColor(Color.BLACK);
                g.fillRect(10,wh+41, ww, 20);
                g.setColor(Color.ORANGE);
                g.drawString(mes_s, 15, wh+56);
                mes_b = false;
            }
            prnt = true;
        }
    }

    public static void pixelimgrect (int wp, int xp, int yp, int w, int h,int pix, byte[] inm, int rc){
        int i = wp*yp+xp;
        int X = 0;
        int Y = 0;
        int W = w*pix;
        if(pix > 1)
        for(int y = 0; y < h; y++){           //строки
            for(int f = 0; f < (pix-1); f++){
            X = 0;                //ps-1 строки             
                for(int x = 0; x < w; x++){            //риксели в строке
                    for(int p = 0; p < (pix-1); p++){       // ps-1 пиксель
                        img.setRGB(X++, Y, col[inm[i]+1]);         //копируем пиксели
                    }
                    img.setRGB(X++,Y,rc);            // пустой пиксель          
                    i++;                   
                }
                i-=w;
                Y++;
            } 
            i+=wp;
            for(X = 0; X < W; X++){
                img.setRGB(X,Y,rc);       // пустая строка
            }
            Y++;  
        } 
        img.flush();
    }

    public static void steping(){
        byte c;
        int i = 0;
        for(int y = 0; y < lh; y++)
        for(int x = 0; x < lw; x++){ 
            c = 0;
            if(y > 0){
                if(x > 0) if(pole[i-(lw+1)] > 0) c++;
                if(x < (lw-1)) if(pole[i-(lw-1)] > 0) c++;               
                if(pole[i-lw] > 0) c++;                        
            }
            if((i + lw) < lk){
                if(x > 0) if(pole[i+(lw-1)] > 0) c++;
                if(x < (lw-1)) if(pole[i+(lw+1)] > 0) c++;
                if(pole[i+lw] > 0) c++;                
            }
            if(x > 0) if(pole[i-1] > 0) c++;
            if(x < (lw-1)) if(pole[i+1] > 0) c++;

            if((c > 3)||(c < 2)){
                if(pole[i] < 1){
                    npole[i] = 0;
                }else{
                    npole[i] = -1;
                }
            } else {
                npole[i] = 0;
                if(c == 3 || pole[i] > 0) npole[i] = c;
                if(c == 3 && pole[i] == 0) npole[i] = 4;
            }
            i++;
        }
        byte[] tmp = pole;
        pole = npole;
        npole = tmp;
        win.repaint();
    }  

    public static void randLife(){
        for(int i = 0; i < lw*lh; i++) {
            if(Math.random()*12 > 11){
                 pole[i] = 1;
            }else if(ctrl == 0)pole[i] = 0; 
        }
        pixadd = false;
        win.repaint();
    }

    public void mousePressed(MouseEvent p){
        px = (p.getX()-12) / ps + lx;
        py = (p.getY()-42) / ps + ly;
        System.out.println("x="+ px +" y=" + py);
        pw=true;
    }
    
    public static void pixwriter(){
        if(px>=0 && px<lw && py>=0 && py<lh){
            int z=px+(py*lw);
            if(z<lk){
                if(pole[z]>0){
                    pole[z] = 0;
                } else pole[z] = 1;
            }
            win.repaint();
        }
        pw=false;
            
    }
    
    public void keyPressed(KeyEvent v){
        switch(v.getKeyCode()){
            case 'N': pixadd = true;;    //New random pixels
            break;
            case 'S': saveLife(slname);        //Save to file
            break;
            case 'L': loadLife(slname);        //Load from file
            break; 
            case 'C': clear = true;   //Clear
            break;
            case 32: if(timer){         //Pause\Start run of game
                timer = false; 
            }else{
                timer = true;
            }
            break;
            case 107: if(delay>0) delay -= 2; message("delay :" + delay); // num + - speed up
            break;
            case 109: delay += 2; message("delay :" + delay);              //num - - speed down 
            break;
            case '0': commline();
            message("save/load file name: " + slname);
            break;
            case '1':                   //Set save\load file name saveN.life
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
            case  40: ly+=ctrl; setXY(); break;
            case  38: ly-=ctrl; setXY(); break;
            case  39: lx+=ctrl; setXY(); break;
            case  37: lx-=ctrl; setXY(); break;
            case  'P': resizer(+1); break; 
            case  'O': resizer(-1); break;
            case  'I': setXY(); break;
            case  17: ctrl = 5; break;
            default: steping(); // message("Key :" + v.getKeyCode());

        }
        win.repaint();               
    }
    
    public static void clearing(){
      for(int i = 0; i < lk; i++) pole[i] = 0;
      clear = false; 
      win.repaint();
    }

    public static void resizer(int r){
        if((ps > 2 && r < 0) || (ps < 32 && r > 0)) {
            int lyc = ly + (wh/ps)/2;
            int lxc = lx + (ww/ps)/2;
            ps+=r;
            ly = lyc - (wh/ps)/2;
            lx = lxc - (ww/ps)/2;
            setXY();
        }
    } 
    
    public static void setXY(){
        if(ly < 0) ly=0;
        if(lx < 0) lx=0;
        if(ly>lh-(wh/ps)) ly=lh-(wh/ps);
        if(lx>lw-(ww/ps)) lx=lw-(ww/ps);
        message("pixel size:" + ps + " posit: " + lx + "/" + ly);
    }
    
    public static void saveLife(String name){
        byte[] buf = new byte[lk >> 3];
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
            byte[] buf = new byte[lk>>3];
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
            if(clear)clearing();
            if(pixadd)randLife();
            if(pw)pixwriter(); 
            if(timer)steping();  
        }
    }

    public void keyReleased(KeyEvent v){
        if(v.getKeyCode() == 17) ctrl = 1;
    }
    
    public void keyTyped(KeyEvent v){}
    
    public void mouseClicked(MouseEvent p) {}
  
    public void mouseEntered(MouseEvent p) {}
    
    public void mouseExited(MouseEvent p) {}

    public void mouseReleased(MouseEvent p) {}
}
