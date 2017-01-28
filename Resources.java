import java.io.*;
import java.net.*;
import java.util.*;

public class Resources
{
	/*
		Amber Krause
		November 21, 2016
		CISC 230-02
		Doctor Jarvis

		This class implements accesses the RemoteFileServer.properties
		and RemoteFileServer_en_US.properties files.

		Class variables:
			clientServerDirectory
				the directory for the server, which is inside
				the directory from which the client was launched.

			instance
				the Resources instance. this variable will
				be null until the Resources object is
				created.

			propertiesFile
				the Resources properties file.

			resourceBundle
				ResourceBundle that indicates the language
				of the user.

			multicastPortNumber
				the port number to be used for multicast.

			multicastGroup
				the InetAddress for multicast.

			unicastPortNumber
				the port number to be used for unicast.

			userName
				a String user name to be used with multicast.

		Constructors:
			Resources()
				creates a new Resources object according
				to the properties in the properties file.

		Methods:
			getClientServerDirectory()
				accessor for clientServerDirectory.

			getInstance()
				returns the Resources object. If there
				is no Resources object, it will be created.

			getMulticastPortNumber()
				accessor for multicastPortNumber.

			getMulticastGroup()
				accessor for multicastGroup.

			getResourceBundle()
				accessor for resourceBundle.

			getUnicastPortNumber()
				accessor for unicastPortNumber.

			getUserName()
				accessor for userName.

			get(String key, Properties properties)
				returns the String form of the property
				associated with the key in the passed
				Properties object.

		Modification History:
			November 21, 2016
				original version.

			November 22, 2016
				added class variable resourceBundle and its
				accessor getResourceBundle().

			December 4, 2016
				added the class variables multicastPortNumber,
				multicastGroup, and userName and their accessor
				methods.

			December 7, 2016
				added the class variable clientServerDirectory
				and its accessor.
	*/

	private File				clientServerDirectory;

	private static Resources	instance = null;

	private static File			propertiesFile;

	private ResourceBundle		resourceBundle;

	private int					multicastPortNumber;

	private InetAddress			multicastGroup;

	private int					unicastPortNumber;

	private String				userName;

	private Resources()
	{
		Locale	locale;
		try
		{
			Properties	properties;
			properties = new Properties();
			properties.load(new FileReader("RemoteFileServer.properties"));
			locale = new Locale(get("Locale.Language", properties), get("Locale.Country", properties));
			resourceBundle = ResourceBundle.getBundle(get("ResourceBundle.BaseName", properties), locale);
			multicastPortNumber = Integer.parseInt(get("Multicast.PortNumber", properties));
			multicastGroup = InetAddress.getByName(get("Multicast.Group", properties));
			unicastPortNumber = Integer.parseInt(get("Unicast.PortNumber", properties));
			userName = get("User.Name", properties);
			clientServerDirectory = new File(System.getProperty("user.dir")+"\\" + get("Client.Server.Directory.Name", properties));
		} //try
		catch(Exception e)
		{
			throw new RuntimeException(e);
		} //catch
	} //constructor

	public File getClientServerDirectory()
	{
		return clientServerDirectory;
	} //getClientServerDirectory

	public static synchronized Resources getInstance()
	{
		if(Resources.instance == null) Resources.instance = new Resources();
		return Resources.instance;
	} //getInstance

	public int getMulticastPortNumber()
	{
		return multicastPortNumber;
	} //getMulticastPortNumber

	public InetAddress getMulticastGroup()
	{
		return multicastGroup;
	} //getMulticastGroup

	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	} //getResourceBundle

	public int getUnicastPortNumber()
	{
		return unicastPortNumber;
	} //getUnicastPortNumber

	public String getUserName()
	{
		return userName;
	} //getUserName

	private String get(String key, Properties properties)
	{
		//return the String form of the property
		return properties.getProperty(key);
	} //get

	public void setUnicastPortNumber(int port)
	{
		if(port < 1 || port > 65536) throw new IllegalArgumentException("Resources.setUnicastPortNumber: port must be between 1 and 65,536: " + port);
		unicastPortNumber = port;
	} //setUnicastPortNumber

} //class Resources