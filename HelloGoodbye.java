import java.io.*;
import java.net.*;

public abstract class HelloGoodbye implements Serializable, Runnable
{
	/*
		Amber Krause
		December 5, 2016
		CISC 230-02
		Doctor Jarvis

		This class models Hello and Goodbye messages.

		Class variables:
			serialVersionUID
				a serialVersionUID used for serialization.

			multicastSocket
				a MulticastSocket.

			multicastGroup
				an InetAddress representing the IP address for the multicast.

			multicastPort
				an int port number for multicast.

			userName
				a String indicating the user name.

		Constructors:
			HelloGoodbye(String userName, InetAddress multicastGroup, int multicastPort)
				creates a new HelloGoodbye object set to the values of the parameters

		Methods:
			getUserName()
				accessor for userName

			toString()
				returns a String containing the class name and the user name

			sendMe()
				serializes the object and sends it

		Modification History:
			December 5, 2016
				original version.
	*/

	private static final long			serialVersionUID = 1;

	transient private MulticastSocket	multicastSocket;

	private InetAddress					multicastGroup;

	private int							multicastPort;

	private String						userName;

	public HelloGoodbye(String userName, InetAddress multicastGroup, int multicastPort) throws IOException
	{
		if(userName == null) throw new IllegalArgumentException("HelloGoodbye.constructor: String parameter is null");
		if(multicastGroup == null) throw new IllegalArgumentException("HelloGoodbye.constructor: InetAddress parameter is null");
		if(multicastPort < 0 || multicastPort > 65535) throw new IllegalArgumentException("HelloGoodbye.constructor: multicast port number must be at least 0 and not greater than 65,535: " + multicastPort);
		this.userName = userName;
		this.multicastGroup = multicastGroup;
		this.multicastPort = multicastPort;
		this.multicastSocket = new MulticastSocket(this.multicastPort);
	} //constructor

	public String getUserName()
	{
		return userName;
	} //getUserName

	public String toString()
	{
		//return a String containing the class name and the user name
		return this.getClass().getName() + " " + getUserName();
	} //toString

	public void sendMe() throws IOException
	{
		//serialize the object and send it
		byte[]					buffer;
		ByteArrayOutputStream	byteArrayOutputStream;
		ObjectOutputStream		objectOutputStream;
		DatagramPacket			packet;
		//create output streams
		byteArrayOutputStream = new ByteArrayOutputStream();
		objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		//serialize "this"
		objectOutputStream.writeObject(this);
		buffer = byteArrayOutputStream.toByteArray();
		objectOutputStream.close();
		//send the object
		packet = new DatagramPacket(buffer, buffer.length, this.multicastGroup, this.multicastPort);
		this.multicastSocket.send(packet);
	} //sendMe

} //class HelloGoodbye