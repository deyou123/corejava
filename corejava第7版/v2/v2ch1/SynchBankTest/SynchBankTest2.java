/**
   @version 1.30 2004-08-01
   @author Cay Horstmann
*/

/** 
   This program shows how multiple threads can safely access a data structure, using 
   synchronized methods.
*/
public class SynchBankTest2
{  
   public static void main(String[] args)
   {  
      Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
      int i;
      for (i = 0; i < NACCOUNTS; i++)
      {  
         TransferRunnable r = new TransferRunnable(b, i, INITIAL_BALANCE);
         Thread t = new Thread(r);
         t.start();
      }
   }

   public static final int NACCOUNTS = 100;
   public static final double INITIAL_BALANCE = 1000;
}

/**
   A bank with a number of bank accounts.
*/
class Bank
{ 
   /**
      Constructs the bank.
      @param n the number of accounts
      @param initialBalance the initial balance
      for each account
   */
   public Bank(int n, double initialBalance)
   {  
      accounts = new double[n];
      for (int i = 0; i < accounts.length; i++)
         accounts[i] = initialBalance;
   }

   /**
      Transfers money from one account to another.
      @param from the account to transfer from
      @param to the account to transfer to
      @param amount the amount to transfer
   */
   public synchronized void transfer(int from, int to, double amount)
      throws InterruptedException
   {  
      while (accounts[from] < amount)
         wait();
      System.out.print(Thread.currentThread());      
      accounts[from] -= amount;
      System.out.printf(" %10.2f from %d to %d", amount, from, to);
      accounts[to] += amount;
      System.out.printf(" Total Balance: %10.2f%n", getTotalBalance());
      notifyAll();
   }

   /**
      Gets the sum of all account balances.
      @return the total balance
   */
   public synchronized double getTotalBalance()
   {  
      double sum = 0;
      
      for (double a : accounts)
         sum += a;
      
      return sum;
   }

   /**
      Gets the number of accounts in the bank.
      @return the number of accounts
   */
   public int size()
   {
      return accounts.length;
   }

   private final double[] accounts;
}

/**
   A runnable that transfers money from an account to other
   accounts in a bank.
*/
class TransferRunnable implements Runnable
{ 
   /**
      Constructs a transfer runnable.
      @param b the bank between whose account money is transferred
      @param from the account to transfer money from
      @param max the maximum amount of money in each transfer 
   */
   public TransferRunnable(Bank b, int from, double max)
   {  
      bank = b;
      fromAccount = from;
      maxAmount = max;
   }

   public void run()
   {  
      try
      {  
         while (true)
         {  
            int toAccount = (int) (bank.size() * Math.random());
            double amount = maxAmount * Math.random();
            bank.transfer(fromAccount, toAccount, amount);
            Thread.sleep((int) (DELAY * Math.random()));
         }
      }
      catch (InterruptedException e) {}
   }

   private Bank bank;
   private int fromAccount;
   private double maxAmount;
   private int repetitions;
   private int DELAY = 10;
}

