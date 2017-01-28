import java.io.*;
import java.net.*;

public class ClientSelectFileState extends FileChooserState
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class models the ClientSelectFileState dialog box.

		Class variables:
			instance
				the ClientSelectFileState object.

		Enumeration types:
			StateName
				represents the buttons in the file chooser.

		Constructors:
			ClientSelectFileState()
				creates a new ClientSelectFileState object.

		Methods:
			getInstance()
				accessor for instance.

			execute()
				launches the file chooser and returns the next NavigationState.

		Modification History:
			December 6, 2016
				original version.
	*/

	private enum StateName
	{
		Select,
		Cancel;
	} //enum StateName

	private static ClientSelectFileState	instance = new ClientSelectFileState();

	private ClientSelectFileState()
	{
		super(StateName.values());
	} //constructor

	public static ClientSelectFileState getInstance()
	{
		return ClientSelectFileState.instance;
	} //getInstance

	public NavigationState execute()
	{
		//launch the file chooser and return the next NavigationState
		StateName			buttonClicked;
		String				clientDirectoryPath;
		NavigationState		nextState;
		InetSocketAddress	serverAddress;
		String				targetPath;
		buttonClicked = StateName.valueOf(super.showDialog(Resources.getInstance().getClientServerDirectory(), null));
		switch(buttonClicked)
		{
			case Select:
			{
				//check whether the selected file exists in the server directory
				try
				{
					serverAddress = ClientBeginState.getInstance().getFileNameToFileServerMap().getServerAddressFor(new File(this.getSelectedFile().getCanonicalPath()));
				} //try
				catch(IOException e) { throw new RuntimeException(e.getMessage()); }
				if(serverAddress == null)
				{
					System.out.println("There is no server address associated with the selected file");
					nextState = ClientFileNotInServerDirectoryState.getInstance();
				} //if
				else { nextState = ClientSaveFileState.getInstance(); }
				break;
			} //case Select
			case Cancel:
			{
				nextState = ClientGetOrQuitState.getInstance();
				break;
			} //case Cancel
			default:	{ throw new IllegalArgumentException("ClientSelectFileState.execute: no execution statement has been written for this case"); }
		} //switch
		return nextState;
	} //execute

} //class ClientSelectFileState