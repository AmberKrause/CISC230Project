public class ClientFileNotInServerDirectoryState extends MessageState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientFileNotInServerDirectoryState dialog box.

		Class variables:
			instance
				the ClientFileNotInServerDirectoryState object.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Constructors:
			ClientFileNotInServerDirectoryState()
				creates a new ClientFileNotInServerDirectoryState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the dialog box and returns the next NavigationState.

		Modification History:
			December 6, 2016
				original version.
	*/

	private enum StateName
	{
		OK;
	} //enum StateName

	private static ClientFileNotInServerDirectoryState	instance = new ClientFileNotInServerDirectoryState();

	private ClientFileNotInServerDirectoryState()
	{
		super(StateName.values());
	} //constructor

	public static ClientFileNotInServerDirectoryState getInstance()
	{
		return ClientFileNotInServerDirectoryState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the dialog box and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case OK:
			{
				nextState = ClientSelectFileState.getInstance();
				break;
			} //case OK
			default:	{ throw new IllegalArgumentException("ClientFileNotInServerDirectoryState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ClientFileNotInServerDirectoryStateState