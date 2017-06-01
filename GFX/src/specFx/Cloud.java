package specFx;

import java.applet.Applet;


 import java.awt.*;
 import java.applet.*;
 import java.lang.String;
 import java.util.Random;
 import java.util.Date;
 
 public class Cloud extends Applet implements Runnable {	
    Thread    runner;	
    int       WIDTH,HEIGHT,MaxLevel;  
    Graphics  g;   
    Random    nrand;    	
         
        public void init() {	
         	 Date d=new Date();	
         	 nrand=new Random(d.getTime());	
         	this.setSize(new Dimension(500,500));
          	 WIDTH=this.size().width;	
         	 HEIGHT=this.size().height;	
         	 g=this.getGraphics();	
         	 this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
         	 this.setVisible(true);
         	 g.setColor(Color.black);    
         	 g.fillRect(0,0,WIDTH,HEIGHT);	 
         	 //-------------------------------------//	 
         	String s;		
         	s=getParameter("MAXLEVEL");
               if (s==null)MaxLevel=8;	
              	else MaxLevel = Integer.parseInt(s); 
              	
      	}		
      	public void start()	{	
      		if (runner == null){		
      	 		runner= new Thread(this);	
      			runner.start();		
      		}	
      	}	
      	
       public void stop(){	
        	if (runner!=null){	
        		runner.stop();		
        		runner=null;	
        	}	
       }	
        
       public void run() //----------------------------------------//
      	{ 	
           init(); //		
      	    while (true) {		
      	    	try { Thread.sleep(10); } 
      	    	catch (InterruptedException e){};
      	    	if ( isVisible()  ) {	
      	    	    DomyRun();	  	  
      	    	    	//paint(g);	
      	    	   }	
      	     }		  
      	 } 		
      	 
      	 public void  DomyRun()//----------------------------------//	
      	 {	
      	  double   XX[],YY[],Val[];	
      	  int      i;	    
      	  	
      	  	  XX =new double[4];	
      	  	  YY =new double[4];
      	  	  Val=new double[4];	    	
      	  	  
      	  	  g.setColor(Color.black);     
      	  	  g.fillRect(0,0,WIDTH,HEIGHT);	   
      	  	       
      	          for(i=0;i<4;i++){   Val[i]=randomise(nrand,100);  Val[i] *=0.01; }   
      	    
      	          XX[0]=0.0;     YY[0]=0.0;    
      	          XX[1]=256.0; YY[1]=0.0;    
      	          XX[2]=0.0;     YY[2]=256.0;   
      	          XX[3]=256.0; YY[3]=256.0;     
      	          
      	          doIteration(XX,YY,0,Val);   
      	         
      }//-----------------------------------------------------//	
      
  private void doIteration(double XX[],double YY[], int level,double Val[]) 
   {  
     int    i,k; 
     double  cX,cY,rr,valx[],valy[],valc,val[],xx[],yy[];    
     
       valx=new double[2];    
       valy=new double[2];    
       val =new double[4];    
       xx  =new double[4];  
       yy  =new double[4];      
       
      level ++;     
          
      cX=(XX[0]+XX[1]+XX[2]+XX[3])*0.25;  
      cY=(YY[0]+YY[1]+YY[2]+YY[3])*0.25;   
      rr=XX[1]-XX[0];  
      rr /=256.0;        
      valc=randomise(nrand,200)-100; 
      valc *=0.01;   valc *=rr;     
      valx[0] =(Val[0]+Val[1])*0.5;   
      valx[1] =(Val[2]+Val[3])*0.5;    
      valy[0] =(Val[0]+Val[2])*0.5;   
      valy[1] =(Val[1]+Val[3])*0.5;   
      valc +=(Val[0]+Val[1]+Val[2]+Val[3])*0.25;   
     
       //---------------------------------------//  
       
      for(i=0;i<4;i++){    
       
          switch(i){      
                   case 0:  xx[0]=xx[2]=XX[0];    xx[1]=xx[3]=cX;          
                            yy[0]=yy[1]=YY[0];    yy[2]=yy[3]=cY;				                
                            val[0]=Val[0];			
                            val[1]=valx[0];	
                            val[2]=valy[0];
                            val[3]=valc;             
                            break;       
                            
                   case 1:  xx[1]=xx[3]=XX[1];    xx[0]=xx[2]=cX;     
                            yy[0]=yy[1]=YY[1];    yy[2]=yy[3]=cY;					            
                            val[0]=valx[0];	
                            val[1]=Val[1];			
                            val[2]=valc;	
                            val[3]=valy[1];         
                            break;                   
                            
                   case 2:  xx[0]=xx[2]=XX[0];   xx[1]=xx[3]=cX;     
                            yy[2]=yy[3]=YY[2];   yy[0]=yy[1]=cY;				        
                            val[0]=valy[0];	
                            val[1]=valc;			
                            val[2]=Val[2];	
                            val[3]=valx[1];	
                            break;           
                            
                   case 3:  xx[1]=xx[3]=XX[3];    xx[0]=xx[2]=cX;     
                            yy[2]=yy[3]=YY[3];    yy[0]=yy[1]=cY;					         
                            val[0]=valc;			
                            val[1]=valy[1];	
                            val[2]=valx[1];		
                            val[3]=Val[3];      
                             break;    
                }                     
                
           if(level>=MaxLevel){    
                 Polygon    poly;  
                 int        n0, sx[],sy[];   		
              	  double     dd;         
              	  
              	    sx=new int[4];
              	    sy=new int[4];  
              	    
              	    dd=(val[0]+val[1]+val[2]+val[3])*0.25;	
              	    dd *=256.0;     
              	    n0=(int)dd;	   n0 -=128;	
              	    if(n0<0) n0=0;  if(n0>255) n0=255;     
              	    
              	    sx[0]=(int)xx[0];  sx[1]=(int)xx[1];  sx[2]=(int)xx[3];  sx[3]=(int)xx[2];  
                   sy[0]=(int)yy[0];  sy[1]=(int)yy[1];  sy[2]=(int)yy[3];  sy[3]=(int)yy[2];                  	    
              	    poly=new Polygon(sx,sy,4);     
              	    g.setColor(new Color(n0,n0,n0));    
              	    g.fillPolygon(poly);     
              	         
           }else   doIteration(xx,yy,level,val);  
            
            
       }     
                            
    }  	
                                  
     public int randomise(Random nrand,int range)	 {
     	    if(range==0) return 0;		
     	     return( java.lang.Math.abs(nrand.nextInt()) % range);	
      } 
      
  }
