import java.net.* ;


public class clienteudp
{
   private final static int PACKETSIZE = 100 ;

   public static void main( String args[] )
   {
      // Check the arguments
      if( args.length != 2 )
      {
         System.out.println( "Sintaxis: java #nombrearchivo host port" ) ;
         return ;
      }

      DatagramSocket socket = null ;

      try
      {
         // Convert the arguments first, to ensure that they are valid
         InetAddress host = InetAddress.getByName( args[0] ) ;
         int port= Integer.parseInt( args[1] ) ;

         // Construct the socket
         socket = new DatagramSocket() ;

         // Construct the datagram packet
         String s =  "{\"id\":  \"new_id\", \"user\": \"new_user\",\"text\": \"new_text\",\"time\": \"new_time\"}";
         byte [] data = s.getBytes() ;
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
