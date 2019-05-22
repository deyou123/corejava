package permissions;

import java.security.*;
import java.util.*;

/**
 * A permission that checks for bad words.
 */
public class WordCheckPermission extends Permission
{
   private String action;

   /**
    * Constructs a word check permission.
    * @param target a comma separated word list
    * @param anAction "insert" or "avoid"
    */
   public WordCheckPermission(String target, String anAction)
   {
      super(target);
      action = anAction;
   }

   public String getActions()
   {
      return action;
   }

   public boolean equals(Object other)
   {
      if (other == null) return false;
      if (!getClass().equals(other.getClass())) return false;
      var b = (WordCheckPermission) other;
      if (!Objects.equals(action, b.action)) return false;
      if ("insert".equals(action)) return Objects.equals(getName(), b.getName());
      else if ("avoid".equals(action)) return badWordSet().equals(b.badWordSet());
      else return false;
   }

   public int hashCode()
   {
      return Objects.hash(getName(), action);
   }

   public boolean implies(Permission other)
   {
      if (!(other instanceof WordCheckPermission)) return false;
      var b = (WordCheckPermission) other;
      if (action.equals("insert"))
      {
         return b.action.equals("insert") && getName().indexOf(b.getName()) >= 0;
      }
      else if (action.equals("avoid"))
      {
         if (b.action.equals("avoid")) return b.badWordSet().containsAll(badWordSet());
         else if (b.action.equals("insert"))
         {
            for (String badWord : badWordSet())
               if (b.getName().indexOf(badWord) >= 0) return false;
            return true;
         }
         else return false;
      }
      else return false;
   }

   /**
    * Gets the bad words that this permission rule describes.
    * @return a set of the bad words
    */
   public Set<String> badWordSet()
   {
      var set = new HashSet<String>();
      set.addAll(List.of(getName().split(",")));
      return set;
   }
}
