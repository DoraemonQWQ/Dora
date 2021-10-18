package top.doraemonqwq.dora.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.doraemonqwq.dora.dao.mapper.ImagesMapper;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;

import java.util.List;

@SpringBootTest(classes = top.doraemonqwq.dora.DoraApplication.class)
public class ImagesMapperTest {
    @Autowired
    ImagesMapper mapper;

    @Test
    void selectImagesAssByUserIdTest() {
        List<ImagePojo> imagePojos = mapper.selectImagesAssByUserId(1010);
        for (ImagePojo imagePojo : imagePojos) {
            System.out.println(imagePojo);
        }
    }

    @Test
    void addImageTest() {
        ImagePojo imagePojo = new ImagePojo("测试图片","C:\\Users\\Doraemon\\Desktop\\开发路径\\images\\IMG_8751.JPG");
        int i = mapper.addImage(imagePojo);
        System.out.println(i > 0);
    }

    @Test
    void addImageUserAssTest() {
        int i = mapper.addImageUserAss(1010, 1);
        System.out.println(i > 0);
    }


}
