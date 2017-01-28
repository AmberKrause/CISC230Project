public class ServerSaveOutsideSharedDirectoryState extends MessageState
{
	/*
		Amber Krause
		December 4, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerSaveOutsideSharedDirectoryState
		dialog box that notifies the user that the selected save
		location is outside the shared directory.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Class variables:
			instance
				the ServerSaveOutsideSharedDirectoryState object.

		Constructors:
			ServerSaveOutsideSharedDirectoryState()
				creates a new ServerSaveOutsideSharedDirectoryState
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

	private static ServerSaveOutsideSharedDirectoryState	instance = new ServerSaveOutsideSharedDirectoryState();

	private ServerSaveOutsideSharedDirectoryState()
	{
		super(StateName.values());
	} //constructor

	public static ServerSaveOutsideSharedDirectoryState getInstance()
	{
		return ServerSaveOutsideSharedDirectoryState.instance;
	} //getInstance

	public NavigationState execute()
	{
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case OK:	{ nextState = ServerSaveFileState.getInstance(); break; }
			default:	{ throw new IllegalArgumentException("ServerSaveOutsideSharedDirectoryState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerSaveOutsideSharedDirectoryState