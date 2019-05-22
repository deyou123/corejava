/**
   @version 1.10 2004-08-04
   @author Cay Horstmann
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
   This program demonstrates how to use the URLConnection class for a POST request. 
*/
public class PostTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new PostTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

class PostTestFrame extends JFrame
{  
   /**
      Makes a POST request and returns the server response.
      @param urlString the URL to post to
      @param nameValuePairs a map of name/value pairs to supply in the request.
      @return the server reply (either from the input stream or the error stream)
   */
   public static String doPost(String urlString, Map<String, String> nameValuePairs) 
      throws IOException
   {  
      URL url = new URL(urlString);
      URLConnection connection = url.openConnection();
      connection.setDoOutput(true);

      PrintWriter out = new PrintWriter(connection.getOutputStream());

      boolean first = true;
      for (Map.Entry<String, String> pair : nameValuePairs.entrySet())
      {  
         if (first) first = false; 
         else out.print('&'); 
         String name = pair.getKey();
         String value = pair.getValue();
         out.print(name);
         out.print('=');
         out.print(URLEncoder.encode(value, "UTF-8"));
      }

      out.close();

      Scanner in;
      StringBuilder response = new StringBuilder();
      try
      {  
         in = new Scanner(connection.getInputStream());
      }
      catch (IOException e)
      {  
         if (!(connection instanceof HttpURLConnection)) throw e;
         InputStream err
            = ((HttpURLConnection)connection).getErrorStream();
         if (err == null) throw e;
         in = new Scanner(err);
      }
      while (in.hasNextLine())
      {
         response.append(in.nextLine());
         response.append("\n");
      }

      in.close();
      return response.toString();
   }

   public PostTestFrame()
   {  
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setTitle("PostTest");

      JPanel northPanel = new JPanel();
      add(northPanel, BorderLayout.NORTH);

      final JComboBox combo = new JComboBox();
      for (int i = 0; i < countries.length; i += 2)
      combo.addItem(countries[i]);
      northPanel.add(combo);

      final JTextArea result = new JTextArea();
      add(new JScrollPane(result));

      JButton getButton = new JButton("Get");
      northPanel.add(getButton);
      getButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {  
               new Thread(new 
                  Runnable()
                  {
                     public void run()
                     {   
                        final String SERVER_URL = "http://www.census.gov/cgi-bin/ipc/idbsprd";
                        result.setText("");
                        Map<String, String> post = new HashMap<String, String>();
                        post.put("tbl", "001");
                        post.put("cty", countries[2 * combo.getSelectedIndex() + 1]);
                        post.put("optyr", "latest checked");
                        try
                        {
                           result.setText(doPost(SERVER_URL, post));
                        }
                        catch (IOException e)
                        {
                           result.setText("" + e);
                        }
                     }
                  }).start();
            }            
         });   
   }

   private static String[] countries = {
      "Afghanistan", "AF", "Albania", "AL", "Algeria", "AG", "American Samoa", "AQ",
      "Andorra", "AN", "Angola", "AO", "Anguilla", "AV", "Antigua and Barbuda", "AC",
      "Argentina", "AR", "Armenia", "AM", "Aruba", "AA", "Australia", "AS", "Austria", "AU",
      "Azerbaijan", "AJ", "Bahamas, The", "BF", "Bahrain", "BA", "Bangladesh", "BG",
      "Barbados", "BB", "Belarus", "BO", "Belgium", "BE", "Belize", "BH", "Benin", "BN",
      "Bermuda", "BD", "Bhutan", "BT", "Bolivia", "BL", "Bosnia and Herzegovina", "BK",
      "Botswana", "BC", "Brazil", "BR", "Brunei", "BX", "Bulgaria", "BU", "Burkina Faso", "UV",
      "Burma", "BM", "Burundi", "BY", "Cambodia", "CB", "Cameroon", "CM", "Canada", "CA",
      "Cape Verde", "CV", "Cayman Islands", "CJ", "Central African Republic", "CT", "Chad", "CD",
      "Chile", "CI", "China", "CH", "Colombia", "CO", "Comoros", "CN", "Congo (Brazzaville", "CF",
      "Congo (Kinshasa)", "CG", "Cook Islands", "CW", "Costa Rica", "CS", "Cote d'Ivoire", "IV",
      "Croatia", "HR", "Cuba", "CU", "Cyprus", "CY", "Czech Republic", "EZ", "Denmark", "DA",
      "Djibouti", "DJ", "Dominica", "DO", "Dominican Republic", "DR", "East Timor", "TT",
      "Ecuador", "EC", "Egypt", "EG", "El Salvador", "ES", "Equatorial Guinea", "EK",
      "Eritrea", "ER", "Estonia", "EN", "Ethiopia", "ET", "Faroe Islands", "FO", "Fiji", "FJ",
      "Finland", "FI", "France", "FR", "French Guiana", "FG", "French Polynesia", "FP",
      "Gabon", "GB", "Gambia, The", "GA", "Gaza Strip", "GZ", "Georgia", "GG", "Germany", "GM",
      "Ghana", "GH", "Gibraltar", "GI", "Greece", "GR", "Greenland", "GL", "Grenada", "GJ",
      "Guadeloupe", "GP", "Guam", "GQ", "Guatemala", "GT", "Guernsey", "GK", "Guinea", "GV",
      "Guinea-Bissau", "PU", "Guyana", "GY", "Haiti", "HA", "Honduras", "HO", 
      "Hong Kong S.A.R", "HK", "Hungary", "HU", "Iceland", "IC", "India", "IN", "Indonesia", "ID",
      "Iran", "IR", "Iraq", "IZ", "Ireland", "EI", "Israel", "IS", "Italy", "IT", "Jamaica", "JM",
      "Japan", "JA", "Jersey", "JE", "Jordan", "JO", "Kazakhstan", "KZ", "Kenya", "KE",
      "Kiribati", "KR", "Korea, North", "KN", "Korea, South", "KS", "Kuwait", "KU", 
      "Kyrgyzstan", "KG", "Laos", "LA", "Latvia", "LG", "Lebanon", "LE", "Lesotho", "LT", 
      "Liberia", "LI", "Libya", "LY", "Liechtenstein", "LS", "Lithuania", "LH", "Luxembourg", "LU",
      "Macau S.A.R", "MC", "Macedonia, The Former Yugo. Rep. of", "MK", "Madagascar", "MA",
      "Malawi", "MI", "Malaysia", "MY", "Maldives", "MV", "Mali", "ML", "Malta", "MT",
      "Man, Isle of", "IM", "Marshall Islands", "RM", "Martinique", "MB", "Mauritania", "MR",
      "Mauritius", "MP", "Mayotte", "MF", "Mexico", "MX", "Micronesia, Federated States of", "FM",
      "Moldova", "MD", "Monaco", "MN", "Mongolia", "MG", "Montserrat", "MH", "Morocco", "MO",
      "Mozambique", "MZ", "Namibia", "WA", "Nauru", "NR", "Nepal", "NP", "Netherlands", "NL",
      "Netherlands Antilles", "NT", "New Caledonia", "NC", "New Zealand", "NZ", "Nicaragua", "NU",
      "Niger", "NG", "Nigeria", "NI", "Northern Mariana Islands", "CQ", "Norway", "NO",
      "Oman", "MU", "Pakistan", "PK", "Palau", "PS", "Panama", "PM", "Papua New Guinea", "PP",
      "Paraguay", "PA", "Peru", "PE", "Philippines", "RP", "Poland", "PL", "Portugal", "PO",
      "Puerto Rico", "RQ", "Qatar", "QA", "Reunion", "RE", "Romania", "RO", "Russia", "RS",
      "Rwanda", "RW", "Saint Helena", "SH", "Saint Kitts and Nevis", "SC", "Saint Lucia", "ST",
      "Saint Pierre and Miquelon", "SB", "Saint Vincent and the Grenadines", "VC", "Samoa", "WS",
      "San Marino", "SM", "Sao Tome and Principe", "TP", "Saudi Arabia", "SA", "Senegal", "SG",
      "Serbia and Montenegro", "YI", "Seychelles", "SE", "Sierra Leone", "SL", "Singapore", "SN",
      "Slovakia", "LO", "Slovenia", "SI", "Solomon Islands", "BP", "Somalia", "SO",
      "South Africa", "SF", "Spain", "SP", "Sri Lanka", "CE", "Sudan", "SU", "Suriname", "NS",
      "Swaziland", "WZ", "Sweden", "SW", "Switzerland", "SZ", "Syria", "SY", "Taiwan", "TW",
      "Tajikistan", "TI", "Tanzania", "TZ", "Thailand", "TH", "Togo", "TO", "Tonga", "TN",
      "Trinidad and Tobago", "TD", "Tunisia", "TS", "Turkey", "TU", "Turkmenistan", "TX",
      "Turks and Caicos Islands", "TK", "Tuvalu", "TV", "Uganda", "UG", "Ukraine", "UP",
      "United Arab Emirates", "TC", "United Kingdom", "UK", "United States", "US", "Uruguay", "UY",
      "Uzbekistan", "UZ", "Vanuatu", "NH", "Venezuela", "VE", "Vietnam", "VM", 
      "Virgin Islands", "VQ", "Virgin Islands, British", "VI", "Wallis and Futuna", "WF",
      "West Bank", "WE", "Western Sahara", "WI", "Yemen", "YM", "Zambia", "ZA", "Zimbabwe", "ZI"
   };

   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 300;  
}
