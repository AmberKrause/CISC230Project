public class ClientFileUnavailableErrorState extends MessageState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientFileUnavailableErrorState dialog box.

		Class variables:
			instance
				the ClientFileUnavailableErrorState object.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Constructors:
			ClientFileUnavailableErrorState()
				creates a new ClientFileUnavailableErrorState object.

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

	private static ClientFileUnavailableErrorState	instance = new ClientFileUnavailableErrorState();

	private ClientFileUnavailableErrorState()
	{
		super(StateName.values());
	} //constructor

	public static ClientFileUnavailableErrorState getInstance()
	{
		return ClientFileUnavailableErrorState.instance;
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
				nextState = ClientGetOrQuitState.getInstance();
				break;
			} //case OK
			default:	{ throw new IllegalArgumentException("ClientFileUnavailableErrorState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ClientFileUnavailableErrorStateState