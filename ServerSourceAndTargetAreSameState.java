public class ServerSourceAndTargetAreSameState extends MessageState
{
	/*
		Amber Krause
		November 29, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerSourceAndTargetAreSameState dialog box
		that notifies the user that the selected source file and target
		file are the same.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Class variables:
			instance
				the ServerSourceAndTargetAreSameState object.

		Constructors:
			ServerSourceAndTargetAreSameState()
				creates a new ServerSourceAndTargetAreSameState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the dialog box and returns the next NavigationState.

		Modification History:
			November 29, 2016
				original version.
	*/

	private enum StateName
	{
		OK;
	} //enum StateName

	private static ServerSourceAndTargetAreSameState	instance = new ServerSourceAndTargetAreSameState();

	private ServerSourceAndTargetAreSameState()
	{
		super(StateName.values());
	} //constructor

	public static ServerSourceAndTargetAreSameState getInstance()
	{
		return ServerSourceAndTargetAreSameState.instance;
	} //getInstance

	public NavigationState execute()
	{
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case OK:	{ nextState = ServerSaveFileState.getInstance(); break; }
			default:	{ throw new IllegalArgumentException("ServerSourceAndTargetAreSameState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerSourceAndTargetAreSameState