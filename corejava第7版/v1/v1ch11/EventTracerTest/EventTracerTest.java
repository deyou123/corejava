/**
   @version 1.12 2004-05-10
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EventTracerTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new EventTracerFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

class EventTracerFrame extends JFrame
{
   public EventTracerFrame()
   {
      setTitle("EventTracerTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add a slider and a button
      add(new JSlider(), BorderLayout.NORTH);
      add(new JButton("Test"), BorderLayout.SOUTH);

      // trap all events of components inside the frame
      EventTracer tracer = new EventTracer();
      tracer.add(this);      
   }

   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 400;  
}
