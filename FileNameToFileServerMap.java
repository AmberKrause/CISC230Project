import java.io.*;
import java.net.*;
import java.util.*;

public class FileNameToFileServerMap
{
	/*
		Amber Krause
		December 12, 2016
		CISC 230-02
		Doctor Jarvis

		This class tracks the locations of the server files.

		Class variables:
			directory
				a HashMap<File, InetSocketAddress> containing the
				absolute file paths and the InetSocketAddress where
				each is located.

		Constructors:
			FileNameToFileServerMap()
				creates a new FileNameToFileServerMap.

		Methods:
			getServerAddressFor(File file)
				returns the InetSocketAddress associated with the File key.

			putServerAddressFor(File file, InetSocketAddress address)
				adds the File key and its associated InetSocketAddress to
				the directory.

			remove(File file)
				removes the File key and its associated InetSocketAddress
				from the directory.

			remove(File[] fileArray)
				removes the File keys in the array and their associated
				InetSocketAddresses.

			size()
				returns the number of Files in the directory.

			toKey(File file)
				returns the File with the absolute path.

		Modification History:
			December 12, 2016
				original version.
	*/

	private HashMap<File, InetSocketAddress>	directory;

	public FileNameToFileServerMap()
	{
		directory = new HashMap<File, InetSocketAddress>();
	} //constructor

	public synchronized InetSocketAddress getServerAddressFor(File file)
	{
		if(file == null) throw new IllegalArgumentException("FileNameToFileServerMap.getServerAddressFor: parameter is null");
		return directory.get(toKey(file));
	} //getServerAddressFor

	public synchronized void putServerAddressFor(File file, InetSocketAddress address)
	{
		if(file == null) throw new IllegalArgumentException("FileNameToFileServerMap.putServerAddressFor: File parameter is null");
		if(address == null) throw new IllegalArgumentException("FileNameToFileServerMap.putServerAddressFor: InetSocketAddress parameter is null");
		directory.put(toKey(file), address);
	} //putServerAddressFor

	public synchronized void remove(File file)
	{
		if(file == null) throw new IllegalArgumentException("FileNameToFileServerMap.remove: File parameter is null");
		directory.remove(toKey(file));
	} //remove(File file)

	public void remove(File[] fileArray)
	{
		if(fileArray == null) throw new IllegalArgumentException("FileNameToFileServerMap.remove: File array parameter is null");
		for(int i = 0; i < fileArray.length; i++)
		{
			remove(fileArray[i]);
		} //for
	} //remove(File[] fileArray)

	public int size()
	{
		return directory.size();
	} //size

	private File toKey(File file)
	{
		try
		{
			return file.getCanonicalFile();
		} //try
		catch(IOException ioe) { throw new RuntimeException("FileNameToFileServerMap.toKey: " + ioe.getMessage()); }
	} //toKey

} //class FileNameToFileServerMap