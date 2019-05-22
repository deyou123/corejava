package spinner;

import javax.swing.*;

/**
 * A model that dynamically generates word permutations.
 */
public class PermutationSpinnerModel extends AbstractSpinnerModel
{
   private String word;

   /**
    * Constructs the model.
    * @param w the word to permute
    */
   public PermutationSpinnerModel(String w)
   {
      word = w;
   }

   public Object getValue()
   {
      return word;
   }

   public void setValue(Object value)
   {
      if (!(value instanceof String)) throw new IllegalArgumentException();
      word = (String) value;
      fireStateChanged();
   }

   public Object getNextValue()
   {
      int[] codePoints = toCodePointArray(word);
      for (int i = codePoints.length - 1; i > 0; i--)
      {
         if (codePoints[i - 1] < codePoints[i])
         {
            int j = codePoints.length - 1;
            while (codePoints[i - 1] > codePoints[j])
               j--;
            swap(codePoints, i - 1, j);
            reverse(codePoints, i, codePoints.length - 1);
            return new String(codePoints, 0, codePoints.length);
         }
      }
      reverse(codePoints, 0, codePoints.length - 1);
      return new String(codePoints, 0, codePoints.length);
   }

   public Object getPreviousValue()
   {
      int[] codePoints = toCodePointArray(word);
      for (int i = codePoints.length - 1; i > 0; i--)
      {
         if (codePoints[i - 1] > codePoints[i])
         {
            int j = codePoints.length - 1;
            while (codePoints[i - 1] < codePoints[j])
               j--;
            swap(codePoints, i - 1, j);
            reverse(codePoints, i, codePoints.length - 1);
            return new String(codePoints, 0, codePoints.length);
         }
      }
      reverse(codePoints, 0, codePoints.length - 1);
      return new String(codePoints, 0, codePoints.length);
   }

   private static int[] toCodePointArray(String str)
   {
      int[] codePoints = new int[str.codePointCount(0, str.length())];
      for (int i = 0, j = 0; i < str.length(); i++, j++)
      {
         int cp = str.codePointAt(i);
         if (Character.isSupplementaryCodePoint(cp)) i++;
         codePoints[j] = cp;
      }
      return codePoints;
   }

   private static void swap(int[] a, int i, int j)
   {
      int temp = a[i];
      a[i] = a[j];
      a[j] = temp;
   }

   private static void reverse(int[] a, int i, int j)
   {
      while (i < j)
      {
         swap(a, i, j);
         i++;
         j--;
      }
   }
}
