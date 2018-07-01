package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.Status;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;

import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Response;

/**
 * The Class StatusesApi.
 */
public class StatusesApi {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger( StatusesApi.class );

	/**
	 * Gets the transaction status.
	 *
	 * @param transactionId the transaction id
	 * @return the transaction status
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Status getTransactionStatus(String transactionId) throws StatusException, IOException
	{
		log.debug( "getTransactionStatus Call :" + transactionId );
		try( Response response = NetworkUtils.sendGetRequest( BigChainDBGlobals.getBaseUrl() + BigchainDbApi.STATUSES + "?transaction_id=" + transactionId ) ) {
			if( response.code() == 200 ) {
				String body = response.body().string();
				return JsonUtils.fromJson( body, Status.class );
			}
			throw new StatusException( response.code(), response.body() != null ? response.body().toString() : "Error in response, body is empty" );
		}
	}
	
	/**
	 * Gets the block status.
	 *
	 * @param blockId the block id
	 * @return the block status
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Status getBlockStatus(String blockId) throws StatusException, IOException {
		log.debug( "getBlockStatus Call :" + blockId );
		try( Response response = NetworkUtils.sendGetRequest( BigChainDBGlobals.getBaseUrl() + BigchainDbApi.STATUSES + "?block_id=" + blockId ) ) {
			if( response.code() == 200 ) {
				String body = response.body().string();
				return JsonUtils.fromJson( body, Status.class );
			}
			throw new StatusException( response.code(), response.body() != null ? response.body().toString() : "Error in response, body is empty" );
		}
	}
}
                                                               