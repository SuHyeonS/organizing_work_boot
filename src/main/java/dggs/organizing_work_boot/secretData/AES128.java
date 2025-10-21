package dggs.organizing_work_boot.secretData;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64; //java 8 부터 추가됨
//import org.apache.commons.codec.binary.Base64;
import java.util.Optional;
import java.util.function.Predicate;

/*
* //암호화 대칭키
        AES128 aes128 = new AES128("1234567891011120");
        String enc = null;
        String dec = null;
        try {
            enc = aes128.encrypt(sample.getSamplePw());
            dec = aes128.decrypt(enc);
        }catch (Exception e){
            e.printStackTrace();
        }

        log.info(enc);
        log.info(dec);
        if(enc == null && dec == null){
            return  -1L;
        }
* */

public class AES128 {
    private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8; // 인코딩
    private static final String INSTANCE_TYPE = "AES/CBC/PKCS5Padding"; // 암호화 방식

    private final SecretKeySpec secretKeySpec; // 양방향 키
    private final IvParameterSpec ivParameterSpec; // 초기화 벡터
    private final Cipher cipher; // 공통키 AES 암호화 클래스


    //암호화키
    public AES128(final String Key) {
        validation(Key);
        try {
            byte[] keyBytes = Key.getBytes(ENCODING_TYPE); //utf-8로 인코딩후 byte로 변환
            secretKeySpec = new SecretKeySpec(keyBytes, "AES"); //암호화 키 객체생성

            // key 앞 16바이트를 IV로 사용 (보통은 별도의 랜덤 IV를 쓰지만, 고정 IV도 가능)
            ivParameterSpec = new IvParameterSpec(Arrays.copyOfRange(keyBytes, 0, 16));

            cipher = Cipher.getInstance(INSTANCE_TYPE); //생성 인스턴스화
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            //e.printStackTrace();
            throw new RuntimeException("AES128 초기화 오류", e);
        }
    }

    //암호화
    public String encrypt(final String str) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec); //ENCRYPT_MODE = 객체를 암호화 모드로 초기화한다.
        byte[] encrypted = cipher.doFinal(str.getBytes(ENCODING_TYPE)); //암호화
        //return new String(Base64.getEncoder().encode(encrypted), ENCODING_TYPE); //Base64 는 데이터를 보낼때 손실을 줄이기위해
        return Base64.getEncoder().encodeToString(encrypted);

    }

    //복호화
    public String decrypt(final String str) throws Exception{
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec); //DECRYPT_MODE = 객체를 복호화 모드로 초기화한다.
        //byte[] decoded = Base64.getDecoder().decode(str.getBytes(ENCODING_TYPE));
        //return new String(cipher.doFinal(decoded), ENCODING_TYPE); //디코딩

        byte[] decoded = Base64.getDecoder().decode(str);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, ENCODING_TYPE);
    }

    //길이 체크
    private void validation(final String key){
        Optional.ofNullable(key)
                .filter(Predicate.not(String::isBlank))
                //.filter(Predicate.not(s -> s.length() != 16))
                //.orElseThrow(IllegalArgumentException :: new);
                .filter(k -> k.length() == 16)
                .orElseThrow(() -> new IllegalArgumentException("AES 키는 16자리여야 합니다."));

    }
}
