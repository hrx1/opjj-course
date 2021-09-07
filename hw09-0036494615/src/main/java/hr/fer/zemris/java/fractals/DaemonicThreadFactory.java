package hr.fer.zemris.java.fractals;

import java.util.concurrent.ThreadFactory;

/**
 * ThreadFactory which creates Deamonic Threads
 * @author Hrvoje
 *
 */
public class DaemonicThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		Thread result = new Thread(r);
		result.setDaemon(true);
		return result;
	}

}
