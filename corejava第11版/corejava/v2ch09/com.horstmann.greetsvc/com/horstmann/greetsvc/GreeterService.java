package com.horstmann.greetsvc;

import java.util.Locale;

public interface GreeterService 
{
    String greet(String subject);
    Locale getLocale();
}
