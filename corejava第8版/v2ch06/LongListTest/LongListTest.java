import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * This program demonstrates a list that dynamically computes list entries.
 * @version 1.23 2007-08-01
 * @author Cay Horstmann
 */
public class LongListTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new LongListFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame contains a long word list and a label that shows a sentence made up from the chosen
 * word.
 */
class LongListFrame extends JFrame
{
   public LongListFrame()
   {
      setTitle("LongListTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      wordList = new JList(new WordListModel(3));
      wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      wordList.setPrototypeCellValue("www");
      JScrollPane scrollPane = new JScrollPane(wordList);

      JPanel p = new JPanel();
      p.add(scrollPane);
      wordList.addListSelectionListener(new ListSelectionListener()
         {
            public void valueChanged(ListSelectionEvent evt)
            {
               StringBuilder word = (StringBuilder) wordList.getSelectedValue();
               setSubject(word.toString());
            }

         });

      Container contentPane = getContentPane();
      contentPane.add(p, BorderLayout.NORTH);
      label = new JLabel(prefix + suffix);
      contentPane.add(label, BorderLayout.CENTER);
      setSubject("fox");
   }

   /**
    * Sets the subject in the label.
    * @param word the new subject that jumps over the lazy dog
    */
   public void setSubject(String word)
   {
      StringBuilder text = new StringBuilder(prefix);
      text.append(word);
      text.append(suffix);
      label.setText(text.toString());
   }

   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 300;
   private JList wordList;
   private JLabel label;
   private String prefix = "The quick brown ";
   private String suffix = " jumps over the lazy dog.";
}

/**
 * A model that dynamically generates n-letter words.
 */
class WordListModel extends AbstractListModel
{
   /**
    * Constructs the model.
    * @param n the word length
    */
   public WordListModel(int n)
   {
      length = n;
   }

   public int getSize()
   {
      return (int) Math.pow(LAST - FIRST + 1, length);
   }

   public Object getElementAt(int n)
   {
      StringBuilder r = new StringBuilder();
      ;
      for (int i = 0; i < length; i++)
      {
         char c = (char) (FIRST + n % (LAST - FIRST + 1));
         r.insert(0, c);
         n = n / (LAST - FIRST + 1);
      }
      return r;
   }

   private int length;
   public static final char FIRST = 'a';
   public static final char LAST = 'z';
}
