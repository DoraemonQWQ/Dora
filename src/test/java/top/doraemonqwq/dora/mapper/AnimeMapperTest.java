package top.doraemonqwq.dora.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.doraemonqwq.dora.dao.mapper.AnimeMapper;
import top.doraemonqwq.dora.entity.pojo.Anime;

import java.util.List;

@SpringBootTest(classes = top.doraemonqwq.dora.DoraApplication.class)
public class AnimeMapperTest {
    @Autowired
    AnimeMapper animeMapper;

    @Test
    void addAnimeTest() {
        Anime anime = new Anime("测试动漫名称",12,"日本","测试简介","2021",false);
        int i = animeMapper.addAnime(anime);
        System.out.println(i > 0 ? "添加成功": "添加失败");
    }

    @Test
    void selectAnimesByFuzzyTextTest() {
        List<Anime> animeList = animeMapper.selectAnimesByFuzzyText("%测试%");
        for (Anime anime : animeList) {
            System.out.println(anime.getEnd());
        }
    }

}
