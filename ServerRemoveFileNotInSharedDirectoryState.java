public class ServerRemoveFileNotInSharedDirectoryState extends MessageState
{
	/*
		Amber Krause
		December 4, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerRemoveFileNotInSharedDirectoryState
		dialog box that notifies the user that the selected file
		to remove is outside the shared directory.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Class variables:
			instance
				the ServerRemoveFileNotInSharedDirectoryState
				object.

		Constructors:
			ServerRemoveFileNotInSharedDirectoryState()
				creates a new ServerRemoveFileNotInSharedDirectoryState
				object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launch the dialog box and return the next NavigationState.

		Modification History:
			December 4, 2016
				original version.
	*/

	private enum StateName
	{
		OK;
	} //enum StateName

	private static ServerRemoveFileNotInSharedDirectoryState	instance = new ServerRemoveFileNotInSharedDirectoryState();

	private ServerRemoveFileNotInSharedDirectoryState()
	{
		super(StateName.values());
	} //constructor

	public static ServerRemoveFileNotInSharedDirectoryState getInstance()
	{
		return ServerRemoveFileNotInSharedDirectoryState.instance;
	} //getInstance

	public NavigationState execute()
	{
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case OK:	{ nextState = ServerRemoveFileState.getInstance(); break; }
			default:	{ throw new IllegalArgumentException("ServerRemoveFileNotInSharedDirectoryState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerRemoveFileNotInSharedDirectoryState