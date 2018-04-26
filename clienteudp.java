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
         String s_json;
         DatagramPacket packet;
         byte [] data;
         // Construct the socket
         socket = new DatagramSocket() ;
         System.out.println("Enter username: ");
             BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
             String username = bufferRead.readLine();
             s_json =  "{\"id\":  \"\", \"user\": \""+username+"\",\"text\": \"\",\"action\": \"e\"}";
             data = s_json.getBytes() ;
             packet = new DatagramPacket( data, data.length, host, port ) ;
                     // Send it
             socket.send( packet ) ;
             //necesita poder recibir en caso de que el nombre de usuario este repetido
             // Set a receive timeout, 2000 milliseconds
 			socket.setSoTimeout( 5000 ) ;
 			// Prepare the packet for receive
 			packet.setData( new byte[PACKETSIZE] ) ;
 			// Wait for a response from the server
 			socket.receive( packet ) ;
 			// Print the response
 			System.out.println( new String(packet.getData()) ) ;

        //TODO Agregar validacion en caso de que se ingrese un nombre de usuarioya existente y se tenga que pedir nuevamente


        //while(true){
	    String option, m, recipient;
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
			s_json =  "{\"id\":  \""+id+"\", \"user\": \""+username+"\",\"text\": \""+m+"\",\"action\": \"a\"}";
			data = s_json.getBytes() ;
            packet = new DatagramPacket( data, data.length, host, port ) ;
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
            System.out.println("Type the message: ");
            System.out.print(">>");
            m= scanner.nextLine();
            System.out.println("Type the recipient: ");
            System.out.print(">>");
            recipient= scanner.nextLine();
            m = m + "," + recipient;
            s_json =  "{\"id\":  \""+id+"\", \"user\": \""+username+"\",\"text\": \""+m+"\",\"action\": \"c\"}";
            data = s_json.getBytes() ;
            packet = new DatagramPacket( data, data.length, host, port ) ;
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
