import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DCDC_RES_CALCULATOR extends PApplet {


//gui控制库


ControlP5 cp5;

Textlabel myTextlabelA;
Textlabel myTextlabelSet;
Textlabel myTextlabelSwitch;
Textlabel myTextlabelCalaulator;

String vOut ;
String vRef ;
String setR1 ;
String setR2 ;
String outR1 ;
String outR2 ;

//声明一个图片类
PImage img;
float imgPosition = 4;
float imgSize = 2;

public void setup() {
  
   background(0);
   PFont font = loadFont("3ds-Light-24.vlw");
  img = loadImage("image/DCDC.png");
  //imageMode(CENTER);
  image(img,0,0,width/imgSize, height);  // Display at full opacity
  
  cp5 = new ControlP5(this);
 
  myTextlabelSet = cp5.addTextlabel("setlabel")
                .setText("1.Please Set Value First: ")
                .setPosition(700,40)
                .setColorValue(0xffffff00)
                .setFont(font)
                ;
  cp5.addTextfield("Vout")
   .setPosition(800,90)
   .setSize(80,30)
   .setFont(font)
   .setFocus(false)
   .setColor(color(255,0,0))
   .setText("0.0")
   .setAutoClear(false)
   ;
  cp5.addTextfield("Vref")
   .setPosition(800,160)
   .setSize(80,30)
   .setFont(font)
   .setFocus(false)
   .setColor(color(255,0,0))
   .setText("0.0")
   .setAutoClear(false)
   ;
   
   myTextlabelSwitch = cp5.addTextlabel("switchlabel")
                .setText("2. Please Set R1 OR R2 Value Second: ")
                .setPosition(700,240)
                .setColorValue(0xffffff00)
                .setFont(font)
                ;
   
 cp5.addTextfield("R1")
   .setPosition(750,280)
   .setSize(80,30)
   .setFont(font)
   .setFocus(false)
   .setColor(color(255,0,0))
   .setText("0.0")
   .setAutoClear(false)
   ;
   
  cp5.addTextfield("R2")
   .setPosition(950,280)
   .setSize(80,30)
   .setFont(font)
   .setFocus(false)
   .setColor(color(255,0,0))
   .setText("0.0")
   .setAutoClear(false)
   ;
   
   myTextlabelCalaulator = cp5.addTextlabel("calculatorlabel")
                .setText("3. Calculator R1 OR R2 Value Finally: ")
                .setPosition(700,380)
                .setColorValue(0xffffff00)
                .setFont(font)
                ;
  cp5.addTextfield("C_R1")
   .setPosition(750,460)
   .setSize(80,30)
   .setFont(font)
   .setFocus(false)
   .setColor(color(255,0,0))
   .setText("0.0")
   .setAutoClear(false)
   ;
   
  cp5.addTextfield("C_R2")
   .setPosition(950,460)
   .setSize(80,30)
   .setFont(font)
   .setFocus(false)
   .setColor(color(255,0,0))
   .setText("0.0")
   .setAutoClear(false)
   ;
   
   cp5.addBang("calculator")
     .setPosition(1090,480)
     .setSize(80,40)
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)
     ;    
   
}

public void draw() {
   
}

public void calculator() {
  double myout,myref;
  double tempR1 = 0,tempR2 = 0;
  double tempOutR1 = 0, tempOutR2 = 0;
 
  vRef = cp5.get(Textfield.class,"Vref").getText();
  vOut = cp5.get(Textfield.class,"Vout").getText();
  setR1 = cp5.get(Textfield.class,"R1").getText();
  setR2 = cp5.get(Textfield.class,"R2").getText();
  
  myout = Double.parseDouble(vOut); //将String转换为double类型
  myref = Double.parseDouble(vRef); //将String转换为double类型
  
  tempR1 = Double.parseDouble(setR1); //将String转换为double类型
  tempR2 = Double.parseDouble(setR2); //将String转换为double类型
  
  //由R1计算R2
  if(tempR1 > 0){
    tempOutR2 = tempR1 * ( myout / myref - 1 );  //计算R2的选择值
    tempOutR2 = fixDec(tempOutR2,2);             //保留小数位数
    cp5.get(Textfield.class,"C_R2").setText(Double.toString(tempOutR2));  //更新窗口文字
  }
  
  //由R2计算R1
  if(tempR2 > 0){
    tempOutR1 = tempR2 * myref /( myout - myref );
    tempOutR1 = fixDec(tempOutR1,2);
    cp5.get(Textfield.class,"C_R1").setText(Double.toString(tempOutR1));
  }
 
}




//控制小数的位数
public double fixDec(double n, int d) {
  return Double.parseDouble(String.format("%." + d + "f", n));
}
  public void settings() {  size(1200, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DCDC_RES_CALCULATOR" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
