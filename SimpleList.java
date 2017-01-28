import java.util.*;

public class SimpleList<T>
{
	/*
		Amber Krause
		October 31, 2016
		CISC 230-02
		Doctor Jarvis

		This class represents a simple list of objects of the specified type.

		Class variables:
			list
				an ArrayList containing objects of the specified type.

			putPosition
				the Position enum value specifying where in the
				list new objects should be added.

			takePostition
				the Position enum value specifying from where in
				the list objects should be taken.

		Constructors:
			SimpleList(Position putPosition, Position takePosition)
				creates a new SimpleList object set to the values
				of the parameters.

		Methods:
			clear()
				removes all of the elements from the list.

			getPutPosition()
				accessor for putPosition.

			getTakePosition()
				accessor for takePosition.

			isEmpty()
				returns true if the list has no elements.

			put(T value)
				adds the parameter value to the list in the
				location specified by putPosition.

			put(T[] valueArray)
				adds the values in the parameter array to
				the list in the location specified by
				putPosition.

			size()
				returns the integer number of elements in
				the list.

			take()
				returns the item in the location specified
				by takePosition and removes the object from
				the list.

			takeAll()
				returns an ArrayList of the items in
				the order specified by takePosition and
				removes the elements from the list.

		Modification History:
			October 31, 2016
				original version.

			November 1, 2016
				modified to accept generic types. takeAll()
				modified to return an ArrayList.
	*/

	private ArrayList<T>		list;

	private Position			putPosition;

	private Position			takePosition;

	public SimpleList(Position putPosition, Position takePosition)
	{
		if(putPosition == null || takePosition == null) throw new IllegalArgumentException("SimpleList.constructor: parameter is null");
		list = new ArrayList<T>();
		this.putPosition = putPosition;
		this.takePosition = takePosition;
	} //constructor

	public void clear()
	{
		list.clear();
	} //clear

	public Position getPutPosition()
	{
		return putPosition;
	} //getPutPosition

	public Position getTakePosition()
	{
		return takePosition;
	} //getPutPosition

	public boolean isEmpty()
	{
		return list.isEmpty();
	} //isEmpty

	public void put(T value)
	{
		//adds the parameter value to the list in the location
		//specified by putPosition
		int		index;
		index = 0;
		if(size() > 0)
		{
			index = getPutPosition().get(size());
			if(getPutPosition() == Position.LAST) index = index + 1;
			if(getPutPosition() == Position.RANDOM) index = (int)Math.floor(Math.random() * (size() + 1));
		} //if
		list.add(index, value);
	} //put(T value)

	public void put(T[] valueArray)
	{
		//adds the values in the parameter array to the list in
		//the location specified by putPosition
		if(valueArray == null) throw new IllegalArgumentException("SimpleList.put: parameter array is null");
		for(int i = 0; i < valueArray.length; i++)
		{
			put(valueArray[i]);
		} //for
	} //put(T[] valueArray)

	public int size()
	{
		return list.size();
	} //size

	public T take()
	{
		//returns the item in the location specified by takePosition
		//and removes the object from the list
		int		index;
		T		result;
		if(isEmpty()) throw new NoSuchElementException("SimpleList.take: the list is empty");
		index = getTakePosition().get(size());
		result = list.remove(index);
		return result;
	} //take

	public ArrayList<T> takeAll()
	{
		//returns an ArrayList of the items in the order specified
		//by takePosition and removes the elements from the list
		ArrayList<T>	result;
		int				size;
		if(isEmpty()) throw new NoSuchElementException("SimpleList.take: the list is empty");
		size = size();
		result = new ArrayList<T>(size);
		for(int i = 0; i < size; i++)
		{
			result.add(take());
		} //for
		return result;
	} //takeAll

} //class SimpleList<T>