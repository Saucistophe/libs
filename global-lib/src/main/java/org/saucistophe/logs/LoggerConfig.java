package org.saucistophe.logs;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import static java.util.logging.Level.parse;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.saucistophe.annotations.SettingsField;
import org.saucistophe.settings.SettingsHandler;

public class LoggerConfig
{
	@SettingsField(category = "Logs", name = "Global logger level", 
					possibleValues = { "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "OFF" })
	public static String globalLoggerLevel = "CONFIG";

	@SettingsField(category = "Logs", name = "Local logger level",
					possibleValues = { "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "OFF" })
	public static String localLoggerLevel = "FINE";

	@SettingsField(category = "Logs", name = "Log format string")
	public static String LOG_FORMAT = "%1$tH:%1$tM:%1$tS.%1$tL [%4$-7s] %5$s%6$s (%2$s)%n";

	@SettingsField(category = "Logs", name = "Log to a file")
	public static boolean LOG_TO_FILE = false;

	@SettingsField(category = "Logs", name = "Log file size (Ko)")
	public static int LOG_FILE_SIZE = 5 * 1024;

	public LoggerConfig()
	{
		super();

		// Load settings from the settings file first off, to allow changing sensitivie things like the format.
		SettingsHandler.readFromFile();

		
		
		System.out.println(String.format("Initializing log system (%s/%s)",globalLoggerLevel, localLoggerLevel));
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);

		// Purge all existing configuration.
		LogManager.getLogManager().reset();

		// Set loggers severity level.
		Logger globalLogger = Logger.getLogger("");
		globalLogger.setLevel(parse(globalLoggerLevel));
		Logger localLogger = Logger.getLogger("org.saucistophe");
		localLogger.setLevel(parse(localLoggerLevel));

		// Redirect all output to a color console handler.
		ColorConsoleHandler handler = new ColorConsoleHandler();
		handler.setLevel(Level.ALL);
		globalLogger.addHandler(handler);

		// Log to file.
		if (LOG_TO_FILE)
		{
			try
			{
				FileHandler fileHandler = new FileHandler("GC.log", LOG_FILE_SIZE * 1024, 1, true);
				// Make it human-readable
				SimpleFormatter formatter = new SimpleFormatter();
				fileHandler.setFormatter(formatter);
				globalLogger.addHandler(fileHandler);
			}
			catch (IOException | SecurityException ex)
			{
				System.err.println(ex);
			}
		}
	}
}
