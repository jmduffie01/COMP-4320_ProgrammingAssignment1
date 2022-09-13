import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream

public class MyFirstTCPEchoServer {
  
  // Size of receive buffer
  private static final int BUFSIZE = 32;

  public static void main(String[] args) throws IOException {

    // Test for correct # of args
    if (args.length != 1)  
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    // Create a server socket to accept client connection requests
    ServerSocket servSock = new ServerSocket(servPort);

    // Size of received message
    int recvMsgSize;

    // Receive buffer
    byte[] byteBuffer = new byte[BUFSIZE];

    // Run forever, accepting and servicing connections
    for (;;) {

      // Get client connection
      Socket clntSock = servSock.accept();

      System.out.println("Handling client at " +
        clntSock.getInetAddress().getHostAddress() + " on port " +
             clntSock.getPort());

      InputStream in = clntSock.getInputStream();
      OutputStream out = clntSock.getOutputStream();

      // Receive until client closes connection, indicated by -1 return
      while ((recvMsgSize = in.read(byteBuffer)) != -1)
        out.write(byteBuffer, 0, recvMsgSize);

      // Close the socket.  We are done with this client!
      clntSock.close();
    }
  }
}