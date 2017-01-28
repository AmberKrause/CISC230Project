public enum Position
{
	/*
		Amber Krause
		October 31, 2016
		CISC 230-02
		Doctor Jarvis

		This enum models a position within an array, Vector, or ArrayList.

		Constants:
			FIRST
				the first position in the array, Vector, or ArrayList.

			LAST
				the last position in the array, Vector, or ArrayList.

			RANDOM
				a random position in the array, Vector, or ArrayList.

		Methods:
			get(int size)
				returns an integer representing the appropriate index
				indicated by the constant.

			checkParameter(String enumConstant, int size)
				private. Throws an IllegalArgumentException if the int
				parameter is less than one.

		Modification History:
			October 31, 2016
				original version

			November 28, 2016
				modified the implementation of get(int size) in the enum.
	*/

	FIRST
	{
		@Override
		public int get(int size)
		{
			//returns the first index
			checkParameter(this.name(), size);
			return 0;
		} //get
	},
	LAST
	{
		@Override
		public int get(int size)
		{
			//returns the last index
			checkParameter(this.name(), size);
			return size - 1;
		} //get
	},
	RANDOM
	{
		@Override
		public int get(int size)
		{
			//returns a random index
			checkParameter(this.name(), size);
			return (int)Math.floor(Math.random() * size);
		} //get
	};

	abstract public int get(int size);

	private static void checkParameter(String enumConstant, int size)
	{
		if(size < 1) throw new IllegalArgumentException("Position." + enumConstant + ".get.checkParameter: the int parameter must be greater than or equal to one: " + size);
	} //checkParameter

} //enum Position