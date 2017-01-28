import java.io.*;

public interface FileCopierInterface
{
	/*
		Amber Krause
		November 8, 2016
		CISC 230-02
		Doctor Jarvis

		An interface for classes that can copy files.

		Modification History:
			November 8, 2016
				original version.
	*/

	public void copyFile(InputStream input, OutputStream output, int bufferSize) throws IOException;

} //interface FileCopierInterface