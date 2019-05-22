package com.horstmann.greetsvc.internal;

import com.horstmann.greetsvc.GreeterService;
import java.util.Locale;

public class FrenchGreeter implements GreeterService 
{
    public String greet(String subject) { return "Bonjour " + subject; }
    public Locale getLocale() { return Locale.FRENCH; }
}
