import java.io.*;

public class DirectoryLister implements FileProcessor
{
	/*
		Amber Krause
		November 2, 2016
		CISC 230-02
		Doctor Jarvis

		This class creates a model that is used to list all of the
		files and subdirectory files of a directory.

		Class variables:
			rootDirectory
				a File object that represents the root directory.

			filesNotYetProcessed
				a SimpleList of the File objects that have not
				been processed yet.

			fileProcessor
				a FileProcessor that determines how the files will
				be processed.

		Constructors:
			DirectoryLister(File rootDirectory)
				creates a new DirectoryLister with the DirectoryLister
				as the default FileProcessor.

			DirectoryLister(File rootDirectory, FileProcessor fileProcessor)
				creates a new DirectoryLister set to the values of
				the parameter.

		Methods:
			getListOfUnprocessedFiles()
			 	private accessor for filesNotYetProcessed.

			getRootDirectory()
				accessor for rootDirectory.

			run()
				processes each File in the directory.

			processFile(File file)
				prints each file and directory within the parameter file.
				If the parameter file is a directory, each file within it
				will be processed with a new DirectoryLister.

			getFileProcessor()
				accessor for fileProcessor.

		Modification History:
			November 2, 2016
				original version.

			November 3, 2016
				added the fileProcessor class variable, overloaded constructor
				DirectoryLister(File rootDirectory, FileProcessor fileProcessor),
				and the getFileProcessor() accessor method. This allows for
				a user to select the desired implementation of the
				processFile(File file) method.

			November 9, 2016
				modified the constructor to change the passed file to its
				canonical version.

			November 28, 2016
				modified the constructor and the run() method to cause
				the root directory to be added to filesNotYetProcessed
				in the run() method.
	*/

	private File				rootDirectory;

	private SimpleList<File>	filesNotYetProcessed;

	private FileProcessor		fileProcessor;

	public DirectoryLister(File rootDirectory)
	{
		this(rootDirectory, null);
	} //DirectoryLister(File rootDirectory)

	public DirectoryLister(File rootDirectory, FileProcessor fileProcessor)
	{
		File[]	fileArray;
		if(rootDirectory == null) throw new IllegalArgumentException("DirectoryLister.constructor: the File parameter is null");
		if(!rootDirectory.isDirectory()) throw new IllegalArgumentException("DirectoryLister.constructor: the File parameter is not a directory: " + rootDirectory);
		if(fileProcessor == null) { fileProcessor = this; }
		this.fileProcessor = fileProcessor;
		try { this.rootDirectory = rootDirectory.getCanonicalFile(); }
		catch(IOException e) { throw new RuntimeException(e); }
		filesNotYetProcessed = new SimpleStack<File>();
	} //DirectoryLister(File rootDirectory, FileProcessor fileProcessor)

	private SimpleList<File> getListOfUnprocessedFiles()
	{
		return filesNotYetProcessed;
	} //getListOfUnprocessedFiles

	public File getRootDirectory()
	{
		return rootDirectory;
	} //getRootDirectory

	public void run()
	{
		//process each file in the directory
		File				file;
		SimpleList<File>	list;
		list = getListOfUnprocessedFiles();
		list.put(getRootDirectory());
		while(!list.isEmpty())
		{
			file = list.take();
			if(file != null)
			{
				if(file.isDirectory()) list.put(file.listFiles());
				getFileProcessor().processFile(file);
			} //if
		} //while
	} //run

	public void processFile(File file)
	{
		//prints each file and directory within the parameter file
		DirectoryLister		subDirectoryLister;
		if(file == null) throw new IllegalArgumentException("DirectoryLister.processFile: parameter is null");
		else
		{
			if(file.isDirectory())
			{
				System.out.println("directory " + file);
			} //if(file.isDirectory())
			else
			{
				//file is not null and not a directory
				System.out.println("file      " + file);
			} //else
		} //if(file != null)
	} //processFile

	public FileProcessor getFileProcessor()
	{
		return fileProcessor;
	} //getFileProcessor

} //class DirectoryLister