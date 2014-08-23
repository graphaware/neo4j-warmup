package com.graphaware.module.warmup;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST API for {@link com.graphaware.module.warmup.WarmUp}.
 */
@Controller
@RequestMapping("/warmup")
public class WarmUpApi {

    private final WarmUp warmUp;

    @Autowired
    public WarmUpApi(GraphDatabaseService database) {
        this.warmUp = new WarmUp(database);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void warmup() {
        warmUp.warmUp();
    }
}
