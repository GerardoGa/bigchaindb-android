package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;
import com.google.gson.JsonArray;

import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Response;

/**
 * The Class OutputsApi.
 */
public class OutputsApi {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger( OutputsApi.class );
	/**
	 * Gets the outputs.
	 *
	 * @param publicKey the public key
	 * @return the outputs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static JsonArray getOutputs(String publicKey) throws IOException {
		log.debug( "getOutputs Call :" + publicKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.OUTPUTS + "?public_key="+ publicKey);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, JsonArray.class);
	}
	
	/**
	 * Gets the spent outputs.
	 *
	 * @param publicKey the public key
	 * @return the spent outputs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static JsonArray getSpentOutputs(String publicKey) throws IOException {
		log.debug( "getSpentOutputs Call :" + publicKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.OUTPUTS + "?public_key="+ publicKey+ "&spent=true");
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, JsonArray.class);
	}
	/**
	 * Gets the spent outputs.
	 *
	 * @param publicKey the public key
	 * @return the spent outputs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static JsonArray getUnSpentOutputs(String publicKey) throws IOException {
		log.debug( "getUnSpentOutputs Call :" + publicKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.OUTPUTS + "?public_key="+ publicKey+ "&spent=false");
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, JsonArray.class);
	}
	
}
