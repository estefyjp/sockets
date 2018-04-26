import java.net.* ;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


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

        //while(true){
	    String option, m;
	    int id=0;
	    Scanner scanner= new Scanner(System.in);
	    System.out.println("--Welcome, select an option (type the letter)--");
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
			String s_json =  "{\"id\":  \""+id+"\", \"user\": \""+username+"\",\"text\": \""+m+"\",\"action\": \"a\"}";
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
		break;
		case "b":
		break;
		case "c":
		break;
		case "d":
		break;
		default: System.out.println("Select the correct option");
		break;

	    }
            //despues de que ingreso el nombre de usuario se le pregunta que accion quiere realizar
            //Poner un case, para  formar el string dependiendo, los campos que no se utilicen como texto, envias vacios

             // Construct the datagram packet

             //String s_json =  "{\"id\":  \"new_id\", \"user\": \"new_user\",\"text\": \"new_text\",\"action\": \"a\"}";

        //}
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
