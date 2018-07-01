package com.bigchaindb.json.strategy;

import android.util.Log;

import com.bigchaindb.model.Asset;
import com.bigchaindb.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CustomAssetSerializer implements JsonSerializer<Asset>
{
	/**
	 *  Serialize an asset object to json object
	 *  Note: given the type of the asset.data it maybe necessary to
	 *  to add a type adapter {@link JsonSerializer} and/or {@link JsonDeserializer} with {@link JsonUtils} and
	 *  {@link JsonUtils#addTypeAdapterSerializer}
	 *
	 *  TODO test user.data with custom serializer
	 *
	 * @param src object to serialize
	 * @param typeOfSrc type of src
	 * @param context the json context
	 * @return the json object
	 */
	public JsonElement serialize( Asset src, Type typeOfSrc, JsonSerializationContext context )
	{
		Gson gson = JsonUtils.getGson();
		JsonObject asset = new JsonObject();
		JsonObject jsonObject = (JsonObject) gson.toJsonTree( src.getData(), src.getDataClass() );
		String s = String.valueOf(jsonObject).replace("\\", "");
		asset.add( "data", gson.toJsonTree( src.getData(), src.getDataClass() ) );
		//asset.add( "data2", DriverUtils.makeSelfSortingGson(s) );
		//asset.add( "data", gson.toJsonTree((String) src.getData(), String.class ) );
		Log.d("assetSerializer.class",asset.toString());
		Log.d("assetSerializer.class",jsonObject.toString());
		//JsonObject assetRefactorized = new JsonObject(asset.toString());
		return asset;
	}
}
