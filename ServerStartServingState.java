import java.net.*;

public class ServerStartServingState extends MessageState
{
	/*
		Amber Krause
		November 24, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerStartServingState dialog box.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Class variables:
			instance
				the ServerStartServingState object.

		Constructors:
			ServerStartServingState()
				creates a new ServerStartServingState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the dialog box and returns the next NavigationState.
				Starts the server and the Hello messages if StartServer
				is selected. Sends Goodbye messages if Quit is selected.

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
		StartServer,
		Quit;
	} //enum StateName

	private static ServerStartServingState	instance = new ServerStartServingState();

	private ServerStartServingState()
	{
		super(StateName.values());
	} //constructor

	public static ServerStartServingState getInstance()
	{
		return ServerStartServingState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launches the dialog box and return the next NavigationState
		StateName		buttonClicked;
		Goodbye			goodbye;
		NavigationState	nextState;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case StartServer:
			{
				//start the server
				try
				{
					ServerSelectDirectoryState.getInstance().getUnicastServer().command(Server.ServerCommand.StartServer);
					ServerSelectDirectoryState.getInstance().getHelloMessage().command(Hello.Command.Start);
					new Thread(ServerSelectDirectoryState.getInstance().getHelloMessage()).start();
				} //try
				catch(Exception e)
				{
					throw new RuntimeException("ServerStartServingState.execute: The server was not started: " + e);
				} //catch
				nextState = ServerStopServerState.getInstance();
				break;
			} //case StartServer
			case Quit:
			{
				ServerSelectDirectoryState.getInstance().getHelloMessage().command(Hello.Command.Quit);
				try
				{
					goodbye = new Goodbye(Resources.getInstance().getUserName(), Resources.getInstance().getMulticastGroup(), Resources.getInstance().getMulticastPortNumber());
				} //try
				catch(Exception e) { throw new RuntimeException("ServerStartServingState.execute: could not create a Goodbye object: " + e.getMessage()); }
				for(int i = 0; i < 3; i++)
				{
					goodbye.run();
					try { Thread.sleep(1000); }
					catch(Exception e) {}
				} //for
				nextState = null;
				break;
			} //case Quit
			default:			{ throw new IllegalArgumentException("ServerStartServingState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ServerStartServingState