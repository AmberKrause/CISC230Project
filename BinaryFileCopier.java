import java.io.*;

public class BinaryFileCopier implements FileCopierInterface
{
	/*
		Amber Krause
		November 8, 2016
		CISC 230-02
		Doctor Jarvis

		This class copies binary files.

		Methods:
			copyFile(InputStream input, OutputStream output, int bufferSize) throws IOException
				copies a file using the given InputStream and OutputStream.

		Modification History:
			November 8, 2016
				original version.
	*/

	public void copyFile(InputStream input, OutputStream output, int bufferSize) throws IOException
	{
		if(input == null) throw new IllegalArgumentException("Driver.copyFile: InputStream parameter is null");
		if(output == null) throw new IllegalArgumentException("Driver.copyFile: OutputStream parameter is null");
		if(bufferSize < 0) throw new IllegalArgumentException("Driver.copyFile: The buffer size must be at least zero: " + bufferSize);
		byte[] buffer;
		int bytesRead;
		buffer = new byte[bufferSize];
		bytesRead = input.read(buffer);
		while(bytesRead > 0)
		{
			output.write(buffer, 0, bytesRead);
			bytesRead = input.read(buffer);
		} //while
	} //copyFile

} //class BinaryFileCopier
