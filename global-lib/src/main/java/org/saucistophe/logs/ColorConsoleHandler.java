package org.saucistophe.logs;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ColorConsoleHandler extends ConsoleHandler
{
	protected static final String COLOR_RESET = "\u001b[0m";
	protected static final String COLOR_SEVERE = "\u001b[31;1m";
	protected static final String COLOR_WARNING = "\u001b[33;1m";
	protected static final String COLOR_INFO = "\u001b[37m";
	protected static final String COLOR_CONFIG = "\u001b[34;1m";
	protected static final String COLOR_FINE = "\u001b[30;1m";
	protected static final String COLOR_FINER = "\u001b[30;1m";
	protected static final String COLOR_FINEST = "\u001b[30;1m";

	public ColorConsoleHandler()
	{
		super();
		registerFormatter();
	}

	private void registerFormatter()
	{
		setFormatter(new SimpleFormatter()
		{
			@Override
			public synchronized String format(LogRecord record)
			{
				String prefix;
				Level level = record.getLevel();
				if (level == Level.SEVERE)
				{
					prefix = COLOR_SEVERE;
				}
				else if (level == Level.WARNING)
				{
					prefix = COLOR_WARNING;
				}
				else if (level == Level.INFO)
				{
					prefix = COLOR_INFO;
				}
				else if (level == Level.CONFIG)
				{
					prefix = COLOR_CONFIG;
				}
				else if (level == Level.FINE)
				{
					prefix = COLOR_FINE;
				}
				else if (level == Level.FINER)
				{
					prefix = COLOR_FINER;
				}
				else if (level == Level.FINEST)
				{
					prefix = COLOR_FINEST;
				}
				else
				{
					prefix = COLOR_SEVERE;
				}

				// Use short package names.
				String[] pathElements = record.getSourceClassName().split("\\.");
				for (int i = 0; i < pathElements.length - 1; i++)
					pathElements[i] = pathElements[i].substring(0, 1);
				

				// I don't like spaces between class and method. I prefer ::.
				record.setSourceMethodName(String.join("::", String.join(".",pathElements), record.getSourceMethodName()));
				record.setSourceClassName(null);
				
				
				String rawMessage = super.format(record);
				return prefix + rawMessage + COLOR_RESET;
			}
		});
	}
}
