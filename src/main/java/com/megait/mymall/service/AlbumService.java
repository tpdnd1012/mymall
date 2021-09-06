package com.megait.mymall.service;

import com.megait.mymall.repository.AlbumRepository;
import com.megait.mymall.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

//@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final CategoryRepository categoryRepository;

}
