public class ServerSelectFileState extends FileChooserState
{
	/*
		Amber Krause
		November 28, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerSelectFileState file chooser.

		Class variables:
			instance
				the ServerSelectFileState object.

		Enumeration types:
			StateName
				represents the buttons in the file chooser.

		Constructors:
			ServerSelectFileState()
				creates a new ServerSelectFileState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the file chooser and returns the next NavigationState.

		Modification History:
			November 28, 2016
				original version.
	*/

	private enum StateName
	{
		Open,
		Cancel;
	} //enum StateName

	private static ServerSelectFileState	instance = new ServerSelectFileState();

	private ServerSelectFileState()
	{
		super(StateName.values());
	} //constructor

	public static ServerSelectFileState getInstance()
	{
		return ServerSelectFileState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the file chooser and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog(null, null));
		switch(buttonClicked)
		{
			case Open:
			{
				if(this.getSelectedFile().exists())
				{
					nextState = ServerSaveFileState.getInstance();
				} //if
				else
				{
					nextState = ServerSelectFileDoesNotExistState.getInstance();
				} //else
				break;
			} //case Open
			case Cancel:	{ nextState = ServerStopServerState.getInstance(); break; }
			default:		{ throw new IllegalArgumentException("ServerSelectFileState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerSelectFileState