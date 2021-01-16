package hr.fer.projektr.logger;

import java.io.Closeable;

public interface Logger extends Closeable {
	
	void log(String msg);
	
}
