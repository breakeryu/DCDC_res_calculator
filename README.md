# DCDC_res_calculator
 
目录

一、实现效果
二、实现原理
三、实现过程
四、参考资源
五、开源地址
 


## 一、实现效果
在DCDC可调芯片中，我们经常会用到分压电阻网络来调整输出电压，今天要做的就是可以计算电阻值的一个辅助工具。下面是最后的实现效果：

![image](https://user-images.githubusercontent.com/35825642/138628207-f91bced8-f650-42c7-8367-a5b1ffa69609.png)

## 二、实现原理
原理上是非常简单的一个公式：

![image](https://user-images.githubusercontent.com/35825642/138628219-31dc5c41-b420-4a2f-833e-40913a1bb7c2.png)


这是一个很简单的一个分压比例关系。在后面的代码中实现其实只有这一个数学公式起着关键的计算作用。


## 三、实现过程
实现的过程是代码构建的几个步骤，同时也是我自己解决问题的过程，供大家参考，如果有问题的地方可以评论指出。最终的工程链接放在文章的最后，可以

 1.环境、库

在开发环境上面，我测试了Processing 4.0 和3.2版本。使用前确保你的电脑上面安装了JAVA环境，而且版本不能太低。

关于库，这里使用了controlP5的GUI库。

 2.GUI界面设计
```java
显示图片
//声明一个图片类
PImage img;
//控制图片大小
float imgSize = 2;

void setup() {
  size(1200, 900);
  background(0);
  ···
  img = loadImage("image/DCDC.png");	//导入图片
  
  image(img,0,0,width/imgSize, height);  // 控制大小
  ···
  }
  ```
  
这里需要注意的是，在本工程文件夹中，创建了image的文件夹。在导出exe程序的时候，要将这个文件夹也复制到exe所在的位置，否则程序不能运行。

 cp5——lable
标签中可以显示文字，一般可以使用这个来展示静态的文字。
```java
//gui控制库
import controlP5.*;
//定义类
ControlP5 cp5;
//标签定义
Textlabel myTextlabelSet;

void setup() {
  PFont font = loadFont("3ds-Light-24.vlw");	//导入生成的字体
  myTextlabelSet = cp5.addTextlabel("setlabel")	//标签的名字
  .setText("1.Please Set Value First: ")	//标签显示文字
  .setPosition(700,40)	//标签显示位置定义
  .setColorValue(0xffffff00)	//颜色定义
  .setFont(font)	//设置
  ;
 }
 ```
 cp5——textfield

textfield是文本输入框，可以输入一段文字，保存类型是string。在本工程中，只需要将文本框中输入的数字转换成小数，方便后续的计算。这里展示一个demo，定义一个文本输入框，并获取框中的小数，转换到本地变量中。
```java
//gui控制库
import controlP5.*;

ControlP5 cp5;
String vOut ;
void setup() {
  cp5 = new ControlP5(this);//新的类
  
  //文本输入框定义
  cp5.addTextfield("Vout")	//添加名字叫Vout的输入框
  .setPosition(800,90)		//位置
  .setSize(80,30)			//大小
  .setFont(font)			//字体
  .setFocus(false)			//设置是否活动
  .setColor(color(255,0,0))	//颜色
  .setText("0.0")			//显示的文本
  .setAutoClear(false)		//设置是否自动清除
  ;
  
}

void draw(){
  //获取文本框中的值
  vOut = cp5.get(Textfield.class,"Vout").getText();
  double myout = Double.parseDouble(vOut); //将String转换为double类型
  println("myout = " + myout);
}
```
cp5——bang
 bang就是一个按键，当按键按下，可以触发一次事件。

```java
//gui控制库
import controlP5.*;

ControlP5 cp5;
void setup() {
   cp5.addBang("calculator")	//添加名字，当然要和后面的回调函数名字一致
     .setPosition(1090,480)		//位置
     .setSize(80,40)			//大小
     .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)	//对齐方法
     ;    
}

public void calculator() {
	····;
}
```
 获取文本框中的string，并转换为double
 ```java
setR1 = cp5.get(Textfield.class,"R1").getText();

tempR1 = Double.parseDouble(setR1); //将String转换为double类型
```
将double转换为文本框string
```java
Double.toString(tempOutR2)
```
控制输出的小数位数
使用该函数可以控制转换小数的位数，防止文本框输出小数位数过长的问题。
```java
//控制小数的位数
double fixDec(double n, int d) {
  return Double.parseDouble(String.format("%." + d + "f", n));
}
```
回调函数输出代码，关键控制代码
```java
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
```
程序导出
程序导出，目前发现4.0的导出需要选中包含EMBED JAVA选项，才可以运行。这样导出的工程非常大。

![image](https://user-images.githubusercontent.com/35825642/138628244-e4b2eaf9-9e37-4da5-a1b5-87401e02fe92.png)


在processing3中，则不需要勾选，导出后立即可以打开。工程导出后非常小。


![image](https://user-images.githubusercontent.com/35825642/138628251-26ac2de2-7528-441b-8ff3-93f853be7224.png)


## 四、参考资源
http://localhost:8053/reference/image_.html

http://www.sojamo.de/libraries/controlP5/reference/

https://discourse.processing.org/t/convert-string-to-integer/2430/6

https://forum.processing.org/one/topic/converting-strings-to-double.html

https://blog.51cto.com/freej/168676

http://localhost:8053/reference/XML_toString_.html


## 五、开源地址
https://github.com/breakeryu/DCDC_res_calculator
