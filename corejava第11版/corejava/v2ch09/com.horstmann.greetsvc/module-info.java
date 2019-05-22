module com.horstmann.greetsvc
{
   exports com.horstmann.greetsvc;

   provides com.horstmann.greetsvc.GreeterService with
      com.horstmann.greetsvc.internal.FrenchGreeter,
      com.horstmann.greetsvc.internal.GermanGreeterFactory;
}
