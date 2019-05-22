package limitations;

interface Task
{
   void run() throws Exception;
   
   @SuppressWarnings("unchecked")
   static <T extends Throwable> void throwAs(Throwable t) throws T 
   {
      throw (T) t;
   }
   
   static Runnable asRunnable(Task task) 
   {
      return () -> 
      { 
         try 
         {
            task.run();
         } 
         catch (Exception e) 
         {
            Task.<RuntimeException>throwAs(e);
         }
      };
   }
}

public class DefeatCheckedExceptionChecking
{
   public static void main(String[] args)
   {
      var thread = new Thread(Task.asRunnable(() -> 
         {
            Thread.sleep(1000);
            System.out.println("Hello, World!");
            throw new Exception("Check this out!");
         }));
      thread.start();
   }
}
