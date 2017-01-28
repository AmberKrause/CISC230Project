import java.io.*;

public class ServerSaveFileState extends FileChooserState
{
	/*
		Amber Krause
		November 28, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerSaveFileState file chooser.
		The user may select a file to save to the shared
		directory.

		Class variables:
			instance
				the ServerSaveFileState object.

		Enumeration types:
			StateName
				represents the buttons in the file chooser.

		Constructors:
			ServerRemoveFileState()
				creates a new ServerSaveFileState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the file chooser and returns the next NavigationState.

		Modification History:
			November 28, 2016
				original version.

			November 29, 2016
				implemented the functionality of execute().

			November 30, 2016
				modified execute() so that the timeOfLastUpdate is updated
				after a file is added.

			December 4, 2016
				modified execute() so that the ServerTargetNotInSharedDirectoryState
				dialog is shown when the save location is not within
				the shared directory.
	*/

	private enum StateName
	{
		Save,
		Cancel;
	} //enum StateName

	private static ServerSaveFileState	instance = new ServerSaveFileState();

	private ServerSaveFileState()
	{
		super(StateName.values());
	} //constructor

	public static ServerSaveFileState getInstance()
	{
		return ServerSaveFileState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the file chooser and return the next NavigationState
		StateName			buttonClicked;
		BinaryFileCopier	fileCopier;
		InputStream			inputStream;
		NavigationState		nextState;
		OutputStream		outputStream;
		String				sharedDirectoryPath;
		File				sourceFile;
		File				targetFile;
		String				targetPath;
		buttonClicked = StateName.valueOf(super.showDialog(ServerSelectDirectoryState.getInstance().getSelectedFile(), ServerSelectFileState.getInstance().getSelectedFile()));
		switch(buttonClicked)
		{
			case Save:
			{
				sourceFile = ServerSelectFileState.getInstance().getSelectedFile();
				targetFile = this.getSelectedFile();
				//check if sourceFile and targetFile are the same
				if(sourceFile.equals(targetFile))
				{
					nextState = ServerSourceAndTargetAreSameState.getInstance();
				} //if
				else
				{
					//check if targetFile is outside the shared directory
					try
					{
						sharedDirectoryPath = ServerSelectDirectoryState.getInstance().getSelectedFile().getCanonicalPath();
						targetPath = targetFile.getCanonicalPath();
					} //try
					catch(IOException e) { throw new RuntimeException(e.getMessage()); }
					if(targetPath.indexOf(sharedDirectoryPath) < 0)
					{
						nextState = ServerSaveOutsideSharedDirectoryState.getInstance();
					} //if
					else
					{
						//check if targetFile already exists
						if(targetFile.exists())
						{
							nextState = ServerOverwriteExistingFileState.getInstance();
						} //if
						else
						{
							//copy the source file to the target file
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
						} //else
					} //else
				} //else
				break;
			} //case Save
			case Cancel:	{ nextState = ServerStopServerState.getInstance(); break; }
			default:		{ throw new IllegalArgumentException("ServerSaveFileState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerSaveFileState