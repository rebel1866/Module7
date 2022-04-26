//package com.epam.esm;
//
//
//import com.epam.esm.dao.TagDao;
//
//
//import com.epam.esm.dto.SearchTagRequest;
//import com.epam.esm.dto.TagDto;
//import com.epam.esm.entity.Tag;
//import com.epam.esm.exception.LogicException;
//import com.epam.esm.logic.TagLogic;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = ServiceLayerTest.class)
//class TagLogicImplTest {
//
//    private TagLogic tagLogic;
//
//    @Autowired
//    public void setTagLogic(TagLogic tagLogic) {
//        this.tagLogic = tagLogic;
//    }
//
//    @MockBean
//    private TagDao tagDao;
//    @Captor
//    private ArgumentCaptor<Tag> captor;
//    @Captor
//    private ArgumentCaptor<Map<String, String>> captorMap;
//
//
//    @BeforeEach
//    public void before() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getTagsByParamsTest() {
//        SearchTagRequest request = SearchTagRequest.builder().tag("test").build();
//        tagLogic.findTags(request);
//        Mockito.verify(tagDao).findTags(captorMap.capture());
//        Assertions.assertNotNull(captorMap.getValue().get("tag"));
//    }
//
//    @Test
//    void getTagByIdTest() {
//        Tag tag = Tag.builder().tagName("new").build();
//        Mockito.when(tagDao.findTagById(5)).thenReturn(tag);
//        LogicException logicException = Assertions.assertThrows(LogicException.class, () -> tagLogic.findTagById(-5));
//        Assertions.assertEquals("messageCode10", logicException.getMessage());
//    }
//
//    @Test
//    void addTagTest() {
//        Tag tag = Tag.builder().tagName("new").build();
//        Mockito.when(tagDao.addTag(tag)).thenReturn(tag);
//        TagDto dto = tagLogic.addTag("new");
//        Mockito.verify(tagDao).addTag(captor.capture());
//        assertEquals("new", captor.getValue().getTagName());
//    }
//
//    @Test
//    void deleteTagTest() {
//        LogicException logicException = Assertions.assertThrows(LogicException.class, () -> tagLogic.deleteTag(-5));
//        Assertions.assertEquals("messageCode10", logicException.getMessage());
//    }
//}