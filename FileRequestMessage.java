import java.io.*;

public class FileRequestMessage extends Message
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models a message that requests a file.
		On the server side, the get method will send the
		file back to the client.

		Class variables:
			serialVersionUID
				a serialVersionUID used for serialization.

			file
				the File in the shared directory to which the
				file should be copied. This File is created
				with a relative path.

		Constructors:
			FileRequestMessage(File file)
				creates a new FileRequestMessage object set
				to the value of the parameter.

		Methods:
			get(File directoryOfSharedFiles, OutputStream outputStream)
				sends a File to the client who requested it.

		Modification History:
			December 6, 2016
				original version.
	*/

	private static final long	serialVersionUID = 1;

	private File				file;

	public FileRequestMessage(File file)
	{
		if(file == null) throw new IllegalArgumentException("FileRequestMessage.constructor: parameter is null");
		this.file = file;
	} //constructor

	public void get(File directoryOfSharedFiles, OutputStream outputStream) throws IOException
	{
		//send the file from the directory of shared files to the client
		if(directoryOfSharedFiles == null) throw new IllegalArgumentException("FileRequestMessage.get: File parameter is null");
		if(outputStream == null) throw new IllegalArgumentException("FileRequestMessage.get: OutputStream parameter is null");
		BinaryFileCopier	fileCopier;
		File				fileToBeCopied;
		FileInputStream		inputStream;
		fileCopier = new BinaryFileCopier();
		fileToBeCopied = new File(directoryOfSharedFiles, file.toString());
		inputStream = new FileInputStream(fileToBeCopied);
		fileCopier.copyFile(inputStream, outputStream, 10000);
		outputStream.close();
		inputStream.close();
		System.out.println("Sent the requested file to a client: " + file);
	} //get

} //class FileRequestMessage
