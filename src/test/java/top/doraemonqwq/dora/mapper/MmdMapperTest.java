package top.doraemonqwq.dora.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.doraemonqwq.dora.dao.mapper.MmdMapper;
import top.doraemonqwq.dora.entity.pojo.MmdPojo;

import java.util.List;

@SpringBootTest(classes = top.doraemonqwq.dora.DoraApplication.class)
public class MmdMapperTest {

    @Autowired
    MmdMapper mmdMapper;

    @Test
    void addMmdTest() {
        MmdPojo mmdPojo = new MmdPojo("测试mmd","测试mmd简介");
        int i = mmdMapper.addMmd(mmdPojo);
        System.out.println(i > 0);
    }

    @Test
    void selectMmdsByFuzzyTextTest(){
        List<MmdPojo> mmdPojos = mmdMapper.selectMmdsByFuzzyText("测试");
        System.out.println(mmdPojos);
    }
}
