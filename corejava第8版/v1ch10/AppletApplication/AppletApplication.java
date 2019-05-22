/*
 * The applet viewer reads the tags below if you call it with appletviewer AppletApplication.java No
 * separate HTML file is required. <applet code="AppletApplication.class" width="200" height="200">
 * </applet>
 */

import java.awt.EventQueue;

import javax.swing.*;

/**
 * It's an applet. It's an application. It's BOTH!
 * @version 1.32 2007-04-28
 * @author Cay Horstmann
 */
public class AppletApplication extends NotHelloWorldApplet
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               AppletFrame frame = new AppletFrame(new NotHelloWorldApplet());
               frame.setTitle("NotHelloWorldApplet");
               frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }

   public static final int DEFAULT_WIDTH = 200;
   public static final int DEFAULT_HEIGHT = 200;
}