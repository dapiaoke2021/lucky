package io.renren;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.io.*;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2020-01-09 09:53
 */
public class LogTest {

    private static String  privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ0m87tfr+m1HvLB"
    +"iUkZtHGrKU5ue56u0aTUmeyk3u1zZuQSPCOHd6hPu4nSLa7bYTmieE+Gb8rwJbqa"
    +"u0aOno+arSG33GhavP8vA/SSa9mFFkHjtNcUaZVlOtHfTsvs6mnLb5RTKptIEzOz"
    +"XD27j/eSq1MsGgrCa/YtTjJX1gQPAgMBAAECgYBUhPGK8cCbkhTN/LbIQPHiPGrf"
    +"yt3jjyQjYVBTjKx0yp8oxIHhnecF97PmQMrfAw/8Plw0cRpI6/Vuse9M2EGAJJxq"
    +"94H56dpPhhMLvS/uRgWIAGhpT3cSi0HBuNY9VEXSzprn7fO1pGdRYGVRIq7am3+e"
    +"jTrB+k3nfI8InLXO2QJBAM4iOz5okJWGr/SOou/M6GLZSDkX9Qi3WQuuhTrNTzFV"
    +"15cnZuIEp9WiKjCfORwcJL6HGOLKVP0P/XKH+Sj9DPUCQQDDK1CLU8YFUGNm+fya"
    +"8jz/Fid5jimyRWJ4lopYUSD3yFHSVbY/20/PZr89tJqGR2LfsCZK13Rd7WS8F6e8"
    +"RSpzAkAMaU874Lvj5OovRW9WFPZhDUgl9+VPEwsPgwCOm9IK3GpQtZSiQzl/yXXU"
    +"26Fqqd8kganj7d7UJeRSwxEjgKkJAkEAmdfu6aTjlxTDBk1QPaNtSXZhL4RMgeYH"
    +"tR6VdwCciDUzqiU+QB/UTZykazOOCwMCgWkNBjnH1LJokYvkAJhU2QJBAKJRUvns"
    +"sYenn3k/dO0H7MZtkbHLQdMj3tNjYLNwhbh1Bv6IhveTFwAY0OEtYa52nRAu95Kw"
            +"KrZw1n9Cxo19XZE=";

    public static void main(String[] args) throws IOException, InterruptedException {


        String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ0m87tfr+m1HvLB"
        + "iUkZtHGrKU5ue56u0aTUmeyk3u1zZuQSPCOHd6hPu4nSLa7bYTmieE+Gb8rwJbqa"
        + "u0aOno+arSG33GhavP8vA/SSa9mFFkHjtNcUaZVlOtHfTsvs6mnLb5RTKptIEzOz"
        + "XD27j/eSq1MsGgrCa/YtTjJX1gQPAgMBAAECgYBUhPGK8cCbkhTN/LbIQPHiPGrf"
        + "yt3jjyQjYVBTjKx0yp8oxIHhnecF97PmQMrfAw/8Plw0cRpI6/Vuse9M2EGAJJxq"
        + "94H56dpPhhMLvS/uRgWIAGhpT3cSi0HBuNY9VEXSzprn7fO1pGdRYGVRIq7am3+e"
        + "jTrB+k3nfI8InLXO2QJBAM4iOz5okJWGr/SOou/M6GLZSDkX9Qi3WQuuhTrNTzFV"
        + "15cnZuIEp9WiKjCfORwcJL6HGOLKVP0P/XKH+Sj9DPUCQQDDK1CLU8YFUGNm+fya"
        + "8jz/Fid5jimyRWJ4lopYUSD3yFHSVbY/20/PZr89tJqGR2LfsCZK13Rd7WS8F6e8"
        + "RSpzAkAMaU874Lvj5OovRW9WFPZhDUgl9+VPEwsPgwCOm9IK3GpQtZSiQzl/yXXU"
        + "26Fqqd8kganj7d7UJeRSwxEjgKkJAkEAmdfu6aTjlxTDBk1QPaNtSXZhL4RMgeYH"
        + "tR6VdwCciDUzqiU+QB/UTZykazOOCwMCgWkNBjnH1LJokYvkAJhU2QJBAKJRUvns"
        + "sYenn3k/dO0H7MZtkbHLQdMj3tNjYLNwhbh1Bv6IhveTFwAY0OEtYa52nRAu95Kw"
        + "KrZw1n9Cxo19XZE=";

        RSA rsa = new RSA(PRIVATE_KEY, null);

        String a = "SZubUyvmL7E0kZsB+4eYMF/4AukJvJVVERtsvVzDbOn3Na10lyqdOWMM2V3fASAkQZKfONQgyAUVW4zf2lC8ojdsDvjQLtqUIsuQjG1D67jJZU96VDyfsmKWOn7nn/mwx43Pnm97BqJccUB9PXgtdljywlZVk6R5Wp4OCm0c6as===XRgnvcizWusqLxMZLutzQFQLEzI3PMeGJjxICsq9+0lX6ik2JC7w9PwFJta8zcTi2k3E3cGuFMcv+hvnswe2GdDx6+tzZid04GRxSB6eVMdz5dp2/Ep0Q7FSNa6NZlnWepGKrtf1I2hGHRBi4SNx452G7twbpx8akBM1h+SdL9k===f5FylGIRNpWz/8R2b2nWQk7VnC6xbyLugHPJMSGwuQAaTRNRmJWoCi8sWIPTzeBpbwqM+Pb67aDcwlshAiOjzMR12bgTcT+OKZ8b8k18Y/18YMiVoHOs3/BQNpCjnI1zpnkucXqiULx5b5EmHyBR/ljNZ3TFyRzbux0FcioqT54===OyMkQHx6Ts9jqgWN8wP8RPpAg8H9A0pBL+OwSmzWUhEMYAKbQfTEDZdnzMt3kKnIK1ksBi7s8k0WcR9uNdNJKFw9AVKSXo8JYCm+WjHIhZDgT77vJJYZ0xCTVhbz2mwKFkyCOeqb85OCysX3V8Btxeo+yAt3LDZx2qhazrhSt1M===lFKivotr+seCJnvnawDBXjxlKazYqFpx/kxr6X/HG3dNJCDfIVIRIfpzSZCQGtIOOvxIUM7Ps9dA1jpJJ61INdUT/ZsUL73gxsPQljqoY09/IqfxsYsXXtAe3709S4pJSnAbi/gANPjtnPa/dgZlQKXpREN60s4fL0pfHgQnQmY===PFS5XaT+tPbaHdFiMQLyyAmWntMuWW3JdvpHMRuydIa98z7yga5XSThPr1lwPeORdzlwTBkycwaYjgI7O2A3ahUKUI3xJBgi/DT0DxqstfQTQN3Uwcx4JxN4tIDitLWxmHhe7Cvt4AZyPhooRZsQFkIgT0xfrGwLCZAsTjDRCf8===";





        byte[] bytes = rsa.decryptFromBcd(a, KeyType.PrivateKey);

    }

}
