package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModel;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.rest.model.EdgeDTO;
import com.patternpedia.api.rest.model.FileDTO;
import com.patternpedia.api.rest.model.PatternInstanceDTO;
import com.patternpedia.api.rest.model.PositionDTO;
import com.patternpedia.api.service.ConcreteSolutionService;
import com.patternpedia.api.service.DesignModelService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.text.CaseUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@CommonsLog
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/design-models", produces = "application/hal+json")
public class DesignModelController {

    private DesignModelService designModelService;
    private ConcreteSolutionService concreteSolutionService;
    private ObjectCodec objectMapper;


    public DesignModelController(DesignModelService designModelService, ConcreteSolutionService concreteSolutionService, ObjectMapper objectMapper) {
        this.designModelService = designModelService;
        this.concreteSolutionService = concreteSolutionService;
        this.objectMapper = objectMapper;
    }


    private static List<Link> getDesignModelCollectionLinks() {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(DesignModelController.class).getDesignModels()).withSelfRel()
                .andAffordance(afford(methodOn(DesignModelController.class).createDesignModel(null))));

        links.add(linkTo(methodOn(DesignModelController.class).getDesignModel(null)).withRel("designModel"));

        return links;
    }


    private static List<Link> getDesignModelLinks(DesignModel designModel) {
        List<Link> links = new ArrayList<>();

        links.add(
                linkTo(methodOn(DesignModelController.class).getDesignModel(designModel.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(DesignModelController.class).putDesignModel(designModel.getId(), null)))
                        .andAffordance(afford(methodOn(DesignModelController.class).deleteDesignModel(designModel.getId())))
        );
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModels()).withRel("designModels"));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModel.getId())).withRel("patterns"));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternEdges(designModel.getId())).withRel("edges"));

        return links;
    }


    // TODO currently this is a duplicate from PatternController, may generalize this and move it to a utility class, etc.
    List<Link> getPatternLinksForDesignModelRoute(Pattern pattern, UUID patternViewId) {
        List<Link> links = Collections.emptyList();

//        List<Link> links = this.getPatternLinks(pattern);
//
//        List<DirectedEdge> outgoingEdges;
//        try {
//            outgoingEdges = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            outgoingEdges = Collections.emptyList();
//        }
//        if (null != outgoingEdges) {
//            for (DirectedEdge directedEdge : outgoingEdges) {
//                if (null != directedEdge.getPatternViews()) {
//                    // edge is part of pattern view, thus we reference the pattern view route
//                    List<Link> newLinks = directedEdge.getPatternViews().stream()
//                            .filter(patternViewDirectedEdge -> patternViewDirectedEdge.getPatternView().getId().equals(patternViewId))
//                            .map(patternViewDirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
//                                    .getDirectedEdgeOfPatternViewById(patternViewDirectedEdge.getPatternView().getId(), patternViewDirectedEdge.getDirectedEdge().getId())).withRel("outgoingDirectedEdges")
//                            ).collect(Collectors.toList());
//                    links.addAll(newLinks);
//                }
//            }
//        }
//
//        List<DirectedEdge> ingoingEdges;
//        try {
//            ingoingEdges = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            ingoingEdges = Collections.emptyList();
//        }
//        if (null != ingoingEdges) {
//            for (DirectedEdge directedEdge : ingoingEdges) {
//                if (null != directedEdge.getPatternViews()) {
//                    // edge is part of pattern view, thus we reference the pattern view route
//                    List<Link> newLinks = directedEdge.getPatternViews().stream()
//                            .filter(patternViewDirectedEdge -> patternViewDirectedEdge.getPatternView().getId().equals(patternViewId))
//                            .map(patternViewDirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
//                                    .getDirectedEdgeOfPatternViewById(patternViewDirectedEdge.getPatternView().getId(), patternViewDirectedEdge.getDirectedEdge().getId())).withRel("ingoingDirectedEdges")
//                            ).collect(Collectors.toList());
//                    links.addAll(newLinks);
//                }
//            }
//        }
//
//        List<UndirectedEdge> undirectedEdges;
//        try {
//            undirectedEdges = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
//        } catch (UndirectedEdgeNotFoundException ex) {
//            undirectedEdges = Collections.emptyList();
//        }
//        if (null != undirectedEdges) {
//            for (UndirectedEdge undirectedEdge : undirectedEdges) {
//                if (null != undirectedEdge.getPatternViews()) {
//                    // edge is part of pattern view, thus we reference the pattern view route
//                    List<Link> newLinks = undirectedEdge.getPatternViews().stream()
//                            .filter(patternViewUndirectedEdge -> patternViewUndirectedEdge.getPatternView().getId().equals(patternViewId))
//                            .map(patternViewUndirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
//                                    .getUndirectedEdgeOfPatternViewById(patternViewUndirectedEdge.getPatternView().getId(), patternViewUndirectedEdge.getUndirectedEdge().getId())).withRel("undirectedEdges")
//                            ).collect(Collectors.toList());
//                    links.addAll(newLinks);
//                }
//            }
//        }
//
//        List<DirectedEdge> outgoingFromPatternLanguage;
//        try {
//            outgoingFromPatternLanguage = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            outgoingFromPatternLanguage = Collections.emptyList();
//        }
//        if (null != outgoingFromPatternLanguage) {
//            for (DirectedEdge directedEdge : outgoingFromPatternLanguage) {
//                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
//                    // edge is part of pattern language, thus reference the route to edge in pattern language
//                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
//                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("outgoingDirectedEdgesFromPatternLanguage"));
//                }
//            }
//        }
//
//        List<DirectedEdge> ingoingFromPatternLanguage;
//        try {
//            ingoingFromPatternLanguage = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            ingoingFromPatternLanguage = Collections.emptyList();
//        }
//        if (null != ingoingFromPatternLanguage) {
//            for (DirectedEdge directedEdge : ingoingFromPatternLanguage) {
//                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
//                    // edge is part of pattern language, thus reference the route to edge in pattern language
//                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
//                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("ingoingDirectedEdgesFromPatternLanguage"));
//                }
//            }
//        }
//
//        List<UndirectedEdge> undirectedFromPatternLanguage;
//        try {
//            undirectedFromPatternLanguage = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
//        } catch (UndirectedEdgeNotFoundException ex) {
//            undirectedFromPatternLanguage = Collections.emptyList();
//        }
//        if (null != undirectedFromPatternLanguage) {
//            for (UndirectedEdge undirectedEdge : undirectedFromPatternLanguage) {
//                if (null != undirectedEdge.getPatternLanguage() && undirectedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
//                    // edge is part of pattern language, thus reference the route to edge in pattern language
//                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
//                            .getUndirectedEdgeOfPatternLanguageById(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId())).withRel("undirectedEdgesFromPatternLanguage"));
//                }
//            }
//        }

        return links;
    }


    static List<Link> getDesignModelPatternInstanceCollectionLinks(UUID designModelId) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModelId)).withSelfRel()
                .andAffordance(afford(methodOn(DesignModelController.class).addDesignModelPatternInstance(designModelId, null))));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModel(designModelId)).withRel("designModel"));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModelId)).withRel("patterns"));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternEdges(designModelId)).withRel("edges"));
        return links;
    }


    @GetMapping("")
    public CollectionModel<EntityModel<DesignModel>> getDesignModels() {

        List<EntityModel<DesignModel>> patternViews = this.designModelService.getAllDesignModels()
                .stream()
                .map(patternView -> new EntityModel<>(patternView,
                        getDesignModelLinks(patternView)))
                .collect(Collectors.toList());

        return new CollectionModel<>(patternViews, getDesignModelCollectionLinks());
    }


    @PostMapping("")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createDesignModel(@RequestBody DesignModel designModel) {
        String nameAsCamelCase = CaseUtils.toCamelCase(designModel.getName(), false);
        String uri = String.format("https://patternpedia.org/designModels/%s", nameAsCamelCase);
        designModel.setUri(uri);

        DesignModel createdDesignModel = this.designModelService.createDesignModel(designModel);

        return ResponseEntity.created(linkTo(methodOn(DesignModelController.class)
                .getDesignModel(createdDesignModel.getId())).toUri()).build();
    }


    @GetMapping("/{designModelId}")
    public EntityModel<DesignModel> getDesignModel(@PathVariable UUID designModelId) {
        DesignModel patternView = this.designModelService.getDesignModel(designModelId);

        return new EntityModel<>(patternView, getDesignModelLinks(patternView));
    }


    @PutMapping("/{designModelId}")
    public ResponseEntity<?> putDesignModel(@PathVariable UUID designModelId, @RequestBody DesignModel designModel) {
//        patternView = this.patternViewService.updateDesignModel(patternView);

        return ResponseEntity.ok(designModel);
    }


    @DeleteMapping("/{designModelId}")
    public ResponseEntity<?> deleteDesignModel(@PathVariable UUID designModelId) {
//        this.patternViewService.deleteDesignModel(designModelId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{designModelId}/patterns")
    CollectionModel<EntityModel<PatternInstanceDTO>> getDesignModelPatternInstances(@PathVariable UUID designModelId) {
        List<DesignModelPatternInstance> patternInstances = this.designModelService.getDesignModel(designModelId).getPatterns();

        List<EntityModel<PatternInstanceDTO>> patterns = patternInstances.stream()
                .map(PatternInstanceDTO::from)
                .map(patternModel -> new EntityModel<>(patternModel))// TODO, getPatternLinksForDesignModelRoute(patternModel, designModelId)))
                .collect(Collectors.toList());
        return new CollectionModel<>(patterns, getDesignModelPatternInstanceCollectionLinks(designModelId));
    }


    @PostMapping("/{designModelId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addDesignModelPatternInstance(@PathVariable UUID designModelId, @RequestBody Pattern pattern) {
        this.designModelService.addPatternInstance(designModelId, pattern.getId());
        return ResponseEntity.created(linkTo(methodOn(PatternController.class) // TODO fix controller
                .getPatternOfPatternViewById(designModelId, pattern.getId())).toUri()).build();
    }


    @PutMapping("/{designModelId}/patterns/{patternInstanceId}/position")
    public ResponseEntity<?> putDesignModelPatternInstancePosition(@PathVariable UUID designModelId, @PathVariable UUID patternInstanceId, @RequestBody PositionDTO position) {
        this.designModelService.updatePatternInstancePosition(designModelId, patternInstanceId, position.getX(), position.getY());
        return ResponseEntity.created(linkTo(methodOn(PatternController.class) // TODO fix controller
                .getPatternOfPatternViewById(designModelId, patternInstanceId)).toUri()).build();
    }


    @DeleteMapping("/{designModelId}/patterns/{patternInstanceId}")
    public ResponseEntity<?> deleteDesignModelPatternInstance(@PathVariable UUID designModelId, @PathVariable UUID patternInstanceId) {
        this.designModelService.deletePatternInstance(designModelId, patternInstanceId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{designModelId}/edges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addDesignModelEdge(@PathVariable UUID designModelId, @RequestBody EdgeDTO edgeDTO) {

        this.designModelService.addEdge(designModelId, edgeDTO.getFirstPatternId(), edgeDTO.getSecondPatternId(),
                edgeDTO.isDirectedEdge(), edgeDTO.getType(), edgeDTO.getDescription());

        return ResponseEntity.created(linkTo(methodOn(DesignModelController.class)
                .addDesignModelEdge(designModelId, null)).toUri()).build();
    }


    @GetMapping("/{designModelId}/edges")
    public ResponseEntity<?> getDesignModelPatternEdges(@PathVariable UUID designModelId) {

        List<DesignModelPatternEdge> designModelPatternEdges = this.designModelService.getEdges(designModelId);

        List<EdgeDTO> edgeDTOs = designModelPatternEdges.parallelStream()
                .map(EdgeDTO::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(edgeDTOs);
//        return new CollectionModel<DesignModelPatternEdge>(
//                designModelPatternEdges,
//                (Iterable<Link>) ResponseEntity.created(linkTo(methodOn(DesignModelController.class)
//                        .addDesignModelEdge(designModelId, null)).toUri()).build()
//        );
    }


    @DeleteMapping("/{designModelId}/edges/{sourceId}/{targetId}")
    public ResponseEntity<?> getDesignModelPatternEdges(@PathVariable UUID designModelId, @PathVariable UUID sourceId, @PathVariable UUID targetId) {

        this.designModelService.deleteEdge(designModelId, sourceId, targetId);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/{designModelId}/concrete-solutions")
    public Set<ConcreteSolution> checkConcreteSolutions(@PathVariable UUID designModelId) {
        List<DesignModelPatternInstance> patternInstanceList = this.designModelService.getDesignModel(designModelId).getPatterns();
        Set<String> patternUris = patternInstanceList.stream().map(patternInstance -> patternInstance.getPattern().getUri()).collect(Collectors.toSet());
        Set<ConcreteSolution> concreteSolutionSet = new HashSet<>();

        for (String uri : patternUris) {
            this.concreteSolutionService.getConcreteSolutions(URI.create(uri)).forEach(concreteSolution -> concreteSolutionSet.add(concreteSolution));
        }

        return concreteSolutionSet;
    }


    @PostMapping("/{designModelId}/aggregate")
    public List<FileDTO> aggregateConcreteSolutions(@PathVariable UUID designModelId, @RequestBody Map<UUID, UUID> patternConcreteSolutionMap) {

        log.info(patternConcreteSolutionMap.toString());

        DesignModel designModel = this.designModelService.getDesignModel(designModelId);
        List<DesignModelPatternInstance> patternInstanceList = designModel.getPatterns();
        List<DesignModelPatternEdge> directedEdgeList = designModel.getDirectedEdges();

        return this.concreteSolutionService.aggregate(patternInstanceList, directedEdgeList, patternConcreteSolutionMap);
    }
}
