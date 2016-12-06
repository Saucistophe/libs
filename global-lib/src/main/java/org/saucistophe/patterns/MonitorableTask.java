package org.saucistophe.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

/**
 A class that can be used to perform long tasks. When it is the case, it will fire progress listeners.
 */
public class MonitorableTask
{
	/**
	 Listeners that will be fired every time there's a progress change in this lengthy task.
	 The passed double will be typically used for progress bars, and will range between 0 (no progress) and 1 (done).
	 */
	public List<DoubleConsumer> progressMonitors = new ArrayList<>();

	/**
	 Sets the progress via the registered monitors.

	 @param progress The progress value, ranging between 0 (no progress) and 1 (done).
	 */
	public void setProgress(double progress)
	{
		progressMonitors.stream().forEach(c ->c.accept(progress));
	}
}
