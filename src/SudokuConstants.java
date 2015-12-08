/**
 * This file declares several constants that are useful in
 * the program in different modules.  Any class that 
 * implements this interface can use these constants.
 */
public interface SudokuConstants {
	
	/**
	 * The size of the game board, which is also the 
	 * number of cells in a block. The board should
	 * always be square, with equal height and width.
	 */
	public static final int BOARD_SIZE = 9;
	/**
	 * The height (how many rows) of a single block.
	 * The width of a block should be BOARD_SIZE / BLOCK_HEIGHT
	 */
	public static final int BLOCK_HEIGHT = 3;
	/**
	 * The default port number for both server and client.
	 * It is shown as the default value for the text fields.
	 */
	public static final int DEFAULT_PORT = 3141;
	/**
	 * The default IP address to connect for client.
	 * It is shown as the default value for the text fields.
	 */
	public static final String DEFAULT_IP = "127.0.0.1";
	/**
	 * The default difficulty level. It is shown as the default
	 * value for the difficulty text field. You can change this 
	 * value to fit your own difficulty level system.
	 */
	public static final int DEFAULT_DIFFICULTY = 5;
}
