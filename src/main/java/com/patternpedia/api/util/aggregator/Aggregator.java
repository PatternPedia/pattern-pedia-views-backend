package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import lombok.extern.apachecommons.CommonsLog;
import org.stringtemplate.v4.ST;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


@CommonsLog
public abstract class Aggregator {

    protected static final Random RANDOM = new Random();


    public abstract String aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<String, Object> query);


    protected static String readFile(String url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    protected static String renderTemplate(String concreteSolutionTemplate, Map<String, Object> dataContainer) {
        ST template = new ST(concreteSolutionTemplate, '$', '$');

        template.add("random", RANDOM.nextInt(Integer.MAX_VALUE));

        for (Map.Entry<String, Object> entry : dataContainer.entrySet()) {
            template.add(entry.getKey(), entry.getValue());
        }

        return template.render();
    }
}