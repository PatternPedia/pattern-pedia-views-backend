package com.patternpedia.api.util;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegrationTestHelper {

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    public Pattern createOrGetPattern(Pattern p) {
        return this.patternRepository.findById(p.getId())
                .orElse(this.patternRepository.save(p));
    }

    public PatternLanguage createOrGetPatternLanguage(PatternLanguage pl) {
        return this.patternLanguageRepository.findById(pl.getId())
                .orElse(this.patternLanguageRepository.save(pl));
    }

}
