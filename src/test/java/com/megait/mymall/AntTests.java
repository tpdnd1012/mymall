package com.megait.mymall;

import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import static org.assertj.core.api.Assertions.assertThat;


public class AntTests {
    @Test
    public void antStylePatternTest() {

        assertThat(checkAntPattern("/static/**", "/static/a.jpg")).isTrue();
        assertThat(checkAntPattern("/static/**", "/static/css/a.css")).isTrue();
        assertThat(checkAntPattern("/static/**", "/x/static/css/a.css")).isFalse();


        assertThat(checkAntPattern("/static/**", "/static/a/test.jpg")).isTrue();
        assertThat(checkAntPattern("/static/*", "/static/a/test.jpg")).isFalse();

        //                     "/static/*"      "/static/**"
        //   /static/a              true            true
        //   /static/a.png          true            true
        //   /static/a/b/c          false           true
        //   /static/a/b/c.png      false           true

//        assertThat(true, is(checkAntPattern("/static/**", "/static/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static/**", "/static/css/a.css")));
//        assertThat(true, is(checkAntPattern("/static/**", "/static/js/a.js")));
//        assertThat(true, is(checkAntPattern("/static/**", "/static/img/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static/**", "/static/a/b/c/d/e/f/g/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static/**", "/static")));
//        assertThat(true, is(checkAntPattern("/static/**", "/static/")));
//
//        // single asterisks
//        assertThat(true, is(checkAntPattern("/static/*", "/static/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static/*", "/static/namkyuProfilePicture.jpg")));
//
//        assertThat(false, is(checkAntPattern("/static/*", "/static/a/test.jpg")));
//        assertThat(false, is(checkAntPattern("/static/*", "/static/a/b/c/d/test.jpg")));
//
//        assertThat(true, is(checkAntPattern("/static*/*", "/static/test.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/*", "/static1/test.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/*", "/static123/test.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/*", "/static-123/test.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/*", "/static~!@#$%^&*()_+}{|/test.jpg")));
//
//        assertThat(false, is(checkAntPattern("/static*/*", "/static12/a/test.jpg")));
//        assertThat(false, is(checkAntPattern("/static*/*", "/static12/a/b/test.jpg")));
//
//        // double and single combine
//        assertThat(true, is(checkAntPattern("/static*/**", "/static/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/**", "/static1/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/**", "/static/a/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/**", "/static/a/b/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static*/**", "/static/a/b/c/a.jpg")));
//
//        assertThat(true, is(checkAntPattern("**/static/**", "a/static/a/b/c/a.jpg")));
//        assertThat(true, is(checkAntPattern("**/static/**", "a/b/static/a/b/c/a.jpg")));
//
//
//        // question-mark
//        assertThat(true, is(checkAntPattern("/static-?/**", "/static-a/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static-?/**", "/static-a/b/c/a.jpg")));
//        assertThat(true, is(checkAntPattern("/static-?/*", "/static-a/abcd.jpg")));
//        assertThat(true, is(checkAntPattern("/static-?/???.jpg", "/static-a/abc.jpg")));

    }

    private boolean checkAntPattern(String pattern, String inputStr) {
        return matches(pattern, inputStr);
    }

    public static boolean matches(String pattern, String inputStr) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(pattern, inputStr);
    }
}
