/**
   @version 1.11 2004-08-15
   @author Cay Horstmann
*/

#include <iostream>
#include <cstdlib>

#include "Env.hh"

using namespace std;

class EnvImpl : 
   public POA_Env,
   public PortableServer::RefCountServantBase
{
public:
   virtual char* getenv(const char *name);
};

char* EnvImpl::getenv(const char *name)
{  
   char* value = std::getenv(name);
   return CORBA::string_dup(value);
}

static void bindObjectToName(CORBA::ORB_ptr orb, const char name[], CORBA::Object_ptr objref)
{
   CosNaming::NamingContext_var rootContext;

   try 
   {
      // Obtain a reference to the root context of the name service:
      CORBA::Object_var obj;
      obj = orb->resolve_initial_references("NameService");
      
      // Narrow the reference returned.
      rootContext = CosNaming::NamingContext::_narrow(obj);
      if(CORBA::is_nil(rootContext)) 
      {
         cerr << "Failed to narrow the root naming context." << endl;
         return;
      }
   }
   catch (CORBA::ORB::InvalidName& ex) 
   {
      // This should not happen!
      cerr << "Service required is invalid [does not exist]." << endl;
      return;
   }

   try 
   {
      CosNaming::Name contextName;
      contextName.length(1);
      contextName[0].id   = (const char*) "corejava"; 
      contextName[0].kind = (const char*) "Context"; 

      CosNaming::NamingContext_var corejavaContext;
      try 
      {
         // Bind the context to root.
         corejavaContext = rootContext->bind_new_context(contextName);
      }
      catch (CosNaming::NamingContext::AlreadyBound& ex) 
      {
         // If the context already exists, this exception will be raised. In this case, just 
         // resolve the name and assign the context to the object returned:
         CORBA::Object_var obj;
         obj = rootContext->resolve(contextName);
         corejavaContext = CosNaming::NamingContext::_narrow(obj);
         if( CORBA::is_nil(corejavaContext) ) 
         {
            cerr << "Failed to narrow naming context." << endl;
            return;
         }
      }

      // Bind objref with given name to the context:
      CosNaming::Name objectName;
      objectName.length(1);
      objectName[0].id = name;
      objectName[0].kind = (const char*) "Object"; 

      try 
      {      
         corejavaContext->bind(objectName, objref);
      }
      catch (CosNaming::NamingContext::AlreadyBound& ex) 
      {
         corejavaContext->rebind(objectName, objref);
      }
   }
   catch (CORBA::COMM_FAILURE& ex) 
   {
      cerr << "Caught system exception COMM_FAILURE--unable to contact the naming service." 
         << endl;
   }
   catch (CORBA::SystemException&) 
   {
      cerr << "Caught a CORBA::SystemException while using the naming service." << endl;
   }
}

int main(int argc, char *argv[])
{  
   cout << "Creating and initializing the ORB..." << endl;

   CORBA::ORB_var orb = CORBA::ORB_init(argc, argv, "omniORB4");

   CORBA::Object_var obj = orb->resolve_initial_references("RootPOA");
   PortableServer::POA_var poa = PortableServer::POA::_narrow(obj);
   poa->the_POAManager()->activate();

   EnvImpl* envImpl = new EnvImpl();
   poa->activate_object(envImpl);

   // Obtain a reference to the object, and register it in the naming service.
   obj = envImpl->_this();

   cout << orb->object_to_string(obj) << endl;
   cout << "Binding server implementations to registry..." << endl;
   bindObjectToName(orb, "Env", obj);
   envImpl->_remove_ref();

   cout << "Waiting for invocations from clients..." << endl;
   orb->run();

   return 0;
}





