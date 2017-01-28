import java.io.*;
import java.net.*;

public class ClientOverwriteExistingFileState extends MessageState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientOverwriteExistingFileState dialog box.

		Class variables:
			instance
				the ClientOverwriteExistingFileState object.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Constructors:
			ClientOverwriteExistingFileState()
				creates a new ClientOverwriteExistingFileState object.


		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the dialog box and return the next NavigationState.
				Downloads the file if "Yes" is selected.

		Modification History:
			December 6, 2016
				original version.
	*/

	private enum StateName
	{
		Yes,
		No;
	} //enum StateName

	private static ClientOverwriteExistingFileState	instance = new ClientOverwriteExistingFileState();

	private ClientOverwriteExistingFileState()
	{
		super(StateName.values());
	} //constructor

	public static ClientOverwriteExistingFileState getInstance()
	{
		return ClientOverwriteExistingFileState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launches the dialog box and return the next NavigationState
		String				absolutePath;
		StateName			buttonClicked;
		BinaryFileCopier	fileCopier;
		FileOutputStream	fileOutputStream;
		ObjectInputStream	objectInputStream;
		ObjectOutputStream	objectOutputStream;
		FileRequestMessage	message;
		NavigationState		nextState;
		String				relativePath;
		InetSocketAddress	serverAddress;
		Socket				socket;
		File				sourceFile;
		File				targetFile;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case Yes:
			{
				try
				{
					sourceFile = ClientSelectFileState.getInstance().getSelectedFile();
					targetFile = ClientSaveFileState.getInstance().getSelectedFile();

					serverAddress = ClientBeginState.getInstance().getFileNameToFileServerMap().getServerAddressFor(ClientSelectFileState.getInstance().getSelectedFile());
					if(serverAddress == null)
					{
						System.out.println("There is no server address associated with the selected file");
						nextState = ClientFileUnavailableErrorState.getInstance();
					} //if
					//create the FileRequestMessage
					absolutePath = sourceFile.getCanonicalPath();
					relativePath = absolutePath.substring(sourceFile.getParentFile().getCanonicalPath().length() + 1);
					message = new FileRequestMessage(new File(relativePath));

					//send the message
					socket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
					objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objectOutputStream.writeObject(message);

					//receive the file
					fileCopier = new BinaryFileCopier();
					fileOutputStream = new FileOutputStream(targetFile);
					fileCopier.copyFile(socket.getInputStream(), fileOutputStream, 10000);
					fileOutputStream.close();
					objectOutputStream.close();
					socket.close();

					System.out.println("Downloaded the file");
					nextState = ClientGetOrQuitState.getInstance();
				} //try
				catch(IOException e)
				{
					System.out.println(e.getMessage());
					nextState = ClientFileUnavailableErrorState.getInstance();
				} //catch
				break;
			} //case Yes
			case No:	{ nextState = ClientGetOrQuitState.getInstance(); break; }
			default:	{ throw new IllegalArgumentException("ClientOverwriteExistingFileState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ClientOverwriteExistingFileStateState