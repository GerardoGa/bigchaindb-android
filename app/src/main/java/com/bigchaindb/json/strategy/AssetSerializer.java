package com.bigchaindb.json.strategy;

import com.bigchaindb.model.Asset;
import com.bigchaindb.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AssetSerializer implements JsonSerializer<Asset>
{
	/**
	 *  Serialize an asset object to json object
	 *  Note: given the type of the asset.data it maybe necessary to
	 *  to add a type adapter {@link JsonSerializer} and/or {@link JsonDeserializer} with {@link JsonUtils} and
	 *  {@link com.bigchaindb.util.JsonUtils#addTypeAdapterSerializer}
	 *
	 *  TODO test user.data with custom serializer
	 *
	 * @param src object to serialize
	 * @param typeOfSrc type of src
	 * @param context the json context
	 * @return the json object
	 */
	public JsonElement serialize(Asset src, Type typeOfSrc, JsonSerializationContext context )
	{
		Gson gson = JsonUtils.getGson();
		JsonObject jsonObject = (JsonObject) gson.toJsonTree( src.getData(), src.getDataClass() );
		String s = String.valueOf(jsonObject);

		JsonObject asset = new JsonObject();
		String s2 = s.replace("\\\"","\"");
	//	asset.addProperty("");
		asset.add( "data", gson.toJsonTree( src.getData(), src.getDataClass() ) );
//        asset.add( "type", gson.fromJson("temperature", JsonElement.class));
  //      asset.add( "loc", gson.fromJson("BCN", JsonElement.class));
/*
        Boolean bool = s2.contains("\\");
		int a = s2.indexOf("\\");
       // JSONObject jsonObject12 = gson.fromJson(s2, JSONObject.class);
//		JsonObject jsonElement = gson.toJsonTree(s).getAsJsonObject();



		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse( s2).getAsJsonObject();
		JsonElement p = o.get("asset");//.getAsJsonObject();
		Set<String> keyset = o.keySet();



		Log.d("srcdata",gson.toJson(src.getData(),src.getDataClass()));
		Log.d("srcdataReplaced",s);
		JsonObject jsonObject1 = new JsonObject();


//		for(String key: o.getAsJsonObject("asset").keySet()){
//			jsonObject1.addProperty(key,((JsonObject) gson.toJsonTree(src.getData(), src.getDataClass())).get(key).getAsString());
//		}
//		Log.d("JSONOBJECT1", jsonObject1.toString());
		//asset.add( "data2", DriverUtils.makeSelfSortingGson(s) );
		//asset.add( "data", gson.toJsonTree((String) src.getData(), String.class ) );
	//	Log.d("assetSerializer.class",asset.toString());
	//	Log.d("assetSerializer.class",jsonObject.toString());
		//JsonObject assetRefactorized = new JsonObject(asset.toString());
*/



		JsonObject asset2 = new JsonObject();
		return asset;
	/*

		Gson gson = JsonUtils.getGson();
		JsonObject asset = new JsonObject();
		asset.add( "data", gson.toJsonTree( src.getData(), src.getDataClass() ) );

		return asset;
	*/
	}
}
