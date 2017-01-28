import java.io.*;
import java.util.*;

public class ClientBeginState extends MessageState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientBeginState dialog box.

		Class variables:
			fileNameToFileServerMap
				a FileNameToFileServerMap that tracks the server
				locations of the server files.

			instance
				the ClientBeginState object.

		Enumeration types:
			StateName
				represents the buttons in the dialog box.

		Constructors:
			ClientBeginState()
				creates a new ClientBeginState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the dialog box and returns the next NavigationState.

			getFileNameToFileServerMap()
				accessor for fileNameToFileServerMap.

		Modification History:
			December 6, 2016
				original version.

			December 12, 2016
				added the fileNameToFileServerMap and its accessor.
	*/

	private enum StateName
	{
		Start,
		Quit;
	} //enum StateName

	private FileNameToFileServerMap	fileNameToFileServerMap;

	private static ClientBeginState	instance = new ClientBeginState();

	private ClientBeginState()
	{
		super(StateName.values());
	} //constructor

	public static ClientBeginState getInstance()
	{
		return ClientBeginState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the dialog box and return the next NavigationState
		StateName		buttonClicked;
		NavigationState	nextState;
		File			serverDirectory;
		buttonClicked = StateName.valueOf(super.showDialog());
		switch(buttonClicked)
		{
			case Start:
			{
				//start the client
				serverDirectory = Resources.getInstance().getClientServerDirectory();
				if(serverDirectory.exists()) serverDirectory.renameTo(new File("ServerDirectory " + (new Date().getTime())));
				serverDirectory.mkdir();
				fileNameToFileServerMap = new FileNameToFileServerMap();
				new Thread(new Listener(serverDirectory, fileNameToFileServerMap)).start();
				nextState = ClientGetOrQuitState.getInstance();
				break;
			} //case Start
			case Quit:	{ nextState = null; break; }
			default:	{ throw new IllegalArgumentException("ClientBeginState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

	public FileNameToFileServerMap getFileNameToFileServerMap()
	{
		return fileNameToFileServerMap;
	} //getFileNameToFileServerMap

} //class ClientBeginState
