package hr.fer.projektr.logger;

import java.io.IOException;

public class StandardOutputLogger implements Logger {
	
	private String lastLoggedMessage = "";
	private boolean errorStatus = false;
	
	@Override
	public void close() throws IOException {}

	@Override
	public void log(String msg) {
		System.out.println(msg);
	}

	@Override
	public String getLastLoggedMessage() {
		return lastLoggedMessage;
	}

	@Override
	public boolean getErrorStatus() {
		return errorStatus;
	}

	@Override
	public void setErrorStatus(boolean errorStatus) {
		this.errorStatus = errorStatus;
	}

}
