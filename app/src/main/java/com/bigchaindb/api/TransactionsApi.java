package com.bigchaindb.api;

import android.util.Log;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.Transactions;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;
import com.google.gson.JsonObject;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The Class TransactionsApi.
 */
public class TransactionsApi extends AbstractApi {

	private static final Logger log = LoggerFactory.getLogger( TransactionsApi.class );
	
	/**
	 * Send transaction.
	 *
	 * @param transaction
	 *            the transaction
	 * @param callback
	 *            the callback
	 */
	public static void sendTransaction(JsonObject transaction, final GenericCallback callback) {
		log.debug( "sendTransaction Call :" + transaction );
		RequestBody body = RequestBody.create(JSON, transaction.toString());
		NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS, body, callback);
	}

	/**
	 * Sends the transaction.
	 *
	 * @param transaction
	 *            the transaction
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void sendTransaction(JsonObject transaction) throws IOException {
		log.debug( "sendTransaction Call :" + transaction );
		RequestBody body = RequestBody.create(JSON, JsonUtils.toJson(transaction));
		Response response = NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS, body);
		response.close();
	}

	/**
	 * Sends the transaction.
	 *
	 * @param transaction
	 *            the transaction
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void sendJSONTransaction(JsonObject transaction) throws IOException {
		log.debug( "sendTransaction Call :" + transaction.toString() );
		RequestBody body = RequestBody.create(JSON, JsonUtils.toJson(transaction));
		Response response = NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS, body);
		response.close();
	}
	/**
	 * Sends the transaction.
	 *
	 * @param transaction
	 *            the transaction
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void sendJSONTransaction(JsonObject transaction, final GenericCallback callback) throws IOException {
		Log.d("sendTransaction Call :", transaction.toString());
		RequestBody body = RequestBody.create(JSON, JsonUtils.toJson(transaction));
		NetworkUtils.sendPostRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS, body,callback);
	}
	/**
	 * Gets the transaction by id.
	 *
	 * @param id
	 *            the id
	 * @return the transaction by id
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static JsonObject getTransactionById(String id) throws IOException, HttpException {
		Log.d("getTxIdCall", "getTransactionById Call :" + id );
		String url =BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "/" + id;
		Log.d("url",url);
		Response response = NetworkUtils.sendGetRequest(url);
		String body = response.body().string();
		int returnCode = response.code();
		Log.d("response", body);
		response.close();

		if (returnCode == 404){
			throw new HttpException();
		}
		JsonObject tx = JsonUtils.fromJson(body, JsonObject.class);
//		Log.d("JSON", JsonUtils.fromJson(body, JsonObject.class).toString());
		return JsonUtils.fromJson(body, JsonObject.class);
	}

	/**
	 * Gets the transactions by asset id.
	 *
	 * @param assetId
	 *            the asset id
	 * @param operation
	 *            the operation
	 * @return the transactions by asset id
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Transactions getTransactionsByAssetId(String assetId, Operations operation)
			throws IOException {
		log.debug( "getTransactionsByAssetId Call :" + assetId + " operation " + operation );
		Response response = NetworkUtils.sendGetRequest(
				BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "?asset_id=" + assetId + "&operation=" + operation);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Transactions.class);
	}



}
