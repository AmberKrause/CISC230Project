import java.io.*;
import java.util.*;

public class SharedFiles implements Runnable
{
	/*
		Amber Krause
		November 30, 2016
		CISC 230-02
		Doctor Jarvis

		This class keeps a list of the files in the SharedDirectory.

		Class variables:
			fileList
				a File ArrayList storing the files in the SharedDirectory
				by their relative path names.

			timeOfLastUpdate
				a long indicating the time the fileList was last updated.

			sharedDirectory
				the shared directory for the server.

		Constructors:
			SharedFiles(File sharedDirectory)
				creates a new SharedFiles object set to the value of the
				parameter.

		Methods:
			getFileList()
				private accessor for fileList.

			getTimeOfLastUpdate()
				accessor for timeOfLastUpdate.

			getSetTimeOfLastUpdate(long updateTime)
				sets timeOfLastUpdate if the passed updateTime is more recent.
				Returns true if timeOfLastUpdate was set.

			getSharedDirectory()
				accessor for sharedDirectory.

			setList(ArrayList<File> fileList)
				sets the fileList to the passed file ArrayList.

			toArray()
				returns an array version of fileList.

			run()
				periodically checks whether the sharedDirectory has been updated
				and, if it has, will update fileList.

		Inner class:
			GetSharedFiles
				this class updates SharedFiles's list of the files in the
				SharedDirectory.

		Modification History:
			November 30, 2016
				original version.
	*/

	private ArrayList<File>	fileList;

	private long			timeOfLastUpdate;

	private File			sharedDirectory;

	public SharedFiles(File sharedDirectory)
	{
		if(sharedDirectory == null) throw new IllegalArgumentException("SharedFiles.constructor: parameter shared directory is null");
		fileList = new ArrayList<File>();
		timeOfLastUpdate = 0;
		this.sharedDirectory = sharedDirectory;
	} //constructor

	private ArrayList<File> getFileList()
	{
		return fileList;
	} //getFileList

	public synchronized long getTimeOfLastUpdate()
	{
		return timeOfLastUpdate;
	} //getTimeOfLastUpdate

	public synchronized boolean getSetTimeOfLastUpdate(long updateTime)
	{
		//set timeOfLastUpdate if the passed updateTime is more recent
		//return true if timeOfLastUpdate was set
		boolean	result;
		if(updateTime > getTimeOfLastUpdate())
		{
			timeOfLastUpdate = Math.max(getTimeOfLastUpdate(), updateTime);
			result = true;
		} //if
		else { result = false; }
		return result;
	} //getSetTimeOfLastUpdate

	public File getSharedDirectory()
	{
		return sharedDirectory;
	} //getSharedDirectory

	public synchronized void setList(ArrayList<File> fileList)
	{
		//set the fileList to the passed file ArrayList
		if(fileList == null) throw new IllegalArgumentException("SharedFiles.setList: parameter file ArrayList is null");
		this.fileList = fileList;
	} //setList

	public synchronized File[] toArray()
	{
		//return an array version of fileList
		return getFileList().toArray(new File[getFileList().size()]);
	} //toArray

	public void run()
	{
		while(true)
		{
			if(getSetTimeOfLastUpdate(ServerSelectDirectoryState.getInstance().getTimeOfLastUpdate()))
			{
				//if time updated is changed, create new GetSharedFiles to update the ArrayList
				//of files in the shared directory
				new Thread(new GetSharedFiles(getSharedDirectory())).start();
			} //if
			try
			{
				Thread.sleep(1000);
			} //try
			catch(Exception e) {}
		} //while
	} //run

	private class GetSharedFiles implements Runnable, FileProcessor
	{
		/*
			Amber Krause
			November 30, 2016
			CISC 230-02
			Doctor Jarvis

			This class updates SharedFiles's list of the files in the SharedDirectory.

			Class variables:
				fileList
					a File ArrayList storing the files in the SharedDirectory
					by their relative path names.

				sharedDirectoryPathLength
					an int representing the path length of the shared directory.

				directoryLister
					a DirectoryLister used to process each file in the shared
					directory.

			Constructors:
				GetSharedFiles(File sharedDirectory)
					creates a new GetSharedFiles object set to the value of
					the parameter.

			Methods:
				processFile(File file)
					adds the passed file to fileList using its relative path name.

				run()
					runs the directoryLister to process each file and then sends
					the list to the SharedFiles object.

			Modification History:
				November 30, 2016
					original version.
		*/

		private ArrayList<File>	fileList;

		private int				sharedDirectoryPathLength;

		private DirectoryLister	directoryLister;

		public GetSharedFiles(File sharedDirectory)
		{
			if(sharedDirectory == null) throw new IllegalArgumentException("GetSharedFiles.constructor: parameter shared directory is null");
			fileList = new ArrayList<File>();
			sharedDirectoryPathLength = getSharedDirectory().toString().length();
			directoryLister = new DirectoryLister(sharedDirectory, this);
		} //constructor

		public void processFile(File file)
		{
			String	fullPath; //the file's full path
			String	shortPath;
			//the file's path relative to the shared directory
			if(file == null) throw new IllegalArgumentException("GetSharedFiles.processFile: parameter is null");
			if(!file.isDirectory())
			{
				//add the file to fileList
				try
				{
					fullPath = file.getCanonicalPath();
					shortPath = fullPath.substring(sharedDirectoryPathLength + 1);
					fileList.add(new File(shortPath));
				} //try
				catch(Exception e)
				{
					throw new RuntimeException(e.getMessage());
				} //catch
			} //if
		} //processFile

		public void run()
		{
			//run the directoryLister to process each file
			//send the list to the SharedFiles object
			directoryLister.run();
			setList(fileList);
		} //run

	} //class GetSharedFiles

} //class SharedFiles