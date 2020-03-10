package com.patternpedia.api.service;

import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.rest.model.CreateDirectedEdgeRequest;
import com.patternpedia.api.rest.model.CreateUndirectedEdgeRequest;

import org.springframework.transaction.annotation.Transactional;

public interface PatternLanguageService {

    @Transactional
    PatternLanguage createPatternLanguage(PatternLanguage patternLanguage);

    @Transactional(readOnly = true)
    List<PatternLanguage> getPatternLanguages();

    @Transactional(readOnly = true)
    PatternLanguage getPatternLanguageById(UUID patternLanguageId);

    @Transactional(readOnly = true)
    PatternLanguage getPatternLanguageByUri(String uri);

    @Transactional
    PatternLanguage updatePatternLanguage(PatternLanguage patternLanguage);

    @Transactional
    void deletePatternLanguage(UUID patternLanguageId);

    @Transactional
    Pattern createPatternAndAddToPatternLanguage(UUID patternLanguageId, Pattern pattern);

    @Transactional
    List<Pattern> getPatternsOfPatternLanguage(UUID patternLanguageId);

    @Transactional(readOnly = true)
    Pattern getPatternOfPatternLanguageById(UUID patternLanguageId, UUID patternId);

    @Transactional
    void deletePatternOfPatternLanguage(UUID patternLanguageId, UUID patternId);

    @Transactional
    PatternSchema createPatternSchemaAndAddToPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);

    @Transactional(readOnly = true)
    PatternSchema getPatternSchemaByPatternLanguageId(UUID patternLanguageId);

    @Transactional
    PatternSchema updatePatternSchemaOfPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);

    @Transactional(readOnly = true)
    Object getGraphOfPatternLanguage(UUID patternLanguageId);

    @Transactional
    Object createGraphOfPatternLanguage(UUID patternLanguageId, Object graph);

    @Transactional
    Object updateGraphOfPatternLanguage(UUID patternLanguageId, Object graph);

    @Transactional
    void deleteGraphOfPatternLanguage(UUID patternLanguageId);

    @Transactional
    DirectedEdge createDirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, CreateDirectedEdgeRequest createDirectedEdgeRequest);

    List<DirectedEdge> getDirectedEdgesOfPatternLanguage(UUID patternLanguageId);

    @Transactional(readOnly = true)
    DirectedEdge getDirectedEdgeOfPatternLanguageById(UUID patternLanguageId, UUID directedEdgeId);

    @Transactional
    DirectedEdge updateDirectedEdgeOfPatternLanguage(UUID patternLanguageId, DirectedEdge directedEdge);

    @Transactional
    void removeDirectedEdgeFromPatternLanguage(UUID patternLanguageId, UUID directedEdgeId);

    @Transactional
    UndirectedEdge createUndirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, CreateUndirectedEdgeRequest createUndirectedEdgeRequest);

    @Transactional(readOnly = true)
    List<UndirectedEdge> getUndirectedEdgesOfPatternLanguage(UUID patternLanguageId);

    @Transactional(readOnly = true)
    UndirectedEdge getUndirectedEdgeOfPatternLanguageById(UUID patternLanguageId, UUID undirectedEdgeId) throws UndirectedEdgeNotFoundException;

    @Transactional
    UndirectedEdge updateUndirectedEdgeOfPatternLanguage(UUID patternLanguageId, UndirectedEdge undirectedEdge);

    @Transactional
    void removeUndirectedEdgeFromPatternLanguage(UUID patternLanguageId, UUID undirectedEdgeId);
}
