package com.bigchaindb.api;

import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.Votes;
import com.bigchaindb.util.JsonUtils;
import com.bigchaindb.util.NetworkUtils;

import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Response;

/**
 * The Class VotesApi.
 */
public class VotesApi {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger( VotesApi.class );

	/**
	 * Gets the votes.
	 *
	 * @param blockId the block id
	 * @return the votes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Votes getVotes(String blockId) throws IOException {
		log.debug( "getVotes Call :" + blockId );
		Response response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.VOTES + "?block_id=" + blockId);
		String body = response.body().string();
		response.close();
		return JsonUtils.fromJson(body, Votes.class);
	}

}
