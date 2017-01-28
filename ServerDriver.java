import javax.swing.*;

public class ServerDriver
{
	/*
		Amber Krause
		November 28, 2016
		CISC 230-02
		Doctor Jarvis

		This class drives the server.

		Methods:
			main()

		Modification History:
			November 28, 2016
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
		state = ServerBeginState.getInstance();
		while(state != null)
		{
			state = state.execute();
		} //while
		System.out.println("ServerDriver main has ended");
	} //main

} //class ServerDriver