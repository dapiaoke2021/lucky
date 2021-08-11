package io.renren.common.utils;

import cn.hutool.core.io.IoUtil;
import io.renren.common.utils.ReaderPem.PemObject;
import io.renren.common.utils.ReaderPem.PemReader;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * rsa解密java实现
 *
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2020-09-23 11:35:17
 */
@Component
public class Decrypt {

    public String getDecryptString(String cipherText) {
        PrivateKey privateKey = null;
        byte[] cipherData = null;
        try {
            privateKey = getPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipherData = cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return new String(cipherData);
    }

    private PrivateKey getPrivateKey() throws IOException, GeneralSecurityException {
        BufferedReader privKeyPEM = getPrivateKeyFromPEMfile();

        return generatePrivateKeyFromPem(privKeyPEM);
    }

    private BufferedReader getPrivateKeyFromPEMfile() throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("rsa/private_key.pem");
        BufferedReader reader = IoUtil.getReader(resourceAsStream, "utf-8");
        return new BufferedReader(reader);
    }

    private PrivateKey generatePrivateKeyFromPem(BufferedReader privateKeyPem) throws IOException, GeneralSecurityException {
        PemReader pp = new PemReader(privateKeyPem);

        PemObject pem = pp.readPemObject();
        byte[] content = pem.getContent();
        pp.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);

        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(spec);
    }

}
