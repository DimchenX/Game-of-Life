import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PixMaps extends JFrame implements KeyListener{  

    public static final int bw = 256*2;
    public static final int bh = 256*2;
    public static final int bs = bh*bw;
    
    public static int ps = 5;
//    public static final int bW = bw*ps;
//    public static final int bH = bh*ps;
//    public static final int bS = bH*bW;
    
    public static int[] bm = new int[bs];
//    public static int[] bM = new int[bS];
    
    public static int bx = 0;
    public static int by = 0;  

    public static PixMaps win = new PixMaps();

    public static boolean repaint = true;
    
    public static void main(String[] args) {
        
        int i = 0;
        for(int y = 0; y < bh; y++)
            for(int x = 0; x < bw; x++){
                bm[i]=(x*y<<8) - 1;
                i++;
            }
        
        win.setSize(bw+20,bh+20);
        win.setDefaultCloseOperation(3);
        win.setResizable(false);
        win.setTitle("PixMap");
        win.setUndecorated(true);
        win.addKeyListener(win);
        win.setVisible(true);
        win.repaint();        
    }
    
    @Override
    public void paint(Graphics g){
        if(repaint)
            g.drawImage(creatImage(bw,bh,bm), 10, 10, null);
        //g.drawImage(creatImage(bw,bh,bm), 30, 30, 210, 210, 0, 0, bw, bh, null);
        //g.drawImage(img, 30, 30, 210, 210, bx, by, bx+60, by+60, null);
        //g.drawImage(creatImage(30*ps,30*ps,pixeling(30, 30, ps, recttorect(bw, bx, by, 30, 30, bm),-1)), 30, 30, null);
        //g.drawImage(pixelimg(30, 30, ps, recttorect(bw, bx, by, 30, 30, bm), 0x070707), 30, 30, null);
        g.drawImage(pixelimgrect(bw, bx, by, 30, 30, ps, bm, -1), 30, 30, null);
        repaint = true;      
    }
    
    public static int[] pixeling(int w, int h,int pix, int[] inm, int rc){
        int[] outm = new int[w*h*pix*pix];
        int i = 0;
        int I = 0;
        int W = w*pix;
        if(pix > 1)
        for(int l = 0; l < h; l++){            //строки
            for(int f = 0; f < (pix-1); f++){       //ps-1 строки             
                for(int x = 0; x < w; x++){            //риксели в строке
                    for(int p = 0; p < (pix-1); p++){       // ps-1 пиксель
                        outm[I] = inm[i];
                        I++;         //копируем пиксели
                    }
                    outm[I] = rc;
                    I++;           // пропускаем пиксель
                    i++;                   
                }
                i-=w;
            }
            i+=w;
            for(int a = 0; a < W; a++){
                outm[I] = rc;   
                I++; // пропускаем строку
            }           
        } 
        return outm;
    }
    
    public static BufferedImage pixelimg (int w, int h,int pix, int[] inm, int rc){
        BufferedImage img = new BufferedImage(w*pix, h*pix, 1); 
        int i = 0;
        int X = 0;
        int Y = 0;
        int W = w*pix;
        if(pix > 1)
        for(int y = 0; y < h; y++){           //строки
            for(int f = 0; f < (pix-1); f++){
            X = 0;                //ps-1 строки             
                for(int x = 0; x < w; x++){            //риксели в строке
                    for(int p = 0; p < (pix-1); p++){       // ps-1 пиксель
                        img.setRGB(X, Y, inm[i]);
                        X++;         //копируем пиксели
                    }
                    img.setRGB(X,Y,rc);
                    X++;           
                    i++;                   
                }
                i-=w;
                Y++;
            } 
            i+=w;
            for(X = 0; X < W; X++){
                img.setRGB(X, Y, rc);
            }
            Y++;  
        } 
        return img;
    }
    
        public static BufferedImage pixelimgrect (int wp, int xp, int yp, int w, int h,int pix, int[] inm, int rc){
        BufferedImage img = new BufferedImage(w*pix, h*pix, 1); 
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
                        img.setRGB(X, Y, inm[i]);         //копируем пиксели
                        X++;
                    }
                    img.setRGB(X,Y,rc);            // пустой пиксель
                    X++;           
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
        return img;
    }
    
    public static BufferedImage creatImage(int w, int h, int[] pm) {
        BufferedImage img = new BufferedImage(w, h, 1);
        int i = 0;
        for(int y1 = 0; y1 < h; y1++){
            for(int x1 = 0; x1 < w; x1++){
                img.setRGB(x1, y1, pm[i]);
                i++;
            }
        }
        img.flush();
        return img;
    }
    
    public static int[] recttorect(int W, int xp, int yp, int xs, int ys, int[] mas){
        int[] rect = new int[xs*ys];
        int i = 0;
        int I = W * yp + xp;
        W -= xs; 
        for(int y = 0; y < ys; y++){
            for(int x = 0; x < xs; x++){
                rect[i] = mas[I];
                i++;
                I++;
            }
            I+=W;
        }
        return rect;
    }
    
    public void keyPressed(KeyEvent v){
        switch(v.getKeyCode()){
            case 40: if(by < bh-30) by++;
                     repaint = false;
                     break;
            case 38: if(by > 0) by--;
                     repaint = false;   
                     break;
            case 39: if(bx < bw-30) bx++;
                     repaint = false;
                     break;
            case 37: if(bx > 0) bx--;
                     repaint = false;
                     break;
            case 107: ps++; break;
            case 109: ps--; break;
            default: System.out.println(v.getKeyCode());
        }
        repaint();
    }
    
    public void keyReleased(KeyEvent v){}
    public void keyTyped(KeyEvent v){}
}


