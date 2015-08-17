package br.com.altamira.msg;

import java.util.Map;

/**
 * @author Alessandro Holanda
 */
public class MessageToProcess extends GenericMessage<Object> {

    /**
     *
     */
    public MessageToProcess() {
		// TODO Auto-generated constructor stub
	}

    /**
     *
     * @param payload
     */
    public MessageToProcess(Object payload) {
		super(payload);
		// TODO Auto-generated constructor stub
	}

    /**
     *
     * @param payload
     * @param headers
     */
    public MessageToProcess(Object payload, Map<String, Object> headers) {
		super(payload, headers);
		// TODO Auto-generated constructor stub
	}

}
