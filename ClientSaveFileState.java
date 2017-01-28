import java.io.*;
import java.net.*;

public class ClientSaveFileState extends FileChooserState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientSaveFileState dialog box.

		Class variables:
			instance
				the ClientSaveFileState object.

		Enumeration types:
			StateName
				represents the buttons in the file chooser.

		Constructors:
			ClientSaveFileState()
				creates a new ClientSaveFileState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the file chooser and returns the next NavigationState.
				If a file is chosen to be saved, this method checks that the
				file still exists and downloads it from the server.

		Modification History:
			December 6, 2016
				original version.

			December 13, 2016
				modified the execute() method to download the file from the
				server.
	*/

	private enum StateName
	{
		Save,
		Cancel;
	} //enum StateName

	private static ClientSaveFileState	instance = new ClientSaveFileState();

	private ClientSaveFileState()
	{
		super(StateName.values());
	} //constructor

	public static ClientSaveFileState getInstance()
	{
		return ClientSaveFileState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the file chooser and return the next NavigationState
		String				absolutePath;
		StateName			buttonClicked;
		String				clientDirectoryPath;
		File				fileReceived;
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
		String				targetPath;
		buttonClicked = StateName.valueOf(super.showDialog(null, ClientSelectFileState.getInstance().getSelectedFile()));
		switch(buttonClicked)
		{
			case Save:
			{
				try
				{
					clientDirectoryPath = Resources.getInstance().getClientServerDirectory().getCanonicalPath();
					sourceFile = ClientSelectFileState.getInstance().getSelectedFile();
					targetFile = this.getSelectedFile();
					targetPath = targetFile.getCanonicalPath();
				} //try
				catch(IOException e) { throw new RuntimeException(e.getMessage()); }

				//check whether the selected file is in the server directory
				if(targetPath.indexOf(clientDirectoryPath) >= 0)
				{
					nextState = ClientFileSaveErrorState.getInstance();
				} //if
				else
				{
					try
					{
						//check whether the file to be copied still exists in the server directory
						serverAddress = ClientBeginState.getInstance().getFileNameToFileServerMap().getServerAddressFor(ClientSelectFileState.getInstance().getSelectedFile());
						if(serverAddress == null)
						{
							System.out.println("There is no server address associated with the selected file");
							nextState = ClientFileUnavailableErrorState.getInstance();
						} //if
						else
						{
							//check whether the selected file in the save location already exists
							if(targetFile.exists())
							{
								nextState = ClientOverwriteExistingFileState.getInstance();
							} //if
							else
							{
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
							} //else
						} //else
					} //try
					catch(Exception e)
					{
						System.out.println(e.getMessage());
						nextState = ClientFileUnavailableErrorState.getInstance();
					} //catch
				} //else
				break;
			} //case Save
			case Cancel:
			{
				nextState = ClientGetOrQuitState.getInstance();
				break;
			} //case Cancel
			default:	{ throw new IllegalArgumentException("ClientSaveFileState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ClientSaveFileState