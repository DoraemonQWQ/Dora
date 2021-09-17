package top.doraemonqwq.dora.entity.security;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AuthorityTest {

    @Test
    public void getUrlTest() {
        String str = "http://www.baidu.com, https://www.bilibili.com, https://www.google.com";
        String substring = str.substring(0, str.length());
        System.out.println("substring = "+ substring);
        String[] split = substring.split(",");
        for (String s : split) {
            if (s.charAt(0) == ' ') {
                s = s.substring(1,s.length());
            }
            System.out.println(s);
        }
    }

    @Test
    public void setUrlTest() {
        List<String> strings = new ArrayList<>();
        strings.add("http://www.baidu.com");
        strings.add("https://www.bilibili.com");
        strings.add("https://www.google.com");

        String url = "";

        for (int i = 0; i < strings.size(); i++) {
            url += strings.get(i);
            if (strings.size() - 1 > i) {
                url += ", ";
            }
        }

        System.out.println("list转换String后的url = " + url);
    }

}
