package com.bigchaindb;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bigchaindb.api.MetadataApi;
import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.model.Custom.CustomTransaction;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.util.DriverUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

import java.io.IOException;
import java.security.KeyPair;
import java.util.List;
import java.util.Random;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button createTxButton, transferTxButton;
    String lastTx;

    KeyPair keyPair = generatekeypair("Seed");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        createTxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        CustomTransaction transaction = new CustomTransaction();

                        JsonObject dataAsset = DriverUtils.getSelfSortingJson();
                        dataAsset.addProperty("type", "temperature");
                        dataAsset.addProperty("loc", "Barcelona2");

                        JsonObject metadataAsset = DriverUtils.getSelfSortingJson();
                        metadataAsset.addProperty("value", "22.4");
                        metadataAsset.addProperty("random", String.valueOf(new Random().nextFloat()));

                        transaction
                                .addAsset(dataAsset)
                                .addMetadata(metadataAsset)
                                .addInput((EdDSAPublicKey) keyPair.getPublic()) //origin
                                .addOutput((EdDSAPublicKey) keyPair.getPublic()) //destination
                                .operation("CREATE")
                                .signTransaction((EdDSAPrivateKey) keyPair.getPrivate())
                                .sendJSONTransaction(new GenericCallback() {
                                    @Override
                                    public void pushedSuccessfully(Response response) {
                                        Log.d("pushedSuccessfully", response.toString());
                                        lastTx = transaction.getTransactionId();
                                    }

                                    @Override
                                    public void transactionMalformed(Response response) {
                                        Log.d("transactionMalformed", response.toString());
                                    }

                                    @Override
                                    public void otherError(Response response) {
                                        Log.d("other", response.toString());
                                    }
                                });
                        ;
                    }
                }).start();

            }
        });
        transferTxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("detected", "click");
                new Thread(new Runnable() {
                    public void run() {
                        String prevTxId = lastTx;

                        JsonObject metadataAsset = DriverUtils.getSelfSortingJson();
                        metadataAsset.addProperty("value", "22.4");
                        metadataAsset.addProperty("random", String.valueOf(new Random().nextFloat()));

                        CustomTransaction transaction = new CustomTransaction(prevTxId);

                        transaction.addMetadata(metadataAsset)
                                .addInput((EdDSAPublicKey) keyPair.getPublic(), 0) //origin
                                .addOutput((EdDSAPublicKey) keyPair.getPublic()) //destination
                                .operation("TRANSFER")
                                .signTransaction((EdDSAPrivateKey) keyPair.getPrivate())
                                .sendJSONTransaction(new GenericCallback() {
                                    @Override
                                    public void pushedSuccessfully(Response response) {
                                        Log.d("pushedSuccessfully", response.toString());
                                        lastTx = transaction.getTransactionId();

                                    }

                                    @Override
                                    public void transactionMalformed(Response response) {
                                        Log.d("transactionMalformed", response.toString());

                                    }

                                    @Override
                                    public void otherError(Response response) {
                                        Log.d("other", response.toString());

                                    }
                                });
                    }
                }).start();
            }
        });
    }

    public void init() {
        createTxButton = (Button) findViewById(R.id.create_transaction);
        transferTxButton = (Button) findViewById(R.id.transferTx);

        new Thread(new Runnable() {
            public void run() {
                try {
                    BigchainDbConfigBuilder
                            .baseUrl("https://test.bigchaindb.com")
                            .addToken("app_id", "<your app id>")
                            .addToken("app_key", "<your app key id>").setup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * Generates a Eddsa keypair
     *
     * @param seed: the seed to generate the keypair
     * @return
     */
    public KeyPair generatekeypair(String seed) {

        byte[] sha3Hash = DriverUtils.getSha3HashRaw(seed.getBytes());

        String EDDSA_CURVE_TABLE = "Ed25519";
        EdDSAParameterSpec EDDSA_PARAMETER_SPEC = EdDSANamedCurveTable.getByName(EDDSA_CURVE_TABLE);

        EdDSAPrivateKeySpec keySpec = new EdDSAPrivateKeySpec(sha3Hash, EDDSA_PARAMETER_SPEC);
        EdDSAPrivateKey privateKey = new EdDSAPrivateKey(keySpec);
        EdDSAPublicKey publicKey = new EdDSAPublicKey(new EdDSAPublicKeySpec(privateKey.getAbyte(), EDDSA_PARAMETER_SPEC));

        return new KeyPair(publicKey, privateKey);
    }
}

