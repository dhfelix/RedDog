package mx.nic.rdap.server.result;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;

import mx.nic.rdap.server.RdapResult;

/**
 * A result from a exception request
 * 
 * @author dalpuche
 *
 */
public class ExceptionResult implements RdapResult {

	private final static Logger logger = Logger.getLogger(ExceptionResult.class.getName());

	private String errorCode;
	private String errorTitle;
	private String errorDescription;

	/**
	 * Read the request and fill the error data
	 * 
	 * @param httpRequest
	 */
	public ExceptionResult(HttpServletRequest httpRequest) {
		errorCode = httpRequest.getAttribute("javax.servlet.error.status_code").toString();
		if (errorCode != null) {
			switch (errorCode) {
			case "404":
				errorTitle = "Object not found";
				break;
			case "500":
				errorTitle = "Internal server error";
				break;
			}
		}
		errorDescription = httpRequest.getAttribute("javax.servlet.error.message").toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.server.RdapResult#toJson()
	 */
	@Override
	public JsonObject toJson() {
		JsonObjectBuilder object = Json.createObjectBuilder();
		if (errorCode != null) {
			object.add("erroCode", errorCode);
		}
		if (errorTitle != null) {
			object.add("title", errorTitle);
		}
		if (errorCode != null && errorDescription != null) {
			logger.log(Level.WARNING, errorCode + ":" + errorDescription);
		}
		return object.build();
	}

}