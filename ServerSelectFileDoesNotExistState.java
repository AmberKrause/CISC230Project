public class ServerSelectFileDoesNotExistState extends MessageState
{
	/*
		Amber Krause
		December 15, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerSelectFileDoesNotExistState
		dialog box that notifies the user that the selected file
		does not exist.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Class variables:
			instance
				the ServerSelectFileDoesNotExistState
				object.

		Constructors:
			ServerSelectFileDoesNotExistState()
				creates a new ServerSelectFileDoesNotExistState
				object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launch the dialog box and return the next NavigationState.

		Modification History:
			December 15, 2016
				original version.
	*/

	private enum StateName
	{
		OK;
	} //enum StateName

	private static ServerSelectFileDoesNotExistState	instance = new ServerSelectFileDoesNotExistState();

	private ServerSelectFileDoesNotExistState()
	{
		super(StateName.values());
	} //constructor

	public static ServerSelectFileDoesNotExistState getInstance()
	{
		return ServerSelectFileDoesNotExistState.instance;
	} //getInstance

	public NavigationState execute()
	{
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case OK:	{ nextState = ServerSelectFileState.getInstance(); break; }
			default:	{ throw new IllegalArgumentException("ServerSelectFileDoesNotExistState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerSelectFileDoesNotExistState