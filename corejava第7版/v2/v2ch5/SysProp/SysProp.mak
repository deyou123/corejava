# Pre-requisite:
#
# Make sure that you have environment variable LIB and INCLUDE setup for
# using Developer studio from the command line. Usually, this is accomplished
# by source the vcvars32.bat file.
#

# Where is the top of this distribution. All executable, library and include
# directories are relative to this variable.
#
TOP = d:\omniorb


##########################################################################
# Essential flags to use omniORB2
#
DIR_CPPFLAGS   = -I. -I$(TOP)\include
#
#
CORBA_CPPFLAGS = -D__WIN32__ -D__x86__ -D__NT__ -D__OSVERSION__=4
CORBA_LIB      = omniORB271_rt.lib omnithread2_rt.lib wsock32.lib advapi32.lib \
                 -libpath:$(TOP)\lib\x86_win32
CXXFLAGS       = -O2 -MD -GX $(CORBA_CPPFLAGS) $(DIR_CPPFLAGS)
CXXLINKOPTIONS =

.SUFFIXES: .cpp
.cpp.obj:
  cl /nologo /c $(CXXFLAGS) /Tp$<

SysPropClient.exe: SysPropClient.obj SysPropSK.obj
  link -nologo $(CXXLINKOPTIONS) -out:$@ $** $(CORBA_LIB) 

SysProp.hh SysPropSK.cpp: SysProp.idl
   $(TOP)\bin\x86_win32\omniidl2 -a -t -h .hh -s SK.cpp SysProp.idl
