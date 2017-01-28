public class ClientFileSaveErrorState extends MessageState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientFileSaveErrorState dialog box.

		Class variables:
			instance
				the ClientFileSaveErrorState object.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Constructors:
			ClientFileSaveErrorState()
				creates a new ClientFileSaveErrorState object.

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

	private static ClientFileSaveErrorState	instance = new ClientFileSaveErrorState();

	private ClientFileSaveErrorState()
	{
		super(StateName.values());
	} //constructor

	public static ClientFileSaveErrorState getInstance()
	{
		return ClientFileSaveErrorState.instance;
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
				nextState = ClientSaveFileState.getInstance();
				break;
			} //case OK
			default:	{ throw new IllegalArgumentException("ClientFileSaveErrorState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ClientFileSaveErrorStateState