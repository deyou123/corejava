/**
   @version 1.21 2002-06-19
   @author Cay Horstmann
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class WelcomeApplet extends JApplet
{
   public void init()
   {
      setLayout(new BorderLayout());

      JLabel label = new JLabel(getParameter("greeting"), SwingConstants.CENTER);
      label.setFont(new Font("Serif", Font.BOLD, 18));
      add(label, BorderLayout.CENTER);

      JPanel panel = new JPanel();

      JButton cayButton = new JButton("Cay Horstmann");
      cayButton.addActionListener(makeURLActionListener(
         "http://www.horstmann.com"));
      panel.add(cayButton);

      JButton garyButton = new JButton("Gary Cornell");
      garyButton.addActionListener(makeURLActionListener(
         "mailto:gary@thecornells.com"));
      panel.add(garyButton);

      add(panel, BorderLayout.SOUTH);
   }

   private ActionListener makeURLActionListener(final String u)
   {
      return new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {
                  getAppletContext().showDocument(new URL(u));
               }
               catch(MalformedURLException e) 
               { 
                  e.printStackTrace(); 
               }
            }
         };
   }
}
