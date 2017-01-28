public class ClientGetOrQuitState extends MessageState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientGetOrQuitState dialog box.

		Class variables:
			instance
				the ClientGetOrQuitState object.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Constructors:
			ClientGetOrQuitState()
				creates a new ClientGetOrQuitState object.

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
		GetAFile,
		Quit;
	} //enum StateName

	private static ClientGetOrQuitState	instance = new ClientGetOrQuitState();

	private ClientGetOrQuitState()
	{
		super(StateName.values());
	} //constructor

	public static ClientGetOrQuitState getInstance()
	{
		return ClientGetOrQuitState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launches the dialog box and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case GetAFile:
			{
				nextState = ClientSelectFileState.getInstance();
				break;
			} //case GetAFile
			case Quit:	{ nextState = null; break; }
			default:	{ throw new IllegalArgumentException("ClientGetOrQuitState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ClientGetOrQuitStateState