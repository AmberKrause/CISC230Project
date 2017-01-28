import java.io.*;
import java.net.*;
import java.util.*;

public class Listener implements Runnable
{
	/*
		Amber Krause
		November 10, 2016
		CISC 230-02
		Doctor Jarvis

		This class receives a multicast. When a Hello message is
		received from a new server, an entry is added in the HashMap
		for that server and the shared files are listed in the
		serverDirectory as empty files. These will be updated when
		the server's list of shared files is updated and another Hello
		message is received. When a Goodbye message is received, the
		files associated with that server are deleted and the HashMap
		entry is removed.

		Class variables:
			servers
				a Hashmap with a String key in the format of
				username concatenated with the server IP number.
				The values are ServerInfo objects tracking information
				about each server.

			serverDirectory
				the directory containing directories for each server.

			fileNameToFileServerMap
				a FileNameToFileServerMap that tracks the server
				locations of the server files.

		Constructors:
			Listener(File serverDirectory)
				creates a new Listener object set to the value of the
				parameter.

		Methods:
			getServerDirectory()
				accessor for serverDirectory.

			getServerInfo(String userName, InetAddress ipNumber)
				returns the ServerInfo object associated with the
				server.

			putServerInfo(ServerInfo serverInfo)
				adds the ServerInfo object and its key to the servers
				HashMap.

			toKey(String userName, InetAddress ip)
				returns the key format, which is the username concatenated
				with the server IP number.

			run()
				listen for a HelloGoodbye message from a server and pass it
				to the handleMessage method when received.

			handleMessage(HelloGoodbye message, InetAddress sendAddress)
				process the HelloGoodbye message and update the files in
				the serverDirectory as necessary.

			getFileNameMap()
				accessor for fileNameToFileServerMap.

		Nested class:
			ServerInfo

		Modification History:
			November 10, 2016
				original version.

			December 7, 2016
				implemented the Runnable interface.

			December 10, 2016
				expanded the functionality of this class in order to
				process the received Hello and Goodbye objects.

			December 12, 2016
				implemented the functionality of the FileNameToFileServerMap.
	*/

	private HashMap<String, ServerInfo>	servers;
	//String key in the format user name concatenated with server IP number

	private File						serverDirectory;

	private FileNameToFileServerMap		fileNameToFileServerMap;

	public Listener(File serverDirectory, FileNameToFileServerMap fileNameToFileServerMap)
	{
		if(serverDirectory == null) throw new IllegalArgumentException("Listener.constructor: File parameter is null");
		if(fileNameToFileServerMap == null) throw new IllegalArgumentException("Listener.constructor: FileNameToFileServerMap parameter is null");
		this.serverDirectory = serverDirectory;
		this.servers = new HashMap<String, ServerInfo>();
		this.fileNameToFileServerMap = fileNameToFileServerMap;
	} //constructor

	public File getServerDirectory()
	{
		return serverDirectory;
	} //getServerDirectory

	private ServerInfo getServerInfo(String userName, InetAddress ipNumber)
	{
		//return the ServerInfo object associated with the server
		if(userName == null) throw new IllegalArgumentException("Listener.getServerInfo: userName is null");
		if(ipNumber == null) throw new IllegalArgumentException("Listener.getServerInfo: ipNumber is null");
		return servers.get(toKey(userName, ipNumber));
	} //getServerInfo

	private void putServerInfo(ServerInfo serverInfo)
	{
		//add the ServerInfo object and its key to the servers HashMap
		if(serverInfo == null) throw new IllegalArgumentException("Listener.putServerInfo: parameter is null");
		servers.put(toKey(serverInfo.getUserName(), serverInfo. getUnicastIPNumber()), serverInfo);
	} //putServerInfo

	private String toKey(String userName, InetAddress ip)
	{
		//return the key format
		if(userName == null) throw new IllegalArgumentException("Listener.getServerInfo: userName is null");
		if(ip == null) throw new IllegalArgumentException("Listener.getServerInfo: ipNumber is null");
		return userName.trim() + ip.toString().trim();
	} //toKey

	public void run()
	{
		//listen for a HelloGoodbye message from a server and pass it
		//to the handleMessage method when received
		byte[]				buffer;
		DatagramPacket		datagramPacket;
		InetAddress			group;
		InetAddress			ipOfSender;
		HelloGoodbye		message;
		int					messageCounter;
		MulticastSocket		multicastSocket;
		Object				object;
		ObjectInputStream	objectInputStream;
		int					port;

		try
		{
			//initialize variables
			port = Resources.getInstance().getMulticastPortNumber();
			group = Resources.getInstance().getMulticastGroup();
			messageCounter = 0;

			//create packet
			buffer = new byte[1000];
			datagramPacket = new DatagramPacket(buffer, buffer.length);

			//join multicast group
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(group);

			//receive packet
			System.out.println("Listener is listening");
			while(true)
			{
				multicastSocket.receive(datagramPacket);
				ipOfSender = datagramPacket.getAddress();
				messageCounter = messageCounter + 1;
				try
				{
					objectInputStream = new ObjectInputStream(new ByteArrayInputStream(datagramPacket.getData()));
					object = objectInputStream.readObject();
					try { message = (HelloGoodbye) object; }
					catch(ClassCastException cce)
					{
						throw new RuntimeException("Listener.run: Received an object of the class " + object.getClass().getName() + " from " + ipOfSender.getAddress() + " that cannot be cast into a HelloGoodbye object: " + cce.getMessage());
					} //catch(ClassCastException cce)
					handleMessage(message, ipOfSender);
					objectInputStream.close();
				} //try
				catch(Exception e)
				{
					throw new RuntimeException(e.getMessage());
				} //catch(Exception e)
			} //while
		} //try
		catch(IOException ioe) { throw new RuntimeException("Listener.run: IOException was thrown: " + ioe.getMessage()); }
	} //run

	private void handleMessage(HelloGoodbye message, InetAddress sendAddress) throws IOException, ClassNotFoundException
	{
		//process the HelloGoodbye message and update the files in
		//the serverDirectory as necessary
		if(message == null) throw new IllegalArgumentException("Listener.handleMessage: HelloGoodbye parameter is null");
		if(sendAddress == null) throw new IllegalArgumentException("Listener.handleMessage: InetAddress parameter is null");
		Goodbye		goodbyeMessage;
		Hello		helloMessage;
		ServerInfo	serverInfo;
		String		serverUserName;
		long		serverTimeOfLastUpdate;
		System.out.println("Message received: " + message.toString() + " from " + sendAddress.getHostAddress());
		serverUserName = message.getUserName();
		try
		{
			//handle Hello message
			helloMessage = (Hello) message;
			serverInfo = getServerInfo(serverUserName, sendAddress);
			if(serverInfo == null)
			{
				//add entry to HashMap
				serverInfo = new ServerInfo(helloMessage, sendAddress);
				putServerInfo(serverInfo);
				System.out.println("Added entry to servers HashMap. There are now " + servers.size() + " entries.");
			} //if
			//if the server's files have been updated, update the sharedFiles list
			//and update the files in the directory
			else
			{
				serverTimeOfLastUpdate = helloMessage.getTimeOfLastUpdate();
				if(serverInfo.getTimeOfLastUpdate() < serverTimeOfLastUpdate)
				{
					System.out.println("Updating ServerInfo files for " + serverInfo.getUserName());
					serverInfo.deleteFiles();
					serverInfo.setSharedFiles(serverInfo.getSharedFilesFromServer());
					serverInfo.createFiles();
					serverInfo.setTimeOfLastUpdate(serverTimeOfLastUpdate);
				} //if
			} //else
		} //try
		catch(ClassCastException cce) {}
		try
		{
			//handle Goodbye message
			goodbyeMessage = (Goodbye) message;
			serverInfo = getServerInfo(goodbyeMessage.getUserName(), sendAddress);
			//delete the files and remove the entry from the HashMap
			if(serverInfo != null)
			{
				serverInfo.deleteFiles();
				new File(serverInfo.getDirectoryName()).delete();
				servers.remove(toKey(goodbyeMessage.getUserName(), sendAddress));
				System.out.println("Removed entry from servers HashMap. There are now " + servers.size() + " entries.");
			} //if
		} //try
		catch(ClassCastException cce) {}
	} //handleMessage

	private FileNameToFileServerMap getFileNameMap()
	{
		return fileNameToFileServerMap;
	} //getFileNameMap

	private class ServerInfo
	{
		/*
			Amber Krause
			December 10, 2016
			CISC 230-02
			Doctor Jarvis

			This class tracks information about a server.

			Class variables:
				sharedFiles
					a list of the absolute pathnames of the shared
					files.

				timeOfLastUpdate
					a long representing the time of the last update
					of the list of shared files.

				unicastIPNumber
					the InetAddress ip number of the server

				unicastPortNumber
					an int representing the port number when communicating
					with the server.

				userName
					the String username associated with the server.

			Constructors:
				ServerInfo(Hello helloMessage, InetAddress unicastIPNumber)
					creates a new ServerInfo object based on information
					gathered from the passed parameters.

			Methods:
				getUserName()
					accessor for userName.

				getDirectoryName()
					returns the String path of the directory containing the files
					from the server.

				getTimeOfLastUpdate()
					accessor for timeOfLastUpdate.

				getUnicastIPNumber()
					accessor for unicastIPNumber.

				getUnicastPortNumber()
					accessor for unicastPortNumber.

				setSharedFiles(File[] fileArray)
					mutator for sharedFiles.

				setTimeOfLastUpdate(long time)
					mutator for timeOfLastUpdate.

				createFiles()
					creates empty files in the directory for each file in the
					sharedFiles.

				deleteFiles()
					deletes each file in the directory.

	 			getSharedFilesFromServer()
	 				returns a file array listing the absolute path names of
	 				the files in the server's shared directory.

			Modification History:
				December 10, 2016
					original version.

				December 12, 2016
					modified createFiles() and deleteFiles() to implement
					the use of the FileNameToFileServerMap.

				December 13, 2016
					fixed a bug in the getSharedFilesFromServer() method
					that caused the socket to close.
		*/

		private File[]		sharedFiles;

		private long		timeOfLastUpdate;

		private InetAddress	unicastIPNumber;

		private int			unicastPortNumber;

		private String		userName;

		public ServerInfo(Hello helloMessage, InetAddress unicastIPNumber) throws IOException
		{
			if(helloMessage == null) throw new IllegalArgumentException("ServerInfo.constructor: Hello object parameter is null");
			if(unicastIPNumber == null) throw new IllegalArgumentException("ServerInfo.constructor: InetAddress parameter is null");
			File[]	fileArray;
			this.unicastIPNumber = unicastIPNumber;
			this.unicastPortNumber = helloMessage.getUnicastPortNumber();
			this.userName = helloMessage.getUserName();
			try
			{
				(new File(getDirectoryName())).mkdir();
				fileArray = getSharedFilesFromServer();
				setSharedFiles(fileArray);
				setTimeOfLastUpdate(helloMessage.getTimeOfLastUpdate());
				System.out.println("Updating ServerInfo files for " + getUserName());
				createFiles();
			} //try
			catch(IOException ioe) { throw new IOException(ioe); }
			catch(ClassNotFoundException e) { throw new RuntimeException("Listener.ServerInfo.constructor: Could not retrieve the shared files from the server: " + e.getMessage()); }
		} //constructor

		public String getUserName()
		{
			return userName;
		} //getUserName

		public String getDirectoryName()
		{
			//return the String path of the directory containing the
			//files from the server
			return getServerDirectory().getAbsolutePath() + "\\" + getUserName() + "." + (getUnicastIPNumber().getHostAddress());
		} //getDirectoryName

		public long getTimeOfLastUpdate()
		{
			return timeOfLastUpdate;
		} //getTimeOfLastUpdate

		public InetAddress getUnicastIPNumber()
		{
			return unicastIPNumber;
		} //getUnicastIPNumber

		public int getUnicastPortNumber()
		{
			return unicastPortNumber;
		} //getUnicastPortNumber

		public void setSharedFiles(File[] fileArray)
		{
			if(fileArray == null) throw new IllegalArgumentException("ServerInfo.setSharedFiles: parameter is null");
			sharedFiles = fileArray;
		} //setSharedFiles

		public void setTimeOfLastUpdate(long time)
		{
			timeOfLastUpdate = time;
		} //setTimeOfLastUpdate

		public void createFiles()
		{
			//create empty files in the directory for each file in
			//the sharedFiles
			boolean	createSuccessful;
			File	file;
			for(int i = 0; i < sharedFiles.length; i++)
			{
				try
				{
					file = sharedFiles[i];
					getFileNameMap().putServerAddressFor(file, new InetSocketAddress(getUnicastIPNumber(), getUnicastPortNumber()));
					createSuccessful = file.createNewFile();
				} //try
				catch(IOException ioe) { throw new RuntimeException("Listener.ServerInfo.createFiles: could not create this file: " + sharedFiles[i]); }
			} //for
		} //createFiles

		public void deleteFiles()
		{
			//delete each file in the directory
			boolean	deleteSuccessful;
			for(int i = 0; i < sharedFiles.length; i++)
			{
				deleteSuccessful = sharedFiles[i].delete();
				if(!deleteSuccessful) throw new RuntimeException("Listener.ServerInfo.deleteFiles: could not delete this file: " + sharedFiles[i]);
			} //for
			getFileNameMap().remove(sharedFiles);
		} //deleteFiles

		public File[] getSharedFilesFromServer() throws IOException, ClassNotFoundException
		{
			//return a file array listing the absolute path names of
	 		//the files in the server's shared directory
			String						absolutePath;
			//absolute path of individual file
			Socket						socket;
			ArrayList<File>				fileAbsolutePaths;
			//files stored by absolute paths
			File[]						fileRelativePaths;
			//files stored by relative paths
			SharedFilesRequestMessage	message;
			ObjectInputStream			objectInputStream;
			ObjectOutputStream			objectOutputStream;
			File[]						result; //file array from server

			message = new SharedFilesRequestMessage();

			//serialize the message and send it to the server
			socket = new Socket(getUnicastIPNumber(), getUnicastPortNumber());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(message);

			//receive array of relative paths from the server
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			fileRelativePaths = (File[]) objectInputStream.readObject();
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();

			//convert the array of relative paths into an ArrayList of absolute paths
			fileAbsolutePaths = new ArrayList<File>();
			for(int i = 0; i < fileRelativePaths.length; i++)
			{
				absolutePath = getDirectoryName() + "\\" + fileRelativePaths[i];
				fileAbsolutePaths.add(new File(absolutePath));
			} //for

			//return array of absolute paths
			result = (File[]) fileAbsolutePaths.toArray(new File[fileAbsolutePaths.size()]);
			return result;
		} //getSharedFilesFromServer

	} //class ServerInfo

} //class Listener