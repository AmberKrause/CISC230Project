public class ServerStopServerState extends MessageState
{
	/*
		Amber Krause
		November 24, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerStopServerState dialog box.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Class variables:
			instance
				the ServerStopServerState object.

		Constructors:
			ServerStopServingState()
				creates a new ServerStopServerState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the dialog box and returns the next NavigationState.
				Stops the server and the Hello messages if StopServer
				is selected.

		Modification History:
			November 24, 2016
				original version.

			November 28, 2016
				implemented the Singleton design pattern by adding
				the instance class variable and getInstance(). Also
				added execute().

			December 5, 2016
				modified execute() to incorporate the use of the
				Hello class.
	*/

	private enum StateName
	{
		StopServer,
		AddFile,
		RemoveFile;
	} //enum StateName

	private static ServerStopServerState	instance = new ServerStopServerState();

	private ServerStopServerState()
	{
		super(StateName.values());
	} //constructor

	public static ServerStopServerState getInstance()
	{
		return ServerStopServerState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launches the dialog box and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case StopServer:
			{
				//stop the server
				try
				{
					ServerSelectDirectoryState.getInstance().getHelloMessage().command(Hello.Command.Stop);
					ServerSelectDirectoryState.getInstance().getUnicastServer().command(Server.ServerCommand.StopServer);
				} //try
				catch(Exception e)
				{
					throw new RuntimeException("ServerStopServerState.execute: The server was not stopped: " + e);
				} //catch
				nextState = ServerStartServingState.getInstance();
				break;
			}
			case AddFile:		{ nextState = ServerSelectFileState.getInstance(); break; }
			case RemoveFile:	{ nextState = ServerRemoveFileState.getInstance(); break; }
			default:			{ throw new IllegalArgumentException("ServerStopServerState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerStopServingState