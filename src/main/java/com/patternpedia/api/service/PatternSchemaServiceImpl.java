package com.patternpedia.api.service;

import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.entities.PatternSectionSchema;
import com.patternpedia.api.exception.NullPatternSchemaException;
import com.patternpedia.api.exception.PatternSchemaNotFoundException;
import com.patternpedia.api.repositories.PatternSchemaRepository;
import com.patternpedia.api.repositories.PatternSectionSchemaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class PatternSchemaServiceImpl implements PatternSchemaService {

    private PatternSchemaRepository patternSchemaRepository;

    private PatternSectionSchemaRepository patternSectionSchemaRepository;

    public PatternSchemaServiceImpl(PatternSchemaRepository patternSchemaRepository,
                                    PatternSectionSchemaRepository patternSectionSchemaRepository) {
        this.patternSchemaRepository = patternSchemaRepository;
        this.patternSectionSchemaRepository = patternSectionSchemaRepository;
    }

    @Override
    public PatternSchema createPatternSchema(PatternSchema patternSchema) {
        if (null == patternSchema) {
            throw new NullPatternSchemaException();
        }
        List<PatternSectionSchema> patternSectionSchemas = patternSchema.getPatternSectionSchemas();
        patternSchema.setPatternSectionSchemas(new ArrayList<>());
        patternSchema = this.patternSchemaRepository.save(patternSchema);

        PatternSchema finalPatternSchema = patternSchema;
        List<PatternSectionSchema> persistedSectionSchemas = patternSectionSchemas.stream()
                .map(patternSectionSchema -> {
                    patternSectionSchema.setPatternSchema(finalPatternSchema);
                    return this.patternSectionSchemaRepository.save(patternSectionSchema);
                }).collect(toList());
        patternSchema.setPatternSectionSchemas(persistedSectionSchemas);
        return this.patternSchemaRepository.save(patternSchema);
    }

    @Override
    public PatternSchema updatePatternSchema(PatternSchema patternSchema) {
        if (null == patternSchema) {
            throw new NullPatternSchemaException("PatternSchema is null");
        }

        if (this.patternSchemaRepository.existsById(patternSchema.getId())) {
            return this.patternSchemaRepository.save(patternSchema);
        } else {
            throw new PatternSchemaNotFoundException(String.format("PatternSchema not found: %s", patternSchema.getId()));
        }
    }

    @Override
    public PatternSchema getPatternSchemaById(UUID id) {
        return this.patternSchemaRepository.findById(id)
                .orElseThrow(() -> new PatternSchemaNotFoundException(String.format("PatternSchema not found: %s", id)));
    }

    @Override
    public void deletePatternSchemaById(UUID id) {
        this.patternSchemaRepository.deleteById(id);
    }
}
