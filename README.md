Leap Motion Virtual Theremin Project  
Version 2.0 (4/30/2014)
#### General Information

We are using the [Leap Motion API](https://developer.leapmotion.com) and developer tools to create a virtual theremin.
Using MIDI tools such as [MaxMSP](http://cycling74.com/products/max/), we map various sections of the space above the 
Leap Motion device to recreate an authentic experience of playing a theremin. Two different scenes are available:
* Main allows the user to play all 127 MIDI notes on a colorful display
* MainBasic allows the user to play MIDI notes 55-103 and has lines indicating the beginning/end of octaves

###### Languages used
Java, implementing Leap Motion SDK and using a Javafx application

###### Known bugs
* Circle movement is not always smooth
* Circle can get stuck on an edge
* Scene selection must be made before project is run

###### Next Steps
The second half of the project was mainly focused on adding visual effects to the application using Javafx. Additionally we improved the MIDI notes so that the theremin plays MIDI notes 55-103, the typical range for a theremin. The note is determined by the y coordinate while the volume is determined by the x coordinate and only one 2D point is used to generate this x and y.
* Continue to improve on the display, create a way to switch between Main and MainBasic
* Use both hands like with a real theremin, one to adjust volume and one to adjust the note

#### Installation Notes

* This software requires the use of a Leap Motion controller. They are available here: [leapmotion.com](https://store.leapmotion.com/(S(m0bbeboq5cgvhzzkfozqwxfk))/Pages/LeapSolution.aspx)
* To get the project to work on Eclipse follow this tutorial: [How to use in Eclipse](https://docs.google.com/document/d/1FQbyOAb0PIhPSyC9T-FsVGxfBntA_3y8A2522hvCTz8/edit?usp=sharing")

##### System Requirements
* This program will run on any operating system supported by Leap Motion.
* You must install Leap Motion's interface in order to download the application.

#### Credits and Copyright
The authors of this project are Jessica de la Cruz, Ben Galatzer-Levy, Alex Kenney, Sarah Murray, and Grant Stewart. We created this project for our Human Computer Interaction (COMP 388/441) course at Loyola University Chicago in Spring 2014 under the guidance of Professor Mark Albert.
This is a continuation of the project started by Hugh Adam, Heba Bubakr, Sarah Murray, Grant Stewart, and Ian Logan. There original repository for Version 1.0 can be found here:  [Theremin](https://github.com/sarahelizabeth/theremin)

The authors of this project are Hugh Adam, Heba Bubakr, Sarah Murray, Grant Stewart, and Ian Logan.  
We created this project for our Human Computer Interaction (COMP 388/441) course at Loyola University Chicago in Spring 2014 under the guidance of Professor Mark Albert.

###### Contact
Email: alexjkenney@gmail.com


