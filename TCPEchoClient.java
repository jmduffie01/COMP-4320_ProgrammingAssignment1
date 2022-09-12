import java.net.*;  // for Socket
import java.util.Scanner;
import java.io.*;   // for IOException and Input/OutputStream

public class TCPEchoClient {

  public static void main(String[] args) throws IOException {

    if ((args.length < 1) || (args.length > 2))  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Server> [<Port>]");
    
    // Server name or IP address
    String server = args[0];
    
    // Convert input String to bytes using the default character encoding
    byte[] byteBuffer = args[1].getBytes();

    // user input process
    boolean numberSelected = false;
    Scanner userInput = new Scanner(System.in);
    while (numberSelected == false) {
      System.out.print("Input a number:  ");
      String userString = userInput.nextLine();
      try {
        Integer.parseInt(userString);
        numberSelected = true;
        byteBuffer = userString.getBytes();
      }
      catch (NumberFormatException e) {
        System.out.println("Invalid! Input valid integer (max: 2,147,483,647)."); // FIX THIS

      }
    }
    userInput.close();

    // Check for port parameter, and set port; if no port, set as 7
    int servPort = (args.length == 2) ? Integer.parseInt(args[1]) : 7;

    // Create socket that is connected to server on specified port
    Socket socket = new Socket(server, servPort);
    System.out.println("Connected to server...sending echo string");

    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();

    // Send the encoded string to the server
    out.write(byteBuffer);

    // Receive the same string back from the server

    // Total bytes received so far
    int totalBytesRcvd = 0;
    // Bytes received in last read
    int bytesRcvd;

    // while the amt of rcvd bytes doesn't exceed the buffer length
    while (totalBytesRcvd < byteBuffer.length) {
      // read data to byte buffer, until end of buffer reached
      if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,  
                        byteBuffer.length - totalBytesRcvd)) == -1)
        throw new SocketException("Connection close prematurely");
      // add bytes received in last read to total
      totalBytesRcvd += bytesRcvd;
    }

    System.out.println("Received: " + new String(byteBuffer));

    // Close the socket and its streams
    socket.close();
  }
}