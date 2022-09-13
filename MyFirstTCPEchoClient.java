import java.net.*;  // for Socket
import java.util.*;
import java.io.*;   // for IOException and Input/OutputStream

public class MyFirstTCPEchoClient {

  public static void main(String[] args) throws IOException {

    if ((args.length < 1) || (args.length > 2))  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Server> [<Port>]");
    
    // Server name or IP address
    String server = args[0];
    
    // Declare byte buffer for future use
    byte[] byteBuffer = null;

    // user input process
    boolean numberSelected = false;
    Scanner userInput = new Scanner(System.in);
    while (numberSelected == false) {
      System.out.print("Input a number:  ");
      String userString = userInput.nextLine();
      try {
        long userNum = Long.parseLong(userString);
        numberSelected = true;
        // convert string to bytes in UTF-16
        byteBuffer = userString.getBytes("UTF-16");
        if (userNum > Math.pow(2, 32) - 1) {
          System.out.println("Invalid input! Enter a number less than 4,294,967,296.");
          numberSelected = false;
        }
        if (userNum < 0) {
          System.out.println("Invalid input! Enter a non-negative number.");
          numberSelected = false;
        }
      }
      catch (NumberFormatException e) {
        System.out.println("Invalid input! Enter a valid integer.");
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
    long startTime = System.currentTimeMillis();

    // Receive the same string back from the server

    // Total bytes received so far
    int totalBytesRcvd = 0;
    // Bytes received in last read
    int bytesRcvd;

    // while the amt of received bytes doesn't exceed the buffer length
    while (totalBytesRcvd < byteBuffer.length) {
      // read data to byte buffer, until end of buffer reached
      if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,  
                        byteBuffer.length - totalBytesRcvd)) == -1)
        throw new SocketException("Connection close prematurely");
      // add bytes received in last read to total
      totalBytesRcvd += bytesRcvd;
    }
    long endTime = System.currentTimeMillis();
    
    // calculate sending/receiving time elapsed
    long elapsedTime = endTime - startTime;

    System.out.println("Received Message:  " + new String(byteBuffer));
    System.out.println("Time Elapsed (ms): " + elapsedTime);

    // Close the socket and its streams
    socket.close();
  }
}