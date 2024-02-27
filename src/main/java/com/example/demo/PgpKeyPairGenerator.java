package com.example.demo;

/**
 * @author junhu
 * @date 2024/2/22
 */

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Date;

import cn.hutool.core.codec.Base64;

public class PgpKeyPairGenerator {
    private static final String PROVIDER_BC = "BC";
    private static final String RSA = "RSA";

    static {
        if (Security.getProvider(PROVIDER_BC) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static void main(String[] args) throws Exception {
        String path = "/home/junhu/temp/";
        System.out.println(path);
        String pubKeyFile = path + "PUBLIC_KEY_2048.asc";
        String priKeyFile = path + "PRIVATE_KEY_2048.asc";
        generatePGPKeyPair("", "", 2048, pubKeyFile, priKeyFile, path);
    }

    public static void generatePGPKeyPair(String identity, String passPhrase, int keyWidth,
                                          String pubKeyFile, String priKeyFile, String path) throws Exception {
        createDirectory(path);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA, PROVIDER_BC);
        keyPairGenerator.initialize(keyWidth);
        KeyPair generateKeyPair = keyPairGenerator.generateKeyPair();
        OutputStream priOutputStream;
        OutputStream pubOutputStream;
        try {
            // key format without armored
            priOutputStream = new FileOutputStream(priKeyFile);
            pubOutputStream = new FileOutputStream(pubKeyFile);

            // the pass phrase for open private key
            char[] passPhrase_ = passPhrase.toCharArray();
            // Hash algorithm using SHA1 as certificate
            PGPDigestCalculator sha1Calc = (new JcaPGPDigestCalculatorProviderBuilder()).build().get(HashAlgorithmTags.SHA1);
            // Generate RSA key pair
            JcaPGPKeyPair jcaPGPKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, generateKeyPair, new Date());
            PGPSecretKey pgpSecretKey = new PGPSecretKey(
                    PGPSignature.DEFAULT_CERTIFICATION,
                    jcaPGPKeyPair,
                    identity,
                    sha1Calc,
                    null,
                    null,
                    new JcaPGPContentSignerBuilder(jcaPGPKeyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                    (new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calc)).setProvider(PROVIDER_BC).build(passPhrase_));
            String privateKeyString = new Base64().encode(pgpSecretKey.getEncoded());
            priOutputStream.write(privateKeyString.getBytes());
            PGPPublicKey publicKey = pgpSecretKey.getPublicKey();
            String publicKeyString = new Base64().encode(publicKey.getEncoded());
            pubOutputStream.write(publicKeyString.getBytes());
//            pgpSecretKey.encode(priOutputStream);
//            PGPPublicKey pgpPublicKey = pgpSecretKey.getPublicKey();
//            pgpPublicKey.encode(pubOutputStream);
            close(priOutputStream, pubOutputStream);
        } catch (Exception e) {

        }
    }


    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }

    public static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }


}