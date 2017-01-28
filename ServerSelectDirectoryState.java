import java.io.*;

public class ServerSelectDirectoryState extends FileChooserState
{
	/*
		Amber Krause
		November 28, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ServerSelectDirectoryState file chooser.

		Class variables:
			instance
				the ServerSelectDirectoryState object.

			timeOfLastUpdate
				a long indicating the time when the directory was
				last updated.

			messageServer
				a Server type variable that will hold a MessageServer
				object.

		Enumeration types:
			StateName
				represents the buttons in the file chooser.

		Constructors:
			ServerSelectDirectoryState()
				creates a new ServerSelectDirectoryState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the file chooser and return the next NavigationState.

			getTimeOfLastUpdate()
				accessor for timeOfLastUpdate

			updateTimeOfLastUpdate()
				mutator for timeOfLastUpdate. Sets the instance variable to
				the current time.

			getUnicastServer()
				accessor for messageServer.

		Modification History:
			November 28, 2016
				original version.

			November 30, 2016
				added timeOfLastUpdate, getTimeOfLastUpdate(), and
				updateTimeOfLastUpdate().

			December 5, 2016
				added the instance variable helloMessage and its
				accessor getHelloMessage(). Modified execute().

			December 8, 2016
				added the messageServer instance variable and its accessor.
	*/

	private enum StateName
	{
		Select,
		Cancel;
	} //enum StateName

	private static ServerSelectDirectoryState	instance = new ServerSelectDirectoryState();

	private long								timeOfLastUpdate;

	private Hello								helloMessage;

	private Server								messageServer;

	private ServerSelectDirectoryState()
	{
		super(StateName.values());
		timeOfLastUpdate = 0;
		helloMessage = null;
	} //constructor

	public static ServerSelectDirectoryState getInstance()
	{
		return ServerSelectDirectoryState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the file chooser and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		int				port;
		SharedFiles		sharedFiles;
		buttonClicked = StateName.valueOf(super.showDialog(null, null));
		switch(buttonClicked)
		{
			case Select:
			{
				updateTimeOfLastUpdate();
				sharedFiles = new SharedFiles(super.getSelectedFile());
				//create the server
				port = Resources.getInstance().getUnicastPortNumber();
				try
				{
					messageServer = new MessageServer(sharedFiles, port);
					Resources.getInstance().setUnicastPortNumber(messageServer.getPort());
				} //try
				catch(IOException e) { throw new RuntimeException("ServerSelectDirectoryState.execute: The server could not be constructed: " + e); }
				//run the SharedFiles
				new Thread(sharedFiles).start();
				//create the Hello object
				try
				{
					helloMessage = new Hello(Resources.getInstance().getUserName(), Resources.getInstance().getMulticastGroup(), Resources.getInstance().getMulticastPortNumber(), Resources.getInstance().getUnicastPortNumber(), 10000, sharedFiles);
				} //try
				catch(Exception e) { throw new RuntimeException("ServerSelectDirectoryState.execute: could not create a Hello object: " + e.getMessage()); }
				nextState = ServerStartServingState.getInstance();
				break;
			} //case Select
			case Cancel:	{ nextState = ServerBeginState.getInstance(); break; }
			default:		{ throw new IllegalArgumentException("ServerSelectDirectoryState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

	public long getTimeOfLastUpdate()
	{
		return timeOfLastUpdate;
	} //getTimeOfLastUpdate

	public void updateTimeOfLastUpdate()
	{
		timeOfLastUpdate = System.currentTimeMillis();
	} //updateTimeOfLastUpdate

	public Hello getHelloMessage()
	{
		return helloMessage;
	} //getHelloMessage

	public Server getUnicastServer()
	{
		return messageServer;
	} //getUnicastServer

} //class ServerSelectDirectoryState