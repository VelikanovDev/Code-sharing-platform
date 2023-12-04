package com.velikanovdev.platform.service.impl;

import com.velikanovdev.platform.entity.Snippet;
import com.velikanovdev.platform.repository.PlatformRepository;
import com.velikanovdev.platform.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlatformServiceImpl implements PlatformService {
    private final PlatformRepository platformRepository;

    @Autowired
    public PlatformServiceImpl(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    public Snippet getSnippet(Long id) {
        Optional<Snippet> optionalCodeEntity = platformRepository.findById(id.intValue());
        return optionalCodeEntity.orElse(null);
    }

    @Override
    public void addOrUpdateSnippet(Snippet snippet) {
        if (snippet.getId() == null) {
            log.info("PlatformServiceImpl: before saving a new snippet");
            platformRepository.save(snippet);
        } else {
            log.info("PlatformServiceImpl: before updating a snippet");
            Snippet existingSnippet = platformRepository.findById(snippet.getId().intValue()).orElseThrow();
            snippet.setId(existingSnippet.getId());
            platformRepository.save(snippet);
        }

    }

    @Override
    public List<Snippet> getLatest() {
        return platformRepository.findAllByOrderByDateDesc()
                .stream()
                .sorted((c1, c2) -> c2.getDate().compareTo(c1.getDate()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSnippet(Long id) {
        platformRepository.deleteById(id.intValue());
    }

    @Override
    public void deleteAllSnippets() {
        platformRepository.deleteAll();
    }
}
