# BigchianDB-android

This is a basic android app to interact with a [BigchainDB](https://www.bigchaindb.com/) server 2.0. 

This driver is under development but for now you can create and transfer transactions with one input/output. 

The driver do not use the java classes created by the [Authenteq Team](https://github.com/authenteq/java-bigchaindb-driver) It uses a new class called CustomTransaction.java. Shortly I'm going to delete the classes created by [Authenteq](https://github.com/authenteq/java-bigchaindb-driver) that I do not use.

## Android Compatibility
minSdkVersion 26

## Run the App
Clone the Android App.

      git clone https://github.com/GerardoGa/BigchianDB-android/

Modify the Main.java file:
* Set your app_id and app_key if needed.

      BigchainDbConfigBuilder
            .baseUrl("https://test.bigchaindb.com")
            .addToken("app_id", "<your app id>")
            .addToken("app_key", "<your app key id>").setup();
* Change the seed string used to create the Keypair.

      KeyPair keyPair = generatekeypair("Seed"); 

Build and install the app into your Android Device.


## Current options

Currenty there are two buttons in the main view:
* Create Transaction: Creates a new asset in the BigchainDB server using the keypair generated. The button executes this code where <code>dataAsset</code> is the JsonObject with the data of the new asset and <code>metadataAsset</code> is also a JsonObject with the metadata of the transaction.
      
      CustomTransaction transaction = new CustomTransaction();

      transaction
              .addAsset(dataAsset)
              .addMetadata(metadataAsset)
              .addInput((EdDSAPublicKey) keyPair.getPublic()) //origin
              .addOutput((EdDSAPublicKey) keyPair.getPublic()) //destination
              .operation("CREATE")
              .sendJSONTransaction();
              
* Transfer Transaction: Transfers the created asset to yourself. If you keep clicking this button, the asset will be transfered with the new metadata values. The button executes this code where <code>prevTxId</code> is the id of the previous trasaction of the asset that you want to transfer and <code>metadataAsset</code> is the new information of the transaction.

      CustomTransaction transaction = new CustomTransaction(prevTxId);

      transaction.addMetadata(metadataAsset)
              .addInput((EdDSAPublicKey) keyPair.getPublic(), 0) //origin
              .addOutput((EdDSAPublicKey) keyPair.getPublic()) //destination
              .operation("TRANSFER")
              .signTransaction((EdDSAPrivateKey) keyPair.getPrivate())
              .sendJSONTransaction();


