import java.io.*;
import java.net.*;

public class Goodbye extends HelloGoodbye
{
	/*
		Amber Krause
		December 5, 2016
		CISC 230-02
		Doctor Jarvis

		This class sends out Goodbye messages.

		Class variables:
			serialVersionUID
				a serialVersionUID used for serialization.

		Constructors:
			Goodbye(String userName, InetAddress multicastGroup, int multicastPort)
				creates a new Goodbye object set to the values of the parameters.

		Methods:
			run()
				sends a serialized version of this object.

		Modification History:
			December 5, 2016
				original version.
	*/

	private static final long	serialVersionUID = 1;

	public Goodbye(String userName, InetAddress multicastGroup, int multicastPort) throws IOException
	{
		super(userName, multicastGroup, multicastPort);
	} //constructor

	public void run()
	{
		//send a serialized version of this object
		byte[]					buffer;
		ByteArrayOutputStream	byteArrayOutputStream;
		ObjectOutputStream		objectOutputStream;
		DatagramPacket			packet;
		try { this.sendMe(); }
		catch(Exception e) { throw new RuntimeException(e.getMessage()); }
	} //run

/*
	public static void main(String[] args) throws IOException
	{
		//run a Goodbye object
		Goodbye	goodbye;
		goodbye = new Goodbye("AmberKrause", InetAddress.getByName("228.5.6.7"), 8888);
		for(int i = 0; i < 3; i++)
		{
			new Thread(goodbye).start();
			try { Thread.sleep(1000); }
			catch(InterruptedException ie) {}
		} //for
	} //main
*/

} //class Goodbye