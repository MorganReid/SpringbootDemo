package com.example.demo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.IOUtils;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;

import java.security.NoSuchProviderException;
import java.util.UUID;

import org.bouncycastle.openpgp.bc.BcPGPObjectFactory;
import org.bouncycastle.openpgp.bc.BcPGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
import org.springframework.core.io.ClassPathResource;

import static com.google.common.io.ByteStreams.toByteArray;

@Slf4j
public class E2E {

    private static final String PROVIDER_BC = "BC";

    /**
     * Encrypt data to io stream.
     *
     * @param data      - The data to be encrypted.
     * @param out       - The io stream to store the encrypted data.
     * @param fileName  - The output file name.
     * @param publicKey - The stream of public key.
     * @throws IOException
     * @throws PGPException
     */
    public static void encryptToStream(byte[] data, OutputStream out, String fileName, InputStream publicKey) throws IOException, PGPException {

        byte[] compressedData = compressData(data, fileName);

        PGPPublicKey pgpPublicKey = readPublicKey(publicKey);

        PGPEncryptedDataGenerator pgpEncryptedDataGenerator = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5)
                        .setWithIntegrityPacket(true)
                        .setSecureRandom(new SecureRandom())
                        .setProvider(PROVIDER_BC));
        pgpEncryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pgpPublicKey).setProvider(PROVIDER_BC));

        OutputStream encryptOutputStream = pgpEncryptedDataGenerator.open(out, compressedData.length);

        encryptOutputStream.write(compressedData);
        encryptOutputStream.close();
    }

    /**
     * Decrypt the encrypted data to io stream.
     *
     * @param encryptedData        - The data to be decrypted.
     * @param secretKeyInputStream - The secret key.
     * @param out                  - The io stream to store decrypted data.
     * @throws IOException
     */
    public static void decryptDataToStream(InputStream encryptedData, InputStream secretKeyInputStream, OutputStream out, String passphrase) throws Exception {

        try (InputStream pgpEncryptedData = PGPUtil.getDecoderStream(encryptedData)) {
            JcaPGPObjectFactory jcaPGPObjectFactory = new JcaPGPObjectFactory(pgpEncryptedData);
            Object object = jcaPGPObjectFactory.nextObject();

            // the first object might be a PGP marker packet.
            PGPEncryptedDataList pgpEncryptedDataList;
            if (object instanceof PGPEncryptedDataList) {
                pgpEncryptedDataList = (PGPEncryptedDataList) object;
            } else {
                pgpEncryptedDataList = (PGPEncryptedDataList) jcaPGPObjectFactory.nextObject();
            }

            // find the secret key
            Iterator iterator = pgpEncryptedDataList.getEncryptedDataObjects();
            PGPPrivateKey pgpPrivateKey = null;
            PGPPublicKeyEncryptedData pgpPublicKeyEncryptedData = null;
            PGPSecretKeyRingCollection pgpSecretKeyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(secretKeyInputStream), new JcaKeyFingerprintCalculator());

            while (pgpPrivateKey == null && iterator.hasNext()) {
                pgpPublicKeyEncryptedData = (PGPPublicKeyEncryptedData) iterator.next();
                pgpPrivateKey = findSecretKey(pgpSecretKeyRingCollection, pgpPublicKeyEncryptedData.getKeyID(), passphrase.toCharArray());
            }

            if (pgpPrivateKey == null) {
                throw new IllegalArgumentException("secret key for message not found.");
            }

            InputStream pgpPrivateKeyInputStream = pgpPublicKeyEncryptedData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider(PROVIDER_BC).build(pgpPrivateKey));
            JcaPGPObjectFactory pgpPrivateKey_jcaPGPObjectFactory = new JcaPGPObjectFactory(pgpPrivateKeyInputStream);
            Object message = pgpPrivateKey_jcaPGPObjectFactory.nextObject();

            if (message instanceof PGPCompressedData) {
                PGPCompressedData pgpCompressedData = (PGPCompressedData) message;
                JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpCompressedData.getDataStream());

                message = pgpFact.nextObject();
            }

            if (message instanceof PGPLiteralData) {
                PGPLiteralData pgpLiteralData = (PGPLiteralData) message;
                InputStream unc = pgpLiteralData.getInputStream();
                Streams.pipeAll(unc, out);

            } else if (message instanceof PGPOnePassSignatureList) {
                throw new PGPException("encrypted message contains a signed message - not literal data.");
            } else {
                throw new PGPException("message is not a simple encrypted file - type unknown.");
            }

            if (pgpPublicKeyEncryptedData.isIntegrityProtected()) {
                if (!pgpPublicKeyEncryptedData.verify()) {
                    log.error("message failed integrity check");
                } else {
                    log.info("message integrity check passed");
                }
            } else {
                log.error("no message integrity check");
            }
        } catch (PGPException e) {
            log.error(e.getMessage(), e);
            if (e.getUnderlyingException() != null) {
                log.error(e.getUnderlyingException().getMessage(), e.getUnderlyingException());
            }
        }
    }

    public static byte[] compressData(byte[] data, String fileName) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PGPCompressedDataGenerator pgpCompressedDataGenerator = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
        OutputStream compressStream = pgpCompressedDataGenerator.open(byteArrayOutputStream);

        PGPLiteralDataGenerator pgpLiteralDataGenerator = new PGPLiteralDataGenerator();
        // we want to generate compressed data. This might be a user option later,
        OutputStream pgpOutputStream = pgpLiteralDataGenerator.open(compressStream,
                PGPLiteralData.BINARY,
                fileName,
                data.length,
                new Date()
        );

        // in which case we would pass in bOut.
        pgpOutputStream.write(data);
        pgpOutputStream.close();
        compressStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static PGPPublicKey readPublicKey(InputStream keyInputStream) throws IOException, PGPException {
        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
                PGPUtil.getDecoderStream(keyInputStream), new JcaKeyFingerprintCalculator());

        Iterator keyRingIter = pgpPub.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPPublicKeyRing keyRing = (PGPPublicKeyRing) keyRingIter.next();

            Iterator keyIter = keyRing.getPublicKeys();
            while (keyIter.hasNext()) {
                PGPPublicKey key = (PGPPublicKey) keyIter.next();

                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }
        throw new IllegalArgumentException("Can't find encryption key in key ring.");
    }

    public static PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection pgpSecretKeyRingCollection, long keyID, char[] pass) throws PGPException {

        PGPSecretKey pgpSecretKey = pgpSecretKeyRingCollection.getSecretKey(keyID);
        if (pgpSecretKey == null) return null;

        PBESecretKeyDecryptor pbeSecretKeyDecryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(pass);
        return pgpSecretKey.extractPrivateKey(pbeSecretKeyDecryptor);

    }


    private static void encrypt() throws Exception {
        //要加密的文件名
        String dataPath = "template.csv";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dataPath);
        // 读取classes下pgp文件夹的public key
        InputStream pubKey = new ClassPathResource("pgp/PUBLIC_KEY_2048.asc").getInputStream();
        byte[] encData = toByteArray(inputStream);
        //加密后的文件名
        FileOutputStream fileOutputStream = new FileOutputStream("/home/xianzhang/temp/template-encrypt.csv");
        //加密
        encryptToStream(encData, fileOutputStream, "template-encrypt.csv", pubKey);
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
    }

    private static void decrypt() throws Exception {
        InputStream encryptedData = Thread.currentThread().getContextClassLoader().getResourceAsStream("template-encrypt.csv");
        // 读取classes下pgp文件夹的private key
        InputStream secretKey = new ClassPathResource("pgp/PRIVATE_KEY_2048.asc").getInputStream();
        // 解密后的 文件
        FileOutputStream fileOutputStream = new FileOutputStream("/home/xianzhang/temp/template-decrypt.csv");
        decryptDataToStream(encryptedData, secretKey, fileOutputStream, "");
    }

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        encrypt();
        decrypt();
    }


}