package org.example.book.springboot.web;

import org.example.book.springboot.domain.posts.Posts;
import org.example.book.springboot.domain.posts.PostsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Test
    public void 메인페이지_로딩() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
        assertThat(body).contains("스프링부트로 시작하는 웹 서비스");
    }

    @Test
    public void 등록페이지_로딩() {
        String body = this.restTemplate.getForObject("/posts/save", String.class);

        assertThat(body).contains("게시글 등록");
    }

    @Test
    public void 수정페이지_로딩() {
        //given
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Long id = postsList.get(0).getId();

        String url = "/posts/update/" + id;
        String body = this.restTemplate.getForObject(url, String.class);
        System.out.println(">>>>>>>>>>>> " + body);
        assertThat(body).contains("게시글 수정");
        assertThat(body).contains("title");
        assertThat(body).contains("content");
        assertThat(body).contains("author");
        assertThat(body).contains("수정 완료");
    }
}
