package org.saucistophe.exceptions;

/**
 @author Christophe
 */
public class FormatException extends Exception
{
    public FormatException(String message)
    {
        super(message);
    }

    public FormatException(Throwable throwable)
    {
        super(throwable);
    }

    public FormatException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    public FormatException()
    {
        super();
    }
}
