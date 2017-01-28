import java.io.*;

public class ServerBeginState extends MessageState
{
	/*
		Amber Krause
		November 22, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerBeginState dialog box.

		Class variables:
			instance
				the ServerBeginState object.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Constructors:
			ServerBeginState()
				creates a new ServerBeginState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launch the dialog box and return the next NavigationState.

			getUnicastServer()
				accessor for server.

		Modification History:
			November 22, 2016
				original version.

			November 28, 2016
				implemented the Singleton design pattern by adding
				the instance variable and getInstance(). Also
				added execute().

			November 29, 2016
				added instance variable server and accessor
				getUnicastServer(). Implemented the
				functionality of execute().

			December 8, 2016
				removed the Server instance variable and moved it to
				the ServerSelectDirectoryState class.
	*/

	private enum StateName
	{
		Start,
		Quit;
	} //enum StateName

	private static ServerBeginState	instance = new ServerBeginState();

	private ServerBeginState()
	{
		super(StateName.values());
	} //constructor

	public static ServerBeginState getInstance()
	{
		return ServerBeginState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launches the dialog box and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case Start:
			{
				nextState = ServerSelectDirectoryState.getInstance();
				break;
			} //case Start
			case Quit:
			{
				if(ServerSelectDirectoryState.getInstance().getHelloMessage() != null)
				{
					ServerSelectDirectoryState.getInstance().getHelloMessage().command(Hello.Command.Quit);
				} //if
				nextState = null;
				break;
			} //case Quit
			default:	{ throw new IllegalArgumentException("ServerBeginState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerBeginState
