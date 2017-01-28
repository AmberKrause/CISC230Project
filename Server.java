import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.*;

public class Server implements Runnable
{
	/*
		Amber Krause
		November 17, 2016
		CISC 230-02
		Doctor Jarvis

		This class models a server.

		Enumeration types:
			ServerCommand
				used to start or stop the server. See the method
				command(ServerCommand command).

		Class variables:
			serverIsRunning
				AtomicBoolean that indicates whether the server is running.

			serverSocket
				ServerSocket used by the server.

		Constructors:
			Server()
				creates Server object with an automatically allocated port
				number.

			Server(int port)
				creates Server object using the port indicated by the
				parameter. If the parameter is 0, the port number is
				automatically allocated.

		Methods:
			getPort()
				returns integer representing the port used by the server.

			getServerSocket()
				private accessor for serverSocket.

			log(String message)
				prints a log message.

			log(Exception exception)
				prints a log exception.

			setServerIsRunning(boolean value)
				private mutator for serverIsRunning.

			serverIsRunning()
				accessor for serverIsRunning.

			command(ServerCommand command) throws UnknownHostException, IOException, InterruptedException
				synchronized method that executes the passed ServerCommand. StartServer will run the
				server in a new thread if it is not already running. StopServer
				will stop the server if it is currently running.

			run()
				runs the server.

			handleConnection(InputStream inputStream, OutputStream outputStream)
				handles the connection made with the client with the passed
				inputStream and outputStream.

		Modification History:
			November 17, 2016
				original version.

			November 21, 2016
				added handleConnection(InputStream inputStream, OutputStream outputStream)
				method and inner class ConnectionManager.
	*/

	public enum ServerCommand
	{
		StartServer,
		StopServer;
	} //enum ServerCommand

	private AtomicBoolean	serverIsRunning;

	private ServerSocket	serverSocket;

	public Server() throws IOException
	{
		this(0);
	} //Server()

	public Server(int port) throws IOException
	{
		//create Server object using the port indicated by the parameter
		//if the parameter is 0, the port number is automatically allocated
		if(port < 0 || port > 65535) throw new IllegalArgumentException("Server.constructor: Port must be in the range of 0 to 65,535: " + port);
		serverIsRunning = new AtomicBoolean();
		serverSocket = new ServerSocket(port);
	} //Server(int port)

	public int getPort()
	{
		return getServerSocket().getLocalPort();
	} //getPort

	private ServerSocket getServerSocket()
	{
		return serverSocket;
	} //getServerSocket

	public void log(String message)
	{
		if(message != null) System.out.println(message);
	} //log(String message)

	public void log(Exception exception)
	{
		if(exception != null) System.out.println("An exception was raised: " + exception);
	} //log(Exception exception)

	private void setServerIsRunning(boolean value)
	{
		serverIsRunning.set(value);
	} //serServerIsRunning(boolean value)

	public boolean serverIsRunning()
	{
		return serverIsRunning.get();
	} //serverIsRunning()

	public synchronized void command(ServerCommand command) throws UnknownHostException, IOException, InterruptedException
	{
		//execute ServerCommand
		if(command == null) throw new IllegalArgumentException("Server.command: parameter is null");
		switch(command)
		{
			case StartServer:
			{
				//run server in new thread if not currently running
				if(!serverIsRunning())
				{
					new Thread(this).start();
				} //if
				break;
			}
			case StopServer:
			{
				//stop server if currently running
				if(serverIsRunning())
				{
					setServerIsRunning(false);
					Thread.sleep(1000);
					new Socket(InetAddress.getLocalHost(), getPort());
					Thread.sleep(1000);
				} //if
				break;
			}
			default: { throw new IllegalArgumentException("Server.command: command not listed"); }
		} //switch
	} //command(ServerCommand command)

	public void run()
	{
		//run server
		Socket socket;
		try
		{
			setServerIsRunning(true);
			log("The server is running");
			while(serverIsRunning())
			{
				socket = getServerSocket().accept();
				log("Received an object");
				if(serverIsRunning())
				{
					(new Thread(new ConnectionManager(socket))).start();
				} //if
			} //while
			setServerIsRunning(false);
			log("The server has stopped");
		}
		catch(IOException e) { throw new RuntimeException(e); }
	} //run

	public void handleConnection(InputStream inputStream, OutputStream outputStream) throws Exception
	{
		//handle the connection
		log((((ObjectInputStream)inputStream).readObject()).toString());
	} //handleConnection

	private class ConnectionManager implements Runnable
	{
		/*
			Amber Krause
			November 21, 2016
			CISC 230-02
			Doctor Jarvis

			This class manages a connection using the passed socket.

			Class variables:
				socket
					the socket.

			Constructors:
				ConnectionManager(Socket socket)
					creates a new ConnectionManager object that will
					use the passed socket.

			Methods:
				run()
					calls handleConnection(socket.getInputStream(), socket.getOutputStream())
					and then closes the socket

			Modification History:
				November 21, 2016
					original version.
		*/

		private Socket	socket;

		public ConnectionManager(Socket socket)
		{
			this.socket = socket;
		} //constructor

		public void run()
		{
			//call handleConnection(socket.getInputStream(), socket.getOutputStream())
			//and then close the socket
			try
			{
				Server.this.handleConnection(socket.getInputStream(), socket.getOutputStream());
			} //try
			catch(Exception e)
			{
				log(e);
				log("Error occurred while calling the handleConnection(InputStream inputStream, OutputStream outputStream) method");
			} //catch
			finally
			{
				try
				{
					socket.close();
				} //try
				catch(Exception e)
				{
					log(e);
					log("Error occurred while closing the socket");
				} //catch
			} //finally
		} //run

	} //class ConnectionManager

} //class Server