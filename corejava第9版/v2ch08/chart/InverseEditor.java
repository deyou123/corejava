package chart;

import java.awt.*;
import java.beans.*;
import javax.swing.*;

/**
 * The property editor for the inverse property of the ChartBean. The inverse property toggles
 * between colored graph bars and colored background.
 * @version 1.30 2007-10-03
 * @author Cay Horstmann
 */
public class InverseEditor extends PropertyEditorSupport
{
   private ImageIcon normalIcon = new ImageIcon(getClass().getResource("ChartBean_MONO_16x16.gif"));

   private ImageIcon inverseIcon = new ImageIcon(getClass().getResource(
         "ChartBean_INVERSE_16x16.gif"));

   public Component getCustomEditor()
   {
      return new InverseEditorPanel(this);
   }

   public boolean supportsCustomEditor()
   {
      return true;
   }

   public boolean isPaintable()
   {
      return true;
   }

   public String getAsText()
   {
      return null;
   }

   public String getJavaInitializationString()
   {
      return "" + getValue();
   }

   public void paintValue(Graphics g, Rectangle bounds)
   {
      ImageIcon icon = (Boolean) getValue() ? inverseIcon : normalIcon;
      int x = bounds.x + (bounds.width - icon.getIconWidth()) / 2;
      int y = bounds.y + (bounds.height - icon.getIconHeight()) / 2;
      g.drawImage(icon.getImage(), x, y, null);
   }
}
