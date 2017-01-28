import java.io.*;

public class ServerOverwriteExistingFileState extends MessageState
{
	/*
		Amber Krause
		November 24, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerOverwriteExistingFileState dialog box.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Class variables:
			instance
				the ServerOverwriteExistingFileState object.

		Constructors:
			ServerOverwriteExistingFileState()
				creates a new ServerOverwriteExistingFileState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the dialog box and return the next NavigationState.

		Modification History:
			November 24, 2016
				original version.

			November 28, 2016
				implemented the Singleton design pattern by adding
				the instance class variable and getInstance(). Also
				added execute().

			November 29, 2016
				implemented the functionality of execute().

			November 30, 2016
				modified execute() so that the timeOfLastUpdate is updated
				after a file is added.

			December 15, 2016
				modified execute() so that a file may be downloaded
				from the server if "Yes" is selected.
	*/

	private enum StateName
	{
		Yes,
		No;
	} //enum StateName

	private static ServerOverwriteExistingFileState	instance = new ServerOverwriteExistingFileState();

	private ServerOverwriteExistingFileState()
	{
		super(StateName.values());
	} //constructor

	public static ServerOverwriteExistingFileState getInstance()
	{
		return ServerOverwriteExistingFileState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the dialog box and return the next NavigationState
		StateName			buttonClicked;
		BinaryFileCopier	fileCopier;
		String				fileName;
		InputStream			inputStream;
		String				newMessage; //dialog message
		OutputStream		outputStream;
		String				originalMessage;
		File				sourceFile;
		File				targetFile;
		NavigationState	nextState;
		originalMessage = super.getMessage();
		newMessage = originalMessage.trim();
		if(newMessage.charAt(newMessage.length() - 1) == '?')
		{
			newMessage = newMessage.substring(0, newMessage.length() - 1);
		} //if
		try
		{
			fileName = ServerSaveFileState.getInstance().getSelectedFile().getCanonicalPath();
			fileName = fileName.substring(ServerSaveFileState.getInstance().getSelectedFile().getParentFile().getCanonicalPath().length() + 1);
			newMessage = newMessage + ": " + fileName + "?";
			super.setMessage(newMessage);
		} //try
		catch(IOException e) {}
		buttonClicked = StateName.valueOf(super.showDialog());
		super.setMessage(originalMessage);
		switch(buttonClicked)
		{
			case Yes:
			{
				//copy the source file to the target file
				sourceFile = ServerSelectFileState.getInstance().getSelectedFile();
				targetFile = ServerSaveFileState.getInstance().getSelectedFile();
				try
				{
					fileCopier = new BinaryFileCopier();
					inputStream = new FileInputStream(sourceFile);
					outputStream = new FileOutputStream(targetFile);
					fileCopier.copyFile(inputStream, outputStream, 10000);
					inputStream.close();
					outputStream.close();
					ServerSelectDirectoryState.getInstance().updateTimeOfLastUpdate();
				} //try
				catch(Exception e)
				{
					throw new RuntimeException("ServerSaveFileState.execute: File could not be copied: " + e);
				} //catch
				nextState = ServerStopServerState.getInstance();
				break;
			}
			case No:	{ nextState = ServerSaveFileState.getInstance(); break; }
			default:	{ throw new IllegalArgumentException("ServerOverwriteExistingFileState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerOverwriteExistingFileState