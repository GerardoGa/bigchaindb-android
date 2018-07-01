# BigchianDB-android

This is a basic android app to interact with a BigchainDB server. 

This driver is under development but for now you can create and transfer transactions with one input/output.

## Android Compativility
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
            </code>                         
* Change the seed string used to create the Keypair.

      KeyPair keyPair = generatekeypair("Seed"); 

Build and install the app into your Android Device.

## Current options

Currenty there are two buttons in the main view:
* Create Transaction: Creates a new asset in the BigchainDB server using the keypair generated.
* Transfer Transaction: Transfers the created asset to yourself. If you keep clicking this button, the asset will be transfered with the new metadata values.


