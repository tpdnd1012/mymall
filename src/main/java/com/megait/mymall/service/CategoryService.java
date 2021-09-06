package com.megait.mymall.service;

import com.megait.mymall.domain.Category;
import com.megait.mymall.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void createCategories() {
        log.info("createCategories()");
        Category c1 = Category.builder().id(1L).name("도서").parent(null).build();
        Category c2 = Category.builder().id(2L).name("음반").parent(null).build();

        Category c3 = Category.builder().id(3L).name("국내도서").parent(c1).build();
        Category c4 = Category.builder().id(4L).name("해외도서").parent(c1).build();

        Category c5 = Category.builder().id(5L).name("KPOP").parent(c2).build();
        Category c6 = Category.builder().id(6L).name("POP").parent(c2).build();
        Category c7 = Category.builder().id(7L).name("클래식").parent(c2).build();

        Category c8 = Category.builder().id(8L).name("소설").parent(c3).build();
        Category c9 = Category.builder().id(9L).name("자기계발/교양").parent(c3).build();
        Category c10 = Category.builder().id(10L).name("교육").parent(c3).build();
        Category c11 = Category.builder().id(11L).name("경영").parent(c3).build();

        Category c12 = Category.builder().id(12L).name("소설").parent(c4).build();
        Category c13 = Category.builder().id(13L).name("자기계발/교양").parent(c4).build();
        Category c14 = Category.builder().id(14L).name("교육").parent(c4).build();
        Category c15 = Category.builder().id(15L).name("경영").parent(c4).build();

        Category c16 = Category.builder().id(16L).name("성악").parent(c7).build();
        Category c17 = Category.builder().id(17L).name("악기").parent(c7).build();

        List<Category> list = List.of(
                c1, c2, c3, c4, c5, c6, c7, c8, c9, c10,
                c11, c12, c13, c14, c15, c16, c17);

        categoryRepository.saveAllAndFlush(list);
    }

    /*
     * - 도서
     *      - 국내도서
     *          - 소설
     *          - 자기계발/교양
     *          - 교육
     *          - 경영
     *     - 해외도서
     *          - 소설
     *          - 자기계발/교양
     *          - 교육
     *          - 경영
     * - 음반
     *      - KPOP
     *      - POP
     *      - 클래식
     *          - 성악
     *          - 악기
     */

}
