package br.com.altamira.msg;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alessandro Holanda
 * @param <T>
 */
public abstract class GenericMessage<T> {

	private Map<String, Object> headers;

	private T payload;

	/**
	 * 
	 */
	public GenericMessage() {
	}

	/**
	 * 
	 * @param payload
	 */
	public GenericMessage(T payload) {
		this.payload = payload;
		this.headers = new HashMap<String, Object>();
	}

	/**
	 * 
	 * @param payload
	 * @param headers
	 */
	@JsonCreator
	public GenericMessage(
			@JsonProperty("payload") T payload,
			@JsonProperty("headers") Map<String, Object> headers) {
		this.payload = payload;
		this.headers = headers;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getHeaders() {
		return headers;
	}

	/**
	 * 
	 * @param headers
	 */
	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	/**
	 * 
	 * @return
	 */
	public T getPayload() {
		return payload;
	}

	/**
	 * 
	 * @param payload
	 */
	public void setPayload(T payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

}
