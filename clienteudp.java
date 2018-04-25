import java.net.* ;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class clienteudp
{
   private final static int PACKETSIZE = 100 ;

   public static void main( String args[] )
   {

      DatagramSocket socket = null ;

      try
      {
         // Convert the arguments first, to ensure that they are valid
         InetAddress host = InetAddress.getByName("127.0.0.1");
         int port= 8888;

         // Construct the socket
         socket = new DatagramSocket() ;
         System.out.println("Enter username: ");
             BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
             String username = bufferRead.readLine();

             System.out.println(username);

        while(true){

            //despues de que ingreso el nombre de usuario se le pregunta que accion quiere realizar
            //Poner un case, para  formar el string dependiendo, los campos que no se utilicen como texto, envias vacios

             // Construct the datagram packet

             String s_json =  "{\"id\":  \"new_id\", \"user\": \"new_user\",\"text\": \"new_text\",\"action\": \"a\"}";
             byte [] data = s_json.getBytes() ;
             DatagramPacket packet = new DatagramPacket( data, data.length, host, port ) ;

             // Send it
             socket.send( packet ) ;

             // Set a receive timeout, 2000 milliseconds
             socket.setSoTimeout( 2000 ) ;

             // Prepare the packet for receive
             packet.setData( new byte[PACKETSIZE] ) ;

             // Wait for a response from the server
             socket.receive( packet ) ;

             // Print the response
             System.out.println( new String(packet.getData()) ) ;
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
