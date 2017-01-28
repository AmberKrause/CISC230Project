import java.io.*;

public class MessageServer extends Server
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This server receives objects and handles them if they are of
		the FileRequestMessage or SharedFilesRequestMessage classes.

		Class variables:
			sharedFiles
				a SharedFiles object.

		Constructors:
			MessageServer(SharedFiles sharedFiles)
				creates a new MessageServer object set to the value
				of the parameter.

		Methods:
			getSharedFiles()
				accessor for sharedFiles.

			handleConnection(InputStream inputStream, OutputStream outputStream)
				determines whether the object received was of a
				FileRequestMessage or SharedFilesRequestMessage type.

		Modification History:
			December 6, 2016
				original version.

			December 13, 2016
				modified the handleConnection method to fix a bug that
				caused the socket to close.
	*/

	private SharedFiles	sharedFiles;

	public MessageServer(SharedFiles sharedFiles) throws IOException
	{
		this(sharedFiles, 0);
	} //MessageServer(SharedFiles sharedFiles)

	public MessageServer(SharedFiles sharedFiles, int port) throws IOException
	{
		super(port);
		if(sharedFiles == null) throw new IllegalArgumentException("MessageServer.constructor: SharedFiles parameter is null");
		this.sharedFiles = sharedFiles;
	} //MessageServer(SharedFiles sharedFiles, int port)

	public SharedFiles getSharedFiles()
	{
		return sharedFiles;
	} //getSharedFiles

	public void handleConnection (InputStream inputStream, OutputStream outputStream) throws IOException, ClassNotFoundException
	{
		//determine whether the object received was of a FileRequestMessage
		//or SharedFilesRequestMessage type
		if(inputStream == null) throw new IllegalArgumentException("MessageServer.handleConnection: InputStream parameter is null");
		if(outputStream == null) throw new IllegalArgumentException("MessageServer.handleConnection: OutputStream parameter is null");
		Object 				message;
	   	ObjectInputStream 	ois;

	   	ois = new ObjectInputStream (inputStream);
	   	message = ois.readObject();
	  	log("Read a " + message.getClass() + " object");

	  	//try to cast
	  	try { ((SharedFilesRequestMessage) message).get(getSharedFiles(), outputStream); }
	   	catch (ClassCastException cce)
	    {
	    	try { ((FileRequestMessage) message).get(getSharedFiles().getSharedDirectory(), outputStream); }
	      	catch (ClassCastException cce1) { throw new ClassCastException("Received unrecognized message type: " + message.getClass().getName() ); }
			catch(Exception e) { log("MessageServer.handleConnection: Could not send the file: " + e.getMessage()); }
		} //catch
		catch(Exception e1) { log("MessageServer.handleConnection: Could not send the shared files: " + e1.getMessage()); }

	    ois.close();
	} //handleConnection

} //class MessageServer