package config;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class ChainUtil {

    private static ChainUtil chainUtil;

    //change to private after changes
    public ChainUtil() {}

    public static ChainUtil getInstance() {
        if (chainUtil == null) {
            chainUtil = new ChainUtil();
        }
        return chainUtil;
    }

    public String digitalSignature(String data) {
        Signature dsa = null;
        String signature = null;
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(KeyGenerator.getInstance().getPrivateKey());
            byte[] byteArray = data.getBytes();
            dsa.update(byteArray);
            signature = bytesToHex(dsa.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return signature;
    }

    public boolean signatureVerification(String publicKey, String signature, String data) {
        return verify(KeyGenerator.getInstance().getPublicKey(publicKey),hexStringToByteArray(signature),data);
    }

    public static byte[] sign(PrivateKey privateKey,String data) throws SignatureException {
        //sign the data
        Signature dsa = null;
        try {
            dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(privateKey);
            byte[] byteArray = data.getBytes();
            dsa.update(byteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return dsa.sign();
    }

    public static boolean verify(PublicKey publicKey, byte[] signature, String data) {
        Signature sig = null;
        boolean verification = false;
        try {
            sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(publicKey);
            sig.update(data.getBytes(),0,data.getBytes().length);
            verification = sig.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return verification;
    }

//    public publicKeyEncryption() {
//
//    }

    public static byte[] getHashByteArray(String data) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest.digest(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public String getHash(String data) {
        return bytesToHex(getHashByteArray(data));
    }


    public boolean verifyUser(String peerID, String publicKey) {
        if(peerID.equals(publicKey.substring(0,40))) {
            return true;
        }
        return false;
    }

}