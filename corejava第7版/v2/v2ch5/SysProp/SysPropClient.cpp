/**
   @version 1.11 2004-08-15
   @author Cay Horstmann
*/

#include <iostream>

#include "SysProp.hh"

using namespace std;

CORBA::Object_ptr getObjectReference(CORBA::ORB_ptr orb, const char serviceName[])
{  
   CosNaming::NamingContext_var rootContext;

   try
   {  
      // Obtain a reference to the root context of the name service:
      CORBA::Object_var initServ;
      initServ = orb->resolve_initial_references("NameService");

      // Narrow the object returned by resolve_initial_references() to a CosNaming::NamingContext 
      // object
      rootContext = CosNaming::NamingContext::_narrow(initServ);
      if (CORBA::is_nil(rootContext))
      {  
         cerr << "Failed to narrow naming context." << endl;
         return CORBA::Object::_nil();
      }
   }
   catch (CORBA::ORB::InvalidName&)
   {  
      cerr << "Name service does not exist." << endl;
      return CORBA::Object::_nil();
   }

   // Create a name object, containing the name corejava/SysProp:
   CosNaming::Name name;
   name.length(1);

   name[0].id   = serviceName;
   name[0].kind = "Object";

   CORBA::Object_ptr obj;
   try
   {  
      // Resolve the name to an object reference, and assign the returned reference to a 
      // CORBA::Object:
      obj = rootContext->resolve(name);
   }
   catch (CosNaming::NamingContext::NotFound&)
   {  
      // This exception is thrown if any of the components of the path [contexts or the object] 
      // aren't found:
      cerr << "Context not found." << endl;
      return CORBA::Object::_nil();
   }
   return obj;
}

int main (int argc, char *argv[])
{  
   CORBA::ORB_ptr orb = CORBA::ORB_init(argc, argv, "omniORB4");

   CORBA::Object_var obj = getObjectReference(orb, "SysProp");
   SysProp_var sysProp = SysProp::_narrow(obj);

   if (CORBA::is_nil(sysProp))
   { 
      cerr << "Cannot invoke on a nil object reference."  << endl;
      return 1;
   }

   CORBA::String_var key = "java.vendor";
   CORBA::String_var value = sysProp->getProperty(key);

   cerr << key << "=" << value << endl;

   return 0;
}


