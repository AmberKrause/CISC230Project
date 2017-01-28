public class SimpleStack<T> extends SimpleList<T>
{
	/*
		Amber Krause
		November 1, 2016
		CISC 230-02
		Doctor Jarvis

		This class models a stack. Items are added to
		the beginning of the list and removed from the
		beginning of the list.

		Constructors:
			SimpleStack()
				creates a new SimpleStack object. The putPosition
				and takePosition are both Position.FIRST.

		Modification History:
			November 1, 2016
				original version.
	*/

	public SimpleStack()
	{
		super(Position.FIRST, Position.FIRST);
	} //constructor

} //class SimpleStack<T>