import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class waveAddition extends PApplet {

ArrayList<waveGraph> graphs  = new ArrayList<waveGraph>();
ArrayList<superWave> sGraphs = new ArrayList<superWave>();
float res = 10; //Pixels per X calculation

//Controls
boolean showPoints = false;
boolean showCurves = true;
int tParam = 3;
int nParam = 0;
int nWave  = 0;

public void setup(){
    
    initSetup1();
}
public void draw(){
    background(60,60,60);
    drawGraphs();
    overlay();
}
public void keyPressed(){
    /*
    1; Toggle showing points
    2; Toggle showing the curve

    Q; last wave
    E; next wave

    A; last parameter
    D; next parameter

    W; increase value
    S; decrease value

    [; decrease resolution
    ]; increase resolution
    */
    if(key == '1'){
        showPoints = !showPoints;
    }
    if(key == '2'){
        showCurves = !showCurves;
    }
    if(key == 'q'){
        nWave--;
        if(nWave<0){
            nWave = (graphs.size()-1);
        }
    }
    if(key == 'e'){
        nWave++;
        if(nWave>graphs.size()-1){
            nWave = 0;
        }
    }
    if(key == 'a'){
        nParam--;
        if(nParam<0){
            nParam = tParam-1;
        }
    }
    if(key == 'd'){
        nParam++;
        if(nParam>tParam-1){
            nParam = 0;
        }
    }
    if(key == 'w'){
        if(nParam == 0){
            graphs.get(nWave).A ++;
        }
        if(nParam == 1){
            graphs.get(nWave).k ++;
        }
        if(nParam == 2){
            graphs.get(nWave).w ++;
        }
    }
    if(key == 's'){
        if(nParam == 0){
            graphs.get(nWave).A --;
        }
        if(nParam == 1){
            graphs.get(nWave).k --;
        }
        if(nParam == 2){
            graphs.get(nWave).w --;
        }
    }
    if(key == '['){
        res--;
    }
    if(key == ']'){
        res++;
    }
}

public void initSetup1(){
    waveGraph newWaveGraph1 = new waveGraph(new PVector(    width/4.0f, height/4.0f), new PVector(220,100,100), 10.0f, 8.0f, 5.0f, 400.0f, 150.0f, 60.0f, 12.0f);
    waveGraph newWaveGraph2 = new waveGraph(new PVector(3.0f*width/4.0f, height/4.0f), new PVector(100,220,100), 10.0f, 8.0f, 1.0f, 400.0f, 150.0f, 60.0f, 12.0f);
    graphs.add(newWaveGraph1);
    graphs.add(newWaveGraph2);

    ArrayList<waveGraph> superSet = new ArrayList<waveGraph>();
    superSet.add(newWaveGraph1);superSet.add(newWaveGraph2);
    superWave newSuperWave = new superWave(superSet, new PVector(width/2.0f, 3.0f*height/4.0f), new PVector(100,100,220), 600.0f, 200.0f, 60.0f, 40.0f);
    sGraphs.add(newSuperWave);
}

public void drawGraphs(){
    for(int i=0; i<graphs.size(); i++){
        graphs.get(i).display();
        graphs.get(i).tUpdate();
    }
    for(int i=0; i<sGraphs.size(); i++){
        sGraphs.get(i).display();
        sGraphs.get(i).tUpdate();
    }
}
public void overlay(){
    pushStyle();
    fill(255);
    textSize(15);

    text(frameRate, 30,30);
    hoveredGraphBorder();
    showControls();
    showCurrentConfig();

    popStyle();
}
public void showControls(){
    pushStyle();
    fill(255);
    textSize(15);
    text("q = Last wave     ",9.0f*width/10.0f, 30 );
    text("e = Next wave     ",9.0f*width/10.0f, 45 );
    text("a = Last parameter",9.0f*width/10.0f, 60 );
    text("d = Next parameter",9.0f*width/10.0f, 75 );
    text("w = Increase value",9.0f*width/10.0f, 90 );
    text("s = Decrease value",9.0f*width/10.0f, 105);
    text("1 = Toggle points ",9.0f*width/10.0f, 120);
    text("2 = Toggle curves ",9.0f*width/10.0f, 135);
    text("[ = Decrease res  ",9.0f*width/10.0f, 150);
    text("] = Increase res  ",9.0f*width/10.0f, 165);
    popStyle();
}
public void showCurrentConfig(){
    pushStyle();
    textSize(20);

    fill(graphs.get(nWave).col.x, graphs.get(nWave).col.y, graphs.get(nWave).col.z);
    text("Wave "+nWave,width/2.0f, 30);
    fill(0.75f*graphs.get(nWave).col.x, 0.75f*graphs.get(nWave).col.y, 0.75f*graphs.get(nWave).col.z);
    text("-q",-50+width/2.0f, 30); text("+e",150+width/2.0f, 30);

    fill(255);
    String charUnit = "" ;
    if(nParam == 0){
        charUnit = "A";}
    if(nParam == 1){
        charUnit = "k";}
    if(nParam == 2){
        charUnit = "w";}
    text("Parameter; "+charUnit,width/2.0f, 50);
    fill(0.75f*255);
    text("-a",-50+width/2.0f, 50); text("+d",150+width/2.0f, 50);

    fill(255);
    text("Resolution; "+res, width/2.0f, 70);

    popStyle();
}
public void hoveredGraphBorder(){
    pushStyle();
    rectMode(CENTER);
    noFill();
    stroke(252, 240, 3, 50);
    strokeWeight(5);

    rect(graphs.get(nWave).oPos.x, graphs.get(nWave).oPos.y, 2.0f*graphs.get(nWave).xPxMax +graphs.get(nWave).xPxMax/10.0f, 2.0f*graphs.get(nWave).yPxMax +graphs.get(nWave).yPxMax/10.0f);
    
    popStyle();
}

class waveGraph{
    PVector oPos;
    PVector col;
    float A;
    float k;
    float w;
    float yPxMax;
    float xPxMax;
    float xUnitMax;
    float yUnitMax;

    float xUnit;
    float yUnit;

    float t = 0;

    waveGraph(PVector originPos, PVector colour, float amplitude, float waveNumber, float angularFreq, float xPxMaximum, float yPxMaximum, float xUnitMaximum, float yUnitMaximum){
        //y = Asin(kx-wt)
        oPos = originPos;
        col = colour;
        A = amplitude;
        k = waveNumber;
        w = angularFreq;
        xPxMax   = xPxMaximum;
        yPxMax   = yPxMaximum;
        xUnitMax = xUnitMaximum;
        yUnitMax = yUnitMaximum;
        xUnit = xUnitMax / xPxMax;  //Units per pixel
        yUnit = yUnitMax / yPxMax;
    }

    public void display(){
        drawAxis();
        if(showPoints){
            drawPoints();}
        if(showCurves){
            drawCurve();}
    }
    public void drawAxis(){
        pushStyle();
        fill(255);
        stroke(255);
        strokeWeight(1);
        textSize(15);
        
        //X-axis
        line(oPos.x-xPxMax, oPos.y,    oPos.x+xPxMax, oPos.y);
        text(xUnitMax, oPos.x+xPxMax+10, oPos.y);
        //Y-axis
        line(oPos.x, oPos.y+yPxMax,    oPos.x, oPos.y-yPxMax);
        text(yUnitMax, oPos.x, oPos.y-yPxMax+10);

        popStyle();
    }
    public void drawPoints(){
        pushStyle();
        fill(255);
        stroke(255,120,120);
        strokeWeight(1);
        
        for(float i=-xPxMax; i<xPxMax; i+=res){
            float disp = A*(pow(yUnit,-1))*sin(k*(i*xUnit) + w*t);
            ellipse(oPos.x +i, oPos.y+disp, 10,10);
        }

        popStyle();
    }
    public void drawCurve(){
        pushStyle();
        noFill();
        stroke(col.x, col.y, col.z);
        strokeWeight(3);
        
        beginShape();
        for(float i=-xPxMax; i<xPxMax; i+=res){
            float disp = A*(pow(yUnit,-1))*sin(k*(i*xUnit) + w*t);
            curveVertex(oPos.x +i, oPos.y+disp);
        }
        endShape();

        popStyle();
    }
    public void tUpdate(){
        t+= (1.0f)/(60.0f);
    }
}

class superWave{
    ArrayList<waveGraph> waveSet;
    PVector oPos;
    PVector col;
    float yPxMax;
    float xPxMax;
    float xUnitMax;
    float yUnitMax;
    float xUnit;
    float yUnit;

    float t = 0;

    superWave(ArrayList<waveGraph> inputWaveSet, PVector originPos, PVector colour, float xPxMaximum, float yPxMaximum, float xUnitMaximum, float yUnitMaximum){
        waveSet = inputWaveSet;

        //y = Asin(kx-wt)
        oPos = originPos;
        col = colour;
        xPxMax   = xPxMaximum;
        yPxMax   = yPxMaximum;
        xUnitMax = xUnitMaximum;
        yUnitMax = yUnitMaximum;
        xUnit = xUnitMax / xPxMax;  //Units per pixel
        yUnit = yUnitMax / yPxMax;
    }

    public void display(){
        drawAxis();
        if(showPoints){
            drawPoints();}
        if(showCurves){
            drawCurve();}
    }
    public void drawAxis(){
        pushStyle();
        fill(255);
        stroke(255);
        strokeWeight(1);
        
        //X-axis
        line(oPos.x-xPxMax, oPos.y,    oPos.x+xPxMax, oPos.y);
        text(xUnitMax, oPos.x+xPxMax+10, oPos.y);
        //Y-axis
        line(oPos.x, oPos.y+yPxMax,    oPos.x, oPos.y-yPxMax);
        text(yUnitMax, oPos.x, oPos.y-yPxMax+10);

        popStyle();
    }
    public void drawPoints(){
        pushStyle();
        fill(255);
        stroke(255,120,120);
        strokeWeight(1);
        
        PVector rPos;   //Running pos total
        for(float i=-xPxMax; i<xPxMax; i+=res){
            rPos = new PVector(oPos.x +i,oPos.y);
            for(int j=0; j<waveSet.size(); j++){
                float disp = waveSet.get(j).A*(pow(waveSet.get(j).yUnit,-1))*sin(waveSet.get(j).k*(i*waveSet.get(j).xUnit) + waveSet.get(j).w*t);
                rPos.y += disp;
            }
            ellipse(rPos.x, rPos.y, 10,10);
        }

        popStyle();
    }
    public void drawCurve(){
        pushStyle();
        noFill();
        stroke(col.x, col.y, col.z);
        strokeWeight(3);
        
        beginShape();
        PVector rPos;   //Running pos total
        for(float i=-xPxMax; i<xPxMax; i+=res){
            rPos = new PVector(oPos.x +i, oPos.y);
            for(int j=0; j<waveSet.size(); j++){
                float disp = waveSet.get(j).A*(pow(waveSet.get(j).yUnit,-1))*sin(waveSet.get(j).k*(i*waveSet.get(j).xUnit) + waveSet.get(j).w*t);
                rPos.y += disp;
            }
            curveVertex(rPos.x, rPos.y);
        }
        endShape();

        popStyle();
    }
    public void tUpdate(){
        t+= (1.0f)/(60.0f);
    }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "waveAddition" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
