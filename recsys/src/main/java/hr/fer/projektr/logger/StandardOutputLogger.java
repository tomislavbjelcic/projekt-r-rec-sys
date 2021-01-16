package hr.fer.projektr.logger;

import java.io.IOException;

public class StandardOutputLogger implements Logger {

	@Override
	public void close() throws IOException {}

	@Override
	public void log(String msg) {
		System.out.println(msg);
	}

}
