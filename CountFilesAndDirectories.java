import java.io.*;

public class CountFilesAndDirectories implements FileProcessor, FileFilter
{
	/*
		Amber Krause
		November 2, 2016
		CISC 230-02
		Doctor Jarvis

		This class models a FileProcessor that counts the number
		of directories and files within a parent directory.

		Class variables:
			fileCount
				a long representing the number of files.

			directoryCount
				a long representing the number of directories.

			fileFilter
				a FileFilter used to determine which files will
				be processed.

		Constructors:
			CountFilesAndDirectories()
				creates a new CountFilesAndDirectories object
				with the default filter, which accepts all files.

			CountFilesAndDirectories(FileFilter fileFilter)
				creates a new CountFilesAndDirectories object
				with the specified filter.

		Methods:
			getFileCount()
				accessor for fileCount.

			getDirectoryCount()
				accessor for directoryCount.

			processFile(File file)
				counts the files and directories within the
				parameter file. If the parameter file is a
				directory, each file and directory within it
				will be processed also.

			getFileFilter()
				accessor for fileFilter.

			accept(File file)
				default filter method that accepts all files.

		Modification History:
			November 3, 2016
				original version.

			November 28, 2016
				modified processFile() to only increment
				fileCount for normal files.
	*/

	private long		fileCount;

	private long		directoryCount;

	private FileFilter	fileFilter;

	public CountFilesAndDirectories()
	{
		this(null);
	} //CountFilesAndDirectories

	public CountFilesAndDirectories(FileFilter fileFilter)
	{
		fileCount = 0;
		directoryCount = 0;
		if(fileFilter == null) fileFilter = this;
		this.fileFilter = fileFilter;
	} //CountFilesAndDirectories(FileFilter fileFilter)

	public long getFileCount()
	{
		return fileCount;
	} //getFileCount

	public long getDirectoryCount()
	{
		return directoryCount;
	} //getDirectoryCount

	public void processFile(File file)
	{
		//counts the files and directories within the parameter file
		DirectoryLister		subDirectoryLister;
		if(file != null && getFileFilter().accept(file))
		{
			if(file.isDirectory())
			{
				directoryCount = directoryCount + 1;
			} //if(file.isDirectory())
			if(file.isFile())
			{
				fileCount = fileCount + 1;
			} //else
		} //if(file != null)
	} //processFile

	public FileFilter getFileFilter()
	{
		return fileFilter;
	} //getFileFilter

	public boolean accept(File file)
	{
		//default filter method
		return true;
	} //accept

} //class CountFilesAndDirectories