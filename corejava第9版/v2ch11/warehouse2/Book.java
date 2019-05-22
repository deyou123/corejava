/**
 * A book is a product with an ISBN number.
 * @version 1.0 2007-10-09
 * @author Cay Horstmann
 */
public class Book extends Product
{
   private String isbn;

   public Book(String title, String isbn, double price)
   {
      super(title, price);
      this.isbn = isbn;
   }
   
   public String getDescription()
   {
      return super.getDescription() + " " + isbn;
   }  
}
