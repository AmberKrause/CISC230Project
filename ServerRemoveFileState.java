import java.io.*;

public class ServerRemoveFileState extends FileChooserState
{
	/*
		Amber Krause
		November 28, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerRemoveFileState file chooser.
		The user may select a file from the shared directory
		to remove.

		Class variables:
			instance
				the ServerRemoveFileState object.

		Enumeration types:
			StateName
				represents the buttons in the file chooser.

		Constructors:
			ServerRemoveFileState()
				creates a new ServerRemoveFileState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the file chooser and return the next NavigationState.

		Modification History:
			November 28, 2016
				original version.

			November 29, 2016
				implemented the functionality of execute().

			November 30, 2016
				modified execute() so that the timeOfLastUpdate is updated
				after a file is removed.

			December 4, 2016
				modified execute() so that the
				ServerRemoveFileNotInSharedDirectoryState dialog is shown
				when the selected file is not within the shared directory.
	*/

	private enum StateName
	{
		Delete,
		Cancel;
	} //enum StateName

	private static ServerRemoveFileState	instance = new ServerRemoveFileState();

	private ServerRemoveFileState()
	{
		super(StateName.values());
	} //constructor

	public static ServerRemoveFileState getInstance()
	{
		return ServerRemoveFileState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the file chooser and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		String			sharedDirectoryPath;
		String			targetPath;
		buttonClicked = StateName.valueOf(super.showDialog(ServerSelectDirectoryState.getInstance().getSelectedFile(), null));
		switch(buttonClicked)
		{
			case Delete:
			{
				//check if file is outside the shared directory
				try
				{
					sharedDirectoryPath = ServerSelectDirectoryState.getInstance().getSelectedFile().getCanonicalPath();
					targetPath = this.getSelectedFile().getCanonicalPath();
				} //try
				catch(IOException e) { throw new RuntimeException(e.getMessage()); }
				if(targetPath.indexOf(sharedDirectoryPath) < 0)
				{
					nextState = ServerRemoveFileNotInSharedDirectoryState.getInstance();
				} //if
				else
				{
					//delete the file
					this.getSelectedFile().delete();
					ServerSelectDirectoryState.getInstance().updateTimeOfLastUpdate();
					nextState = ServerStopServerState.getInstance();
				} //else
				break;
			} //case Delete
			case Cancel:	{ nextState = ServerStopServerState.getInstance(); break; }
			default:		{ throw new IllegalArgumentException("ServerRemoveFileState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerRemoveFileState