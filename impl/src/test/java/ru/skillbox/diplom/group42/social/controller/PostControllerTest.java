package ru.skillbox.diplom.group42.social.controller;

import org.junit.jupiter.api.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.skillbox.diplom.group42.social.service.dto.post.PostDto;
import ru.skillbox.diplom.group42.social.service.dto.post.PostSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentDto;
import ru.skillbox.diplom.group42.social.service.dto.post.comment.CommentSearchDto;
import ru.skillbox.diplom.group42.social.service.dto.post.like.LikeDto;
import ru.skillbox.diplom.group42.social.service.dto.tag.TagDto;
import ru.skillbox.diplom.group42.social.util.CustomPageImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.skillbox.diplom.group42.social.util.MappingTestingDataFactory.*;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createCommentSearchDto;
import static ru.skillbox.diplom.group42.social.util.ServiceTestingDataFactory.createPostSearchDto;
import static ru.skillbox.diplom.group42.social.util.TemplateUtil.createTestPostWithTags;
import static ru.skillbox.diplom.group42.social.util.TestingConstant.TEST_ID;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostControllerTest extends AbstractIntegrationTest{
    private static PostDto post;
    private static CommentDto comment;

    public static void setPost(PostDto post) {
        PostControllerTest.post = post;
    }

    public static PostDto getPost() {
        return post;
    }

    public static CommentDto getComment() {
        return comment;
    }

    public static void setComment(CommentDto comment) {
        PostControllerTest.comment = comment;
    }

    @BeforeAll
    public static void initEmail(){
        setEmailForCreateAccount("post@mail.ru");
    }

    @Test
    @Order(1)
    void createPostReturnsPostDto() {
        PostDto postDto = createPostDto(TEST_ID);
        postDto.setTags(Set.of());
        ResponseEntity<PostDto> response = template.postForEntity("/api/v1/post", new HttpEntity<>(postDto, headers), PostDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assert response.getBody() != null;
        setPost(response.getBody());
    }

    @Test
    @Order(2)
    void createComment() {
        CommentDto commentDto = createCommentDto(TEST_ID);
        commentDto.setParentId(null);
        ResponseEntity<CommentDto> response = template.exchange("/api/v1/post/{postId}/comment", HttpMethod.POST,
                new HttpEntity<>(commentDto, headers), CommentDto.class, getPost().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
        setComment(response.getBody());
    }

    @Test
    @Order(3)
    void createSubCommentReturnsCommentDto() {
        CommentDto commentDto = createCommentDto(TEST_ID);
        commentDto.setParentId(getComment().getId());
        Map<String, Long> variables = new HashMap<>();
        variables.put("id", getPost().getId());
        variables.put("commentId", getComment().getId());
        ResponseEntity<CommentDto> response = template.exchange("/api/v1/post/{id}/comment/{commentId}", HttpMethod.PUT,
                new HttpEntity<>(commentDto, headers), CommentDto.class, variables);
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(4)
    void createPostReactionReturnsLikeDto() {
        CommentSearchDto commentSearchDto = createCommentSearchDto();
        ResponseEntity<LikeDto> response = template.exchange("/api/v1/post/{id}/like", HttpMethod.POST,
                new HttpEntity<>(commentSearchDto, headers), LikeDto.class, getPost().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(5)
    void createCommentLikeReturnsLikeDto() {
        LikeDto likeDto = createLikeDto(TEST_ID);
        Map<String, Long> variables = new HashMap<>();
        variables.put("id", getPost().getId());
        variables.put("commentId", getComment().getId());
        ResponseEntity<LikeDto> response = template.exchange("/api/v1/post/{id}/comment/{commentId}/like", HttpMethod.POST,
                new HttpEntity<>(likeDto, headers), LikeDto.class, variables);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(6)
    void getAllTagsReturnsTagDto() {
        ResponseEntity<PostDto> expected = createTestPostWithTags(template, headers);
        assert expected.getBody() != null;
        assert expected.getBody().getId() != null;
        ResponseEntity<TagDto> response = template.exchange("/api/v1/tag?name=Test TAG 6", HttpMethod.GET, new HttpEntity<>(headers), TagDto.class);
        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(7)
    void getByIdReturnsPostDto() {
        ResponseEntity<PostDto> response = template.exchange("/api/v1/post/{id}", HttpMethod.GET,
                new HttpEntity<>(headers), PostDto.class, getPost().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(8)
    void getAllReturnsPostDto() {
        PostSearchDto postSearchDto = createPostSearchDto();
        ResponseEntity<CustomPageImpl<PostDto>> response = template.exchange("/api/v1/post", HttpMethod.GET,
                new HttpEntity<>(postSearchDto, headers), new ParameterizedTypeReference<>() {});
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(9)
    void getAllCommentsToPostReturnsCommentDto() {
        ResponseEntity<CustomPageImpl<CommentDto>> response = template.exchange("/api/v1/post/{postId}/comment", HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<>() {}, getPost().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(10)
    void getSubcommentReturnsCommentDto() {
        CommentSearchDto commentSearchDto = createCommentSearchDto();
        Map<String, Long> variables = new HashMap<>();
        variables.put("postId", getPost().getId());
        variables.put("commentId", TEST_ID);
        ResponseEntity<CustomPageImpl<CommentDto>> response = template.exchange("/api/v1/post/{postId}/comment/{commentId}/subcomment", HttpMethod.GET,
                new HttpEntity<>(commentSearchDto, headers), new ParameterizedTypeReference<>() {}, variables);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(11)
    void updatePostReturnsPostDto() {
        PostDto toUpdate = getPost();
        toUpdate.setPostText("new text");
        assert toUpdate != null;
        ResponseEntity<PostDto> response = template.exchange("/api/v1/post", HttpMethod.PUT,
                new HttpEntity<>(toUpdate, headers), PostDto.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(
                MediaType.APPLICATION_JSON_VALUE,
                Objects.requireNonNull(response.getHeaders().getContentType()).toString()
        );
        assert response.getBody() != null;
    }

    @Test
    @Order(12)
    void updateComment() {
        CommentDto toUpdate = getComment();
        toUpdate.setCommentText("new comment text");
        assert toUpdate != null;
        ResponseEntity<PostDto> response = template.exchange("/api/v1/post/{id}/comment", HttpMethod.PUT,
                new HttpEntity<>(toUpdate, headers), PostDto.class, getComment().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(13)
    void deletePostReactionHasStatusOk() {
        ResponseEntity response = template.exchange("/api/v1/post/{id}/like", HttpMethod.DELETE,
                new HttpEntity<>(headers), ResponseEntity.class, getPost().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(14)
    void deleteCommentLikeHasStatusOk() {
        Map<String, Long> variables = new HashMap<>();
        variables.put("id", getPost().getId());
        variables.put("commentId", getComment().getId());
        ResponseEntity response = template.exchange("/api/v1/post/{id}/comment/{commentId}/like", HttpMethod.DELETE,
                new HttpEntity<>(headers), ResponseEntity.class, variables);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(15)
    void deleteCommentById() {
        Map<String, Long> variables = new HashMap<>();
        variables.put("id", getPost().getId());
        variables.put("commentId", getComment().getId());
        ResponseEntity response = template.exchange("/api/v1/post/{id}/comment/{commentId}", HttpMethod.DELETE,
                new HttpEntity<>(headers), ResponseEntity.class, variables);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(16)
    void deleteByIdHasStatusOk() {
        ResponseEntity response = template.exchange("/api/v1/post/{id}", HttpMethod.DELETE,
                new HttpEntity<>(headers), ResponseEntity.class, getPost().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}