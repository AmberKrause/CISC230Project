import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.*;

public class Hello extends HelloGoodbye
{
	/*
		Amber Krause
		December 5, 2016
		CISC 230-02
		Doctor Jarvis

		This class sends out Hello messages to notify clients that a
		server is available.

		Class variables:
			serialVersionUID
				a serialVersionUID used for serialization.

			helloThread
				a thread used to run this object.

			keepRunning
				an AtomicBoolean indicating whether to continue
				attempting to send the message.

			keepSending
				an AtomicBoolean indicating whether to continue
				sending the message.

			sleepBetweenSends
				an int indicating the number of milliseconds
				to sleep between sending the message.

			stopSending
				a boolean indicating whether to stop sending
				the message.

			timeOfLastUpdate
				a long indicating the last time the file list
				in SharedFiles was updated.

			unicastPortNumber
				an int representing the port number for unicast.

			sharedFiles
				a SharedFiles object that maintains a list of
				shared files.

		Enumeration types:
			Command
				allows the server to be started, stopped, or quit.

		Constructors:
			Hello(String userName, InetAddress multicastGroup, int
			multicastPort, int unicastPortNumber, int sleepBetweenSends,
			SharedFiles sharedFiles)
				creates a new Hello object set to the values of
				the parameters.

		Methods:
			getUnicastPortNumber()
				accessor for unicastPortNumber.

			getTimeOfLastUpdate()
				accessor for timeOfLastUpdate.

			command(Hello.Command command)
				executes the command parameter.

			run()
				repeatedly sends a serialized version of this object.

			keepRunning()
				accessor for keepRunning.

			keepSending()
				accessor for keepSending.

			setKeepRunning(boolean value)
				mutator for keepRunning.

			setKeepSending(boolean value)
				mutator for keepSending.

		Modification History:
			December 5, 2016
				original version.
	*/

	public enum Command
	{
		/*
			Values:
				Start
					causes the Hello object to begin
					sending messages.

				Stop
					causes the Hello object to stop
					sending messages temporarily.

				Quit
					causes the Hello object to quit
					attempting to send messages.

			Methods:
				execute(Hello hello)
					executes the desired funcationality of the command
					on the passed Hello object.
		*/

		Start
		{
			public void execute(Hello hello)
			{
				//start sending messages
				if(hello == null) throw new IllegalArgumentException("Hello.Command.Start.execute: parameter is null");
				if(hello.helloThread == null)
				{
					hello.setKeepRunning(true);
					hello.helloThread = new Thread(hello);
					hello.helloThread.start();
				} //if
				hello.setKeepSending(true);
				System.out.println("Hello has started");
			} //execute
		},

		Stop
		{
			public void execute(Hello hello)
			{
				//stop sending messages
				if(hello == null) throw new IllegalArgumentException("Hello.Command.Stop.execute: parameter is null");
				hello.setKeepSending(false);
				System.out.println("Hello has stopped");
			} //execute
		},

		Quit
		{
			public void execute(Hello hello)
			{
				//stop attempting to send messages
				boolean	isAlive;
				if(hello == null) throw new IllegalArgumentException("Hello.Command.Quit.execute: parameter is null");
				hello.setKeepSending(false);
				hello.setKeepRunning(false);
				if(hello.helloThread != null)
				{
					isAlive = hello.helloThread.isAlive();
					if(isAlive) { hello.helloThread.interrupt(); }
					hello.helloThread = null;
				} //if
				System.out.println("Hello has quit");
			} //execute
		};

		public abstract void execute(Hello hello);

	} //enum Command

	private static final long		serialVersionUID = 1;

	transient private Thread		helloThread;

	transient private AtomicBoolean keepRunning;

	transient private AtomicBoolean	keepSending;

	transient private int			sleepBetweenSends;

	private long					timeOfLastUpdate;

	private int						unicastPortNumber;

	transient private SharedFiles	sharedFiles;

	public Hello(String userName, InetAddress multicastGroup, int multicastPort, int unicastPortNumber, int sleepBetweenSends, SharedFiles sharedFiles) throws IOException
	{
		super(userName, multicastGroup, multicastPort);
		if(unicastPortNumber < 1 || unicastPortNumber > 65535) throw new IllegalArgumentException("Hello.constructor: unicast port number must be at least 1 and not greater than 65,535: " + unicastPortNumber);
		if(sleepBetweenSends < 1) throw new IllegalArgumentException("Hello.constructor: sleepBetweenSends cannot be less than 1: " + sleepBetweenSends);
		if(sharedFiles == null) throw new IllegalArgumentException("Hello.constructor: SharedFiles parameter is null");
		this.unicastPortNumber = unicastPortNumber;
		this.sleepBetweenSends = sleepBetweenSends;
		keepRunning = new AtomicBoolean(false);
		keepSending = new AtomicBoolean(false);
		helloThread = null;
		this.sharedFiles = sharedFiles;
	} //constructor

	public int getUnicastPortNumber()
	{
		return unicastPortNumber;
	} //getUnicastPortNumber

	public long getTimeOfLastUpdate()
	{
		return timeOfLastUpdate;
	} //getTimeOfLastUpdate

	public synchronized void command(Hello.Command command)
	{
		if(command == null) throw new IllegalArgumentException("Hello.command: parameter is null");
		command.execute(this);
	} //command

	public void run()
	{
		try
		{
			while(keepRunning())
			{
				if(keepSending())
				{
					this.timeOfLastUpdate = sharedFiles.getTimeOfLastUpdate();
					super.sendMe();
				} //if
				Thread.sleep(sleepBetweenSends);
			} //while
		} //try
		catch(InterruptedException e) {}
		catch(IOException e)
		{
			throw new RuntimeException(e.getMessage());
		} //catch(IOException e)
	} //run

	private boolean keepRunning()
	{
		return keepRunning.get();
	} //keepRunning

	private boolean keepSending()
	{
		return keepSending.get();
	} //keepSending

	private void setKeepRunning(boolean value)
	{
		keepRunning.set(value);
	} //setKeepRunning

	private void setKeepSending(boolean value)
	{
		keepSending.set(value);
	} //setKeepSending

/*
	public static void main(String[] args) throws Exception
	{
		Hello.Command[]	commandArray;
		Hello.Command	helloCommand;
		Hello			hello;
		int				j;

		commandArray = Hello.Command.values();
		hello = new Hello("AmberKrause", InetAddress.getByName("228.5.6.7"), 8888, 1, 5000, new SharedFiles(new File("C:\\Users\\Amber\\Desktop\\CISC 230")));

		/*
		for(int i = 0; i < 200; i++)
		{
			j = (int) (Math.random() * 100);
			helloCommand = commandArray[j % commandArray.length];
			System.out.println("Issuing " + helloCommand.name() + ": ");
			hello.command(helloCommand);
			Thread.sleep(500);
		} //for
		hello.command(Hello.Command.Quit);
		*//*

		hello.command(Command.Start);
		Thread.sleep(100000);
		hello.command(Command.Quit);
	} //main
*/

} //class Hello