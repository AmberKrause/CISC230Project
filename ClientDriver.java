import javax.swing.*;

public class ClientDriver
{
	/*
		Amber Krause
		December 6, 2016
		CISC 230-02
		Doctor Jarvis

		This class drives the client.

		Methods:
			main()

		Modification History:
			December 6, 2016
				original version.
	*/

	public static void main(String[] args)
	{
		NavigationState	state;
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} //try
		catch (Exception e) { System.out.println(e.getMessage()); }
		state = ClientBeginState.getInstance();
		while(state != null)
		{
			state = state.execute();
		} //while
		System.out.println("ClientDriver main has ended");
	} //main

} //class ServerDriver