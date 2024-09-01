package org.tinycode.pay.alipayglobal.utils;

/**
 * Created by littlehui on 2020/3/26.
 */
public class BpKeyUtils {

    /**
     * 折叠
     * @param bpKey
     * @return
     */
    public static String encry(String bpKey) {
        if (bpKey.length() != 32) {
            return bpKey;
        }
        char[] chars = bpKey.toCharArray();
        char[] charsPre = bpKey.substring(0, 16).toCharArray();
        char[] charsSub = bpKey.substring(16, 32).toCharArray();
        char[] newChars = new char[32];
        for (int i=0; i < 16; i++) {
            newChars[2*i] = charsPre[i];
            newChars[2*i + 1] = charsSub[i];
        }
        return new String(newChars);
    }

    /**
     * 还原
     * @param bpKey
     * @return
     */
    public static String decry(String bpKey) {
        if (bpKey.length() != 32) {
            return bpKey;
        }
        char[] chars = bpKey.toCharArray();
        char[] charsPre = new char[16];
        char[] charsSub = new char[16];
        for (int i = 0; i < 16; i++) {
            charsPre[i] = chars[2*i];
            charsSub[i] = chars[2*i + 1];
        }
        return new String(charsPre) + new String(charsSub);
    }

    public static void main(String[] args) {
/*        String bpKey = "c05045c0faf630a360273da854d43422";
        System.out.println("origin:" + bpKey);
        String result = BpKeyUtils.encry(bpKey);
        System.out.println("encry:" + result);
        String decResult = BpKeyUtils.decry(result);
        System.out.println("decry:" + decResult);
        System.out.println(bpKey.equals(decResult));*/

        String bpKey = "0e42fdc4f0be6209bb6b97e07b5f24c7";
        System.out.println("decry:" + BpKeyUtils.decry(bpKey));

    }
}
