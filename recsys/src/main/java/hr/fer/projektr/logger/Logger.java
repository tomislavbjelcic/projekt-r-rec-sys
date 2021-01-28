package hr.fer.projektr.logger;

import java.io.Closeable;

public interface Logger extends Closeable {
	
	void log(String msg);
	default void log(Object o) {
		log(String.valueOf(o));
	}
	String getLastLoggedMessage();
	boolean getErrorStatus();
	void setErrorStatus(boolean errorStatus);
	
}
