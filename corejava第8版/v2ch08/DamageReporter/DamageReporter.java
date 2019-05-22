import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * This program demonstrates the use of an XML encoder and decoder. All GUI and drawing code is
 * collected in this class. The only interesting pieces are the action listeners for openItem and
 * saveItem. Look inside the DamageReport class for encoder customizations.
 * @version 1.01 2004-10-03
 * @author Cay Horstmann
 */
public class DamageReporter extends JFrame
{
   public static void main(String[] args)
   {
      JFrame frame = new DamageReporter();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }

   public DamageReporter()
   {
      setTitle("DamageReporter");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      report = new DamageReport();
      report.setCarType(DamageReport.CarType.SEDAN);

      // set up the menu bar
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu menu = new JMenu("File");
      menuBar.add(menu);

      JMenuItem openItem = new JMenuItem("Open");
      menu.add(openItem);
      openItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               // show file chooser dialog
               int r = chooser.showOpenDialog(null);

               // if file selected, open
               if (r == JFileChooser.APPROVE_OPTION)
               {
                  try
                  {
                     File file = chooser.getSelectedFile();
                     XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
                     report = (DamageReport) decoder.readObject();
                     decoder.close();
                     rentalRecord.setText(report.getRentalRecord());
                     carType.setSelectedItem(report.getCarType());                     
                     repaint();
                  }
                  catch (IOException e)
                  {
                     JOptionPane.showMessageDialog(null, e);
                  }
               }
            }
         });

      JMenuItem saveItem = new JMenuItem("Save");
      menu.add(saveItem);
      saveItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               report.setRentalRecord(rentalRecord.getText());
               chooser.setSelectedFile(new File(rentalRecord.getText() + ".xml"));

               // show file chooser dialog
               int r = chooser.showSaveDialog(null);

               // if file selected, save
               if (r == JFileChooser.APPROVE_OPTION)
               {
                  try
                  {
                     File file = chooser.getSelectedFile();
                     XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
                     report.configureEncoder(encoder);
                     encoder.writeObject(report);
                     encoder.close();
                  }
                  catch (IOException e)
                  {
                     JOptionPane.showMessageDialog(null, e);
                  }
               }
            }
         });

      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });

      // combo box for car type
      rentalRecord = new JTextField();
      carType = new JComboBox();
      carType.addItem(DamageReport.CarType.SEDAN);
      carType.addItem(DamageReport.CarType.WAGON);
      carType.addItem(DamageReport.CarType.SUV);

      carType.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               DamageReport.CarType item = (DamageReport.CarType) carType.getSelectedItem();
               report.setCarType(item);
               repaint();
            }
         });

      // component for showing car shape and damage locations
      carComponent = new JComponent()
         {
            public void paintComponent(Graphics g)
            {
               Graphics2D g2 = (Graphics2D) g;
               g2.setColor(new Color(0.9f, 0.9f, 0.45f));
               g2.fillRect(0, 0, getWidth(), getHeight());
               g2.setColor(Color.BLACK);
               g2.draw(shapes.get(report.getCarType()));
               report.drawDamage(g2);
            }
         };
      carComponent.addMouseListener(new MouseAdapter()
         {
            public void mousePressed(MouseEvent event)
            {
               report.click(new Point2D.Double(event.getX(), event.getY()));
               repaint();
            }
         });
      
      // radio buttons for click action
      addButton = new JRadioButton("Add");
      removeButton = new JRadioButton("Remove");
      ButtonGroup group = new ButtonGroup();
      JPanel buttonPanel = new JPanel();
      group.add(addButton);
      buttonPanel.add(addButton);
      group.add(removeButton);
      buttonPanel.add(removeButton);
      addButton.setSelected(!report.getRemoveMode());
      removeButton.setSelected(report.getRemoveMode());
      addButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               report.setRemoveMode(false);
            }
         });
      removeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               report.setRemoveMode(true);
            }
         });

      // layout components
      JPanel gridPanel = new JPanel();
      gridPanel.setLayout(new GridLayout(0, 2));
      gridPanel.add(new JLabel("Rental Record"));
      gridPanel.add(rentalRecord);
      gridPanel.add(new JLabel("Type of Car"));
      gridPanel.add(carType);
      gridPanel.add(new JLabel("Operation"));
      gridPanel.add(buttonPanel);

      add(gridPanel, BorderLayout.NORTH);
      add(carComponent, BorderLayout.CENTER);
   }

   private JTextField rentalRecord;
   private JComboBox carType;
   private JComponent carComponent;
   private JRadioButton addButton;
   private JRadioButton removeButton;
   private DamageReport report;
   private JFileChooser chooser;

   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 400;

   private static Map<DamageReport.CarType, Shape> shapes = new EnumMap<DamageReport.CarType, Shape>(
         DamageReport.CarType.class);

   static
   {
      int width = 200;
      int x = 50;
      int y = 50;
      Rectangle2D.Double body = new Rectangle2D.Double(x, y + width / 6, width - 1, width / 6);
      Ellipse2D.Double frontTire = new Ellipse2D.Double(x + width / 6, y + width / 3, width / 6,
            width / 6);
      Ellipse2D.Double rearTire = new Ellipse2D.Double(x + width * 2 / 3, y + width / 3,
            width / 6, width / 6);

      Point2D.Double p1 = new Point2D.Double(x + width / 6, y + width / 6);
      Point2D.Double p2 = new Point2D.Double(x + width / 3, y);
      Point2D.Double p3 = new Point2D.Double(x + width * 2 / 3, y);
      Point2D.Double p4 = new Point2D.Double(x + width * 5 / 6, y + width / 6);

      Line2D.Double frontWindshield = new Line2D.Double(p1, p2);
      Line2D.Double roofTop = new Line2D.Double(p2, p3);
      Line2D.Double rearWindshield = new Line2D.Double(p3, p4);

      GeneralPath sedanPath = new GeneralPath();
      sedanPath.append(frontTire, false);
      sedanPath.append(rearTire, false);
      sedanPath.append(body, false);
      sedanPath.append(frontWindshield, false);
      sedanPath.append(roofTop, false);
      sedanPath.append(rearWindshield, false);
      shapes.put(DamageReport.CarType.SEDAN, sedanPath);

      Point2D.Double p5 = new Point2D.Double(x + width * 11 / 12, y);
      Point2D.Double p6 = new Point2D.Double(x + width, y + width / 6);
      roofTop = new Line2D.Double(p2, p5);
      rearWindshield = new Line2D.Double(p5, p6);

      GeneralPath wagonPath = new GeneralPath();
      wagonPath.append(frontTire, false);
      wagonPath.append(rearTire, false);
      wagonPath.append(body, false);
      wagonPath.append(frontWindshield, false);
      wagonPath.append(roofTop, false);
      wagonPath.append(rearWindshield, false);
      shapes.put(DamageReport.CarType.WAGON, wagonPath);

      Point2D.Double p7 = new Point2D.Double(x + width / 3, y - width / 6);
      Point2D.Double p8 = new Point2D.Double(x + width * 11 / 12, y - width / 6);
      frontWindshield = new Line2D.Double(p1, p7);
      roofTop = new Line2D.Double(p7, p8);
      rearWindshield = new Line2D.Double(p8, p6);

      GeneralPath suvPath = new GeneralPath();
      suvPath.append(frontTire, false);
      suvPath.append(rearTire, false);
      suvPath.append(body, false);
      suvPath.append(frontWindshield, false);
      suvPath.append(roofTop, false);
      suvPath.append(rearWindshield, false);
      shapes.put(DamageReport.CarType.SUV, suvPath);
   }
}
