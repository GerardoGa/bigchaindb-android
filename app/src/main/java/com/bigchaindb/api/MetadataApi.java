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
 * The Class MetadataApi.
 */
public class MetadataApi {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger( MetadataApi.class );
	/**
	 * Search by metadata value.
	 *
	 * @param searchKey the search key
	 * @return the assets
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static JsonArray searchMetadata(String searchKey) throws IOException {
		log.debug( "getAssets Call :" + searchKey );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.METADATA + "?search="+ searchKey);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, JsonArray.class);
	}
	
	/**
	 * Search by metadata value with limit.
	 *
	 * @param searchKey the search key
	 * @param limit the limit
	 * @return the assets with limit
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static JsonArray getAssetsWithLimit(String searchKey, String limit) throws IOException {
		log.debug( "getAssets Call :" + searchKey + " limit " + limit );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.METADATA + "?search="+ searchKey+ "&limit=" + limit);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, JsonArray.class);
	}
}
