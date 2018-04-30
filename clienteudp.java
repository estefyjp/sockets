import java.net.* ;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Clienteudp extends Thread
{
   private final static int PACKETSIZE = 100 ;
   private static String username;
   private static DatagramSocket socket;
   private static DatagramPacket packet;
   private static InetAddress host;
   private static int port;


    public static InetAddress getHost() {
        return host;
    }

    public static void setHost(InetAddress newhost) {
        host = newhost;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int newport) {
        port = newport;
    }

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static void setSocket(DatagramSocket newsocket) {
        socket = newsocket;
    }


    public static String getUsername() {
        return username;
    }

    public static void setUsername(String newusername) {
        username = newusername;
    }


    static Thread thread1 = new Thread () {
        public void run () {
            System.out.println("thread write");
            client_operations();
        }
    };
    static Thread thread2 = new Thread () {
        public void run () {
            System.out.println("thread read");
            read_always();
        }
    };

   public static void main( String args[] )
   {

       // Check the arguments
       /*if( args.length != 2 )
       {
          System.out.println( "Sintaxis: java #nombrearchivo host port" ) ;
          return ;
      }*/
      DatagramSocket socket = null ;
      try
      {
        //the host and port must be of the server,
         // Convert the arguments first, to ensure that they are valid
        InetAddress host = InetAddress.getByName("192.168.1.68");
        int port= 8888;
        //InetAddress host = InetAddress.getByName( args[0] ) ;
        setHost(host);
        //int port= Integer.parseInt( args[1] ) ;
        setPort(port);
         String s_json;
         DatagramPacket packet;
         byte [] data;
         // Construct the socket
         socket = new DatagramSocket() ;
         setSocket(socket);
         System.out.println("Enter username: ");
             BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
             String username = bufferRead.readLine();
             setUsername(username);
             s_json =  "{\"id\":  \"\", \"user\": \""+username+"\",\"text\": \"\",\"action\": \"e\"}";
             data = s_json.getBytes() ;
             packet = new DatagramPacket( data, data.length, host, port ) ;
            // Send it
             socket.send( packet ) ;
             // Set a receive timeout, 2000 milliseconds
 			socket.setSoTimeout( 10000 ) ;
 			// Prepare the packet for receive
 			packet.setData( new byte[PACKETSIZE] ) ;
 			// Wait for a response from the server
 			socket.receive( packet ) ;
 			// Print the response
 			System.out.println( new String(packet.getData()) ) ;

        //TODO Agregar validacion en caso de que se ingrese un nombre de usuarioya existente y se tenga que pedir nuevamente

        thread1.start();
        thread2.start();
      }
      catch( Exception e )
      {
         System.out.println( e ) ;
      }
      finally
      {
         if( socket != null )
            socket.close() ;
      }
   }

   public static void client_operations(){

       DatagramSocket socket = null ;
       while(true){
           try
           {
               String s_json;
               DatagramPacket packet;
               byte [] data;
               InetAddress host =  getHost();
               int port = getPort();
               // Construct the socket
               socket = new DatagramSocket() ;

               //while(true){
               String option, m, recipient, username;
               int id=0;
               username = getUsername();
               Scanner scanner= new Scanner(System.in);
               System.out.println("--Select an option--");
               System.out.println("a) Send message to all users");
               System.out.println("b) Show online users");
               System.out.println("c) Send private message");
               System.out.println("d) Exit");
               System.out.print(">>");
               option= scanner.nextLine();
               switch(option){
               case "a":
                   System.out.println("Type the message: ");
                   System.out.print(">>");
                   m= scanner.nextLine();
                   s_json =  "{\"id\":  \""+id+"\", \"user\": \""+username+"\",\"text\": \""+m+"\",\"action\": \"a\"}";
                   data = s_json.getBytes() ;
                   packet = new DatagramPacket( data, data.length, host, port ) ;
                           // Send it
                   socket.send( packet ) ;
                   // Set a receive timeout, 2000 milliseconds
                   socket.setSoTimeout( 10000 ) ;
                   // Prepare the packet for receive
                   packet.setData( new byte[PACKETSIZE] ) ;
                   // Wait for a response from the server
                   socket.receive( packet ) ;
                   // Print the response
                   System.out.println( new String(packet.getData()) ) ;
               break;
               case "b":
                   System.out.println("Users online:");
                   s_json =  "{\"id\":  \""+id+"\", \"user\": \""+username+"\",\"text\": \"text\",\"action\": \"b\"}";
                   data = s_json.getBytes() ;
                   packet = new DatagramPacket( data, data.length, host, port ) ;
                           // Send it
                   socket.send( packet ) ;
                   // Set a receive timeout, 2000 milliseconds
                   socket.setSoTimeout( 10000 ) ;
                   // Prepare the packet for receive
                   packet.setData( new byte[PACKETSIZE] ) ;
                   // Wait for a response from the server
                   socket.receive( packet ) ;
                   // Print the response
                   System.out.println( new String(packet.getData()) ) ;
               break;
               case "c":
                   System.out.println("Type the message: ");
                   System.out.print(">>");
                   m= scanner.nextLine();
                   System.out.println("Type the recipient: ");
                   System.out.print(">>");
                   recipient= scanner.nextLine();
                   m = m + "," + recipient;
                   s_json =  "{\"id\":  \""+id+"\", \"user\": \""+username+"\",\"text\": \""+m+"\",\"action\": \"c\"}";
                   data = s_json.getBytes() ;
                   packet = new DatagramPacket( data, data.length, host, port);
                           // Send it
                   socket.send( packet ) ;
                   // Set a receive timeout, 2000 milliseconds
                   socket.setSoTimeout( 10000 ) ;
                   // Prepare the packet for receive
                   packet.setData( new byte[PACKETSIZE] ) ;
                   // Wait for a response from the server
                   socket.receive( packet ) ;
                   // Print the response
                   System.out.println( new String(packet.getData()) ) ;
               break;
               case "d":
                   System.out.println("Exit...");
        	       s_json =  "{\"id\":  \""+id+"\", \"user\": \""+username+"\",\"text\": \"Exit\",\"action\": \"d\"}";
                   data = s_json.getBytes() ;
                   packet = new DatagramPacket( data, data.length, host, port);
                           // Send it
                   socket.send( packet ) ;
                   // Set a receive timeout, 2000 milliseconds
                   socket.setSoTimeout( 10000 ) ;
                   // Prepare the packet for receive
                   packet.setData( new byte[PACKETSIZE] ) ;
                   // Wait for a response from the server
                   socket.receive( packet ) ;
                   // Print the response
                   System.out.println( new String(packet.getData()) ) ;
               break;
               default: System.out.println("Select the correct option");
               break;

               }
           }
           catch( Exception e )
           {
              System.out.println( e ) ;
           }
           finally
           {
              if( socket != null )
                 socket.close() ;
           }
       }

   }

   public static void read_always(){
       DatagramSocket socket = null ;
       while(true){

           try
           {
               // Convert the arguments first, to ensure that they are valid
               String s_json;
               DatagramPacket packet;
               byte [] data;
               InetAddress host =  getHost();
               int port = getPort();
               s_json =  "{\"id\":  \"id\", \"user\": \"equis\",\"text\": \"ejemplo\",\"action\": \"f\"}";
               data = s_json.getBytes() ;
               // Construct the socket
               socket = new DatagramSocket() ;
               packet = new DatagramPacket( data, data.length, host, port) ;
               // Send it
               socket.send( packet ) ;
               // Set a receive timeout, 2000 milliseconds
               socket.setSoTimeout( 10000 ) ;
              // Prepare the packet for receive
              packet.setData( new byte[PACKETSIZE] ) ;
              // Wait for a response from the server
              socket.receive( packet ) ;
              // Print the response

             /* String answer = new String(packet.getData());
              if(answer.equals("exit")){
                  break;
              }*/
              System.out.println( new String(packet.getData()) ) ;
          }
          catch( Exception e )
          {
             System.out.println( e ) ;
          }
          finally
          {
             if( socket != null )
                socket.close() ;
          }
      }

   }



}
