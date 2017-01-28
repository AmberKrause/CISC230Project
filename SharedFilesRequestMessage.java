import java.io.*;

public class SharedFilesRequestMessage extends Message
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models a message that requests a shared files.
		On the server side, the get method sends an array
		of the shared files to the client.

		Class variables:
			serialVersionUID
				a serialVersionUID used for serialization.

		Constructors:
			SharedFilesRequestMessage()
				creates a new SharedFilesRequestMessage set
				to the value of the parameter.

		Methods:
			get(SharedFiles sharedFiles, OutputStream outputStream)
				sends a file array of the relative paths of the shared
				files to the client who requested it.

		Modification History:
			December 6, 2016
				original version.
	*/

	private static final long	serialVersionUID = 1;

	public SharedFilesRequestMessage() {}

	public void get(SharedFiles sharedFiles, OutputStream outputStream) throws IOException, ClassNotFoundException
	{
		//send a file array of the relative paths of the shared
		//files to the client who requested it
		ObjectOutputStream	oos;
		if(sharedFiles == null) throw new IllegalArgumentException("SharedFilesRequestMessage.get: parameter is null");
		if(outputStream == null) throw new IllegalArgumentException("SharedFilesRequestMessage.get: OutputStream parameter is null");
		oos = new ObjectOutputStream(outputStream);
		oos.writeObject(sharedFiles.toArray());
		oos.close();
		System.out.println("Sent the shared files to a client");
	} //get

} //class SharedFilesRequestMessage