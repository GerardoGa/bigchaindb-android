package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.constants.BlockStatus;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.Block;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;
import com.google.gson.reflect.TypeToken;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;


/**
 * The Class BlocksApi.
 */
public class BlocksApi {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger( BlocksApi.class );
	
	/**
	 * Gets the block.
	 *
	 * @param blockId the block id
	 * @return the block
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Block getBlock(String blockId) throws IOException { 
		log.debug( "getBlock Call :" + blockId );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.BLOCKS + "/"+ blockId);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Block.class);
	}
	
	/**
	 * Gets the blocks.
	 *
	 * @param transactionId the transaction id
	 * @param status the status
	 * @return the blocks
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> getBlocks(String transactionId, BlockStatus status) throws IOException {
		log.debug( "getBlocks Call :" + transactionId + " status " + status );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.BLOCKS + "?transaction_id="+transactionId+"&status="+status);
		String body = response.body().string();
		response.close();
		return JsonUtils.getGson().fromJson(body, new TypeToken<List<String>>(){}.getType());
	}
	
}
