package com.epam.esm;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DaoLayerTest.class)
@Transactional
public class TagDaoImplTest {

    private JdbcTemplate jdbcTemplate;
    private TagDao tagDao;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @BeforeEach
    public void before() throws SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(),
                new ClassPathResource("scripts.create/create_db.sql"));
    }

    @Test
    public void addTagTest() throws DaoException {
        Tag tag = Tag.builder().tagName("new test").build();
        Tag addedTag = tagDao.addTag(tag);
        Assertions.assertEquals("new test", addedTag.getTagName());
    }

    @Test
    public void getTagByParamsAndByIdTest() throws DaoException {
        Tag tag = Tag.builder().tagName("TEST").build();
        tagDao.addTag(tag);
        Map<String, String> params = new HashMap<>();
        params.put("tag", "TEST");
        Tag derivedTag = tagDao.findTags(params).get(0);
        int id = derivedTag.getTagId();
        Tag derivedTag2 = tagDao.findTagById(id);
        Assertions.assertEquals("TEST", derivedTag2.getTagName());
    }

    @Test
    public void deleteTagTest() throws DaoException {
        Tag tag = Tag.builder().tagName("new tag").build();
        tagDao.addTag(tag);
        Map<String, String> params = new HashMap<>();
        params.put("tag", "new tag");
        Tag derivedTag = tagDao.findTags(params).get(0);
        int id = derivedTag.getTagId();
        tagDao.deleteTag(id);
        DaoException thrown = Assertions.assertThrows(DaoException.class, () -> tagDao.findTagById(id));
        Assertions.assertEquals("WmessageCode7:2", thrown.getMessage());
    }

    @AfterEach
    public void after() {
        JdbcTestUtils.dropTables(jdbcTemplate, "gifts.cert_tags", "gifts.tags", "gifts.gift_certificates");
    }
}
