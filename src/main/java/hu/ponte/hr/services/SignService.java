package hu.ponte.hr.services;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class SignService {

	/**
	 * Egy byte array alapján generál aláírást
	 * 
	 * @param input jelenleg egy kép fájl
	 * @return aláírást
	 * @throws Exception inputStream vagy java.security osztályok
	 */
	public String generateSignature(byte[] input) throws Exception {
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/keys/key.private")){
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(inputStream.readAllBytes());
	        KeyFactory kf = KeyFactory.getInstance("RSA");
	 
	        Signature privateSignature = Signature.getInstance("SHA256withRSA");
	        privateSignature.initSign(kf.generatePrivate(spec));
	        privateSignature.update(input);
	        byte[] s = privateSignature.sign();
	        return Base64.getEncoder().encodeToString(s);
		}
	}
}
