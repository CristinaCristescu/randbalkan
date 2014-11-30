package balkanAI;
/**
 * Thrown to indicate that a message is invalid according to the protocol.
 */
@SuppressWarnings("serial")
public class InvalidMessageException extends Exception
{
	// no default constructor, require a message

	/**
	 * @param message A description of the exception.
	 */
	public InvalidMessageException(String message)
	{
		super(message);
	}
}
