package client;

import java.io.*;
import java.math.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import java.net.http.*;
import java.net.http.HttpRequest.*;

class MoreBodyPublishers 
{   
   public static BodyPublisher ofFormData(Map<Object, Object> data) 
   {
      boolean first = true;
      var builder = new StringBuilder();
      for (Map.Entry<Object, Object> entry : data.entrySet()) 
      {
          if (first) first = false;
          else builder.append("&");
          builder.append(URLEncoder.encode(entry.getKey().toString(),
             StandardCharsets.UTF_8));
          builder.append("=");
          builder.append(URLEncoder.encode(entry.getValue().toString(), 
             StandardCharsets.UTF_8));
      }
      return BodyPublishers.ofString(builder.toString());
   }
  
   private static byte[] bytes(String s) { return s.getBytes(StandardCharsets.UTF_8); }
   
   public static BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) 
         throws IOException 
   {
      var byteArrays = new ArrayList<byte[]>();
      byte[] separator = bytes("--" + boundary + "\nContent-Disposition: form-data; name="); 
      for (Map.Entry<Object, Object> entry : data.entrySet()) 
      {
         byteArrays.add(separator);
         
         if (entry.getValue() instanceof Path path) 
         {
            String mimeType = Files.probeContentType(path);
            byteArrays.add(bytes("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName() 
               + "\"\nContent-Type: " + mimeType + "\n\n")); 
            byteArrays.add(Files.readAllBytes(path));
         }
         else 
            byteArrays.add(bytes("\"" + entry.getKey() + "\"\n\n" + entry.getValue() + "\n"));          
     }
     byteArrays.add(bytes("--" + boundary + "--")); 
     return BodyPublishers.ofByteArrays(byteArrays);      
   }
   
   public static BodyPublisher ofSimpleJSON(Map<Object, Object> data) 
   {
      var builder = new StringBuilder();
      builder.append("{");
      var first = true;
      for (Map.Entry<Object, Object> entry : data.entrySet())
      {
         if (first) first = false;
         else
            builder.append(",");
         builder.append(jsonEscape(entry.getKey().toString())).append(": ")
            .append(jsonEscape(entry.getValue().toString()));
      }
      builder.append("}");
      return BodyPublishers.ofString(builder.toString());      
   }
   
   private static Map<Character, String> replacements = Map.of('\b', "\\b", '\f', "\\f",
      '\n', "\\n", '\r', "\\r", '\t', "\\t", '"', "\\\"", '\\', "\\\\");
   
   private static StringBuilder jsonEscape(String str)
   {
      var result = new StringBuilder("\"");
      for (int i = 0; i < str.length(); i++)
      {
         char ch = str.charAt(i);
         String replacement = replacements.get(ch);
         if (replacement == null) result.append(ch);
         else result.append(replacement);
      }
      result.append("\"");
      return result;
   }
}

public class HttpClientTest
{
   public static void main(String[] args)
         throws IOException, URISyntaxException, InterruptedException
   {
      System.setProperty("jdk.httpclient.HttpClient.log", "headers,errors");
      String propsFilename = args.length > 0 ? args[0] : "client/post.properties"; 
      Path propsPath = Path.of(propsFilename);
      var props = new Properties();
      try (Reader in = Files.newBufferedReader(propsPath, StandardCharsets.UTF_8))
      {
         props.load(in);
      }
      String urlString = "" + props.remove("url");
      String contentType = "" + props.remove("Content-Type");
      if (contentType.equals("multipart/form-data")) 
      {
         var generator = new Random();
         String boundary = new BigInteger(256, generator).toString();
         contentType += ";boundary=" + boundary; 
         props.replaceAll((k, v) -> 
            v.toString().startsWith("file://") 
               ? propsPath.getParent().resolve(Path.of(v.toString().substring(7))) 
               : v);
      } 
      String result = doPost(urlString, contentType, props);
      System.out.println(result);
   }  
   
   public static String doPost(String url, String contentType, Map<Object, Object> data)
         throws IOException, URISyntaxException, InterruptedException
   {        
      HttpClient client = HttpClient.newBuilder()
         .followRedirects(HttpClient.Redirect.ALWAYS).build();
            
      BodyPublisher publisher = null;
      if (contentType.startsWith("multipart/form-data")) 
      {
         String boundary = contentType.substring(contentType.lastIndexOf("=") + 1);
         publisher = MoreBodyPublishers.ofMimeMultipartData(data, boundary);
      } 
      else if (contentType.equals("application/x-www-form-urlencoded"))
         publisher = MoreBodyPublishers.ofFormData(data);
      else
      {
         contentType = "application/json";
         publisher = MoreBodyPublishers.ofSimpleJSON(data);
      }
      
      HttpRequest request  = HttpRequest.newBuilder()
         .uri(new URI(url))
         .header("Content-Type", contentType)
         .POST(publisher)
         .build();
      HttpResponse<String> response 
         = client.send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
   }   
}
