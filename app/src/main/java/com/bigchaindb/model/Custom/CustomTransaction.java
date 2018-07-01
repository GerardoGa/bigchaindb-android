package com.bigchaindb.model.Custom;

import android.util.Log;

import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Condition;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Fulfillment;
import com.bigchaindb.model.BigChainDBGlobals;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.util.DriverUtils;
import com.bigchaindb.util.KeyPairUtils;
import com.bigchaindb.util.NetworkUtils;
import com.google.api.client.util.Base64;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * The Class Transaction.
 */
public class CustomTransaction {

	public JsonObject transaction;
	public ArrayList<EdDSAPublicKey> public_keys = new ArrayList<>();

	public String previousTransactionId;
	public JsonObject previousTransaction;

	public CustomTransaction() {
		this.transaction = DriverUtils.getSelfSortingJson();
		this.transaction.addProperty("version", "2.0");
		this.transaction.add("id", null);
	}

	public CustomTransaction(String transactionId) {
		this.transaction = DriverUtils.getSelfSortingJson();
		this.transaction.addProperty("version", "2.0");
		this.transaction.add("id", null);

		this.previousTransactionId = transactionId;
		previousTransaction = getTransactionById(transactionId);

		JsonObject asset = DriverUtils.getSelfSortingJson();

		if(previousTransaction.get(Constants.OPERATION).getAsString().equals(Constants.CREATE))
			asset.addProperty(Constants.TX_ID, previousTransaction.get(Constants.TX_ID).getAsString());
		else if(previousTransaction.get(Constants.OPERATION).getAsString().equals(Constants.TRANSFER))
			asset.addProperty(Constants.TX_ID, previousTransaction.getAsJsonObject(Constants.ASSET).get(Constants.TX_ID).getAsString());

		this.transaction.add("asset", asset);

	}

	public CustomTransaction addInput(EdDSAPublicKey origin){
		JsonArray inputsArr = this.transaction.getAsJsonArray("inputs") != null ? this.transaction.getAsJsonArray("inputs") : new JsonArray();
		JsonObject inputs = DriverUtils.getSelfSortingJson();
		JsonArray ownersBefore = new JsonArray();

		ownersBefore.add(KeyPairUtils.encodePublicKeyInBase58(origin));

		inputs.add("owners_before", ownersBefore);
		inputs.add("fulfills", null);
		inputs.add("fulfillment", null);
		inputsArr.add(inputs);
		this.transaction.add("inputs", inputsArr);
		public_keys.add(origin);
		return this;
	}

	public CustomTransaction addInput(EdDSAPublicKey origin, int outputIndex){

		JsonArray inputsArr = this.transaction.getAsJsonArray("inputs") != null ? this.transaction.getAsJsonArray("inputs") : new JsonArray();
		JsonObject inputs = DriverUtils.getSelfSortingJson();
		JsonArray ownersBefore = new JsonArray();
		JsonObject fulfills = DriverUtils.getSelfSortingJson();

		ownersBefore.add(KeyPairUtils.encodePublicKeyInBase58(origin));
		inputs.add("owners_before", ownersBefore);

		fulfills.addProperty(Constants.OUTPUT_INDEX, outputIndex);
		fulfills.addProperty(Constants.TRANSACTION_ID, this.previousTransactionId);
		inputs.add("fulfills", fulfills);
		inputs.add("fulfillment", null);
		inputsArr.add(inputs);

		this.transaction.add("inputs", inputsArr);
        public_keys.add(origin);

        return this;
	}

	public CustomTransaction addOutput(EdDSAPublicKey destination){
		JsonArray outputsArr = this.transaction.getAsJsonArray("outputs") != null ? this.transaction.getAsJsonArray("outputs") : new JsonArray();;

		JsonObject output = DriverUtils.getSelfSortingJson();
		JsonObject details = DriverUtils.getSelfSortingJson();
		JsonObject condition = DriverUtils.getSelfSortingJson();
		Ed25519Sha256Condition condition1 = new Ed25519Sha256Condition(destination);

		JsonArray publicKeys = new JsonArray();
		publicKeys.add(KeyPairUtils.encodePublicKeyInBase58(destination));

		output.add("public_keys", publicKeys);
		details.addProperty("type", "ed25519-sha-256");
		details.addProperty("public_key", KeyPairUtils.encodePublicKeyInBase58(destination));
		condition.add("details", details);
		condition.addProperty("uri", condition1.getUri().toString());
		output.add("condition", condition);

		//TODO check that the sum(amount) less than new amount (when sign we will check if these values are equal)
		output.addProperty("amount", "1");

		outputsArr.add(output);
		this.transaction.add("outputs", outputsArr);

		return this;
	}

	public CustomTransaction addAsset(JsonObject data){
		JsonObject asset = DriverUtils.getSelfSortingJson();
		asset.add("data", data);
		this.transaction.add("asset", asset);
		return this;
	}

	public CustomTransaction addMetadata(JsonObject metadata){
		this.transaction.add("metadata", metadata);
		return this;
	}

	public CustomTransaction operation(String operation){
		this.transaction.addProperty("operation", operation);
		return this;
	}

	public JsonObject getTransaction(){
		return transaction;
	}

	public CustomTransaction build(){
		//TODO check amount

		return this;
	}

	/**
	 * Sign the transaction with the specified private key.
	 *
	 * @param privateKey the private key
	 * @throws InvalidKeyException the invalid key exception
	 * @throws SignatureException the signature exception
	 */
	public CustomTransaction signTransaction(EdDSAPrivateKey privateKey) {
		try {
			// hashing the transaction
			String temp = DriverUtils.makeSelfSortingGson(this.transaction).toString();

			//TODO for each input <-> output
				int index = 0;

				if (getOperation().equals(Constants.TRANSFER)){
					temp += this.previousTransactionId + String.valueOf(index);
				}
				byte[] sha3Hash = DriverUtils.getSha3HashRaw(temp.getBytes());

				// signing the transaction
				Signature edDsaSigner = new EdDSAEngine(MessageDigest.getInstance(Constants.SHA_512));
				edDsaSigner.initSign(privateKey);
				edDsaSigner.update(sha3Hash);
				byte[] signature = edDsaSigner.sign();

				Ed25519Sha256Fulfillment fulfillment = new Ed25519Sha256Fulfillment(public_keys.get(index), signature);
				String s = Base64.encodeBase64URLSafeString(fulfillment.getEncoded());

				JsonObject inputs = this.transaction.get(Constants.INPUTS).getAsJsonArray().get( index ).getAsJsonObject();

				//inputs.remove(Constants.FULFILLMENT);
				inputs.addProperty(Constants.FULFILLMENT, Base64.encodeBase64URLSafeString(fulfillment.getEncoded()));

			String id = DriverUtils.getSha3HashHex(DriverUtils.makeSelfSortingGson(this.transaction).toString().getBytes());

			// putting the hash as id field
			this.transaction.addProperty(Constants.TX_ID, id);

			Log.d("FINAL TX", transaction.toString());
			return  this;
		} catch ( Exception e) {
			e.printStackTrace();
			return  null;
		}
	}

	/**
	 * send the transaction to the BigchainDB.
	 *
	 */
	public void sendJSONTransaction() {
		try {
			TransactionsApi.sendJSONTransaction(this.transaction);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * send the transaction to the BigchainDB.
	 *
	 */
	public void sendJSONTransaction(final GenericCallback callback) {
		try {
			TransactionsApi.sendJSONTransaction(this.transaction, callback);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JsonObject getTransactionById(String id) {
		try {
			Log.d("getTxIdCall", "getTransactionById Call :" + id);
			String url = BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "/" + id;
			Log.d("url", url);
			Response response = NetworkUtils.sendGetRequest(url);
			String body = response.body().string();
			Log.d("response", body);
			response.close();
			if (response.code() == 404) {
				return null;
			}
			JsonParser jsonParser = new JsonParser();
			//Transaction tx = JsonUtils.fromJson(body, Transaction.class);
//		Log.d("JSON", JsonUtils.fromJson(body, Transaction.class).toString());
			JsonObject jsonObject = jsonParser.parse(body).getAsJsonObject();
			return jsonObject;
		}
		catch (IOException e ){
			return null;
		}
	}

	public String getOperation() {
		return this.transaction.get("operation").getAsString();
	}

	public JsonArray getInputs() {
		return this.transaction.get("inputs").getAsJsonArray();
	}
	public JsonArray getOutputs() {
		return this.transaction.get("outputs").getAsJsonArray();
	}
	public String getTransactionId() {
		return this.transaction.get("id").getAsString();
	}
}
