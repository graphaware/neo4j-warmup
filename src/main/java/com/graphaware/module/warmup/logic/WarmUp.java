/*
 * Copyright (c) 2013-2016 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.module.warmup.logic;

import com.graphaware.common.log.LoggerFactory;
import com.graphaware.tx.executor.batch.IterableInputBatchTransactionExecutor;
import com.graphaware.tx.executor.batch.MultiThreadedBatchTransactionExecutor;
import com.graphaware.tx.executor.input.AllNodes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.logging.Log;

import static org.neo4j.graphdb.Direction.OUTGOING;

/**
 * A component able to warmup the caches using all available cores.
 */
public class WarmUp {

    private static final Log LOG = LoggerFactory.getLogger(WarmUp.class);
    private static final int BATCH_SIZE = 1000;

    private final GraphDatabaseService database;

    /**
     * Create a new instance of this component.
     *
     * @param database which it will warmup.
     */
    public WarmUp(GraphDatabaseService database) {
        this.database = database;
    }

    /**
     * Perform the warmup.
     */
    public void warmUp() {
        IterableInputBatchTransactionExecutor executor = new IterableInputBatchTransactionExecutor<>(
                database,
                BATCH_SIZE,
                new AllNodes(database, BATCH_SIZE),
                (database1, node, batchNumber, stepNumber) -> {
                    if (stepNumber % BATCH_SIZE == 1) {
                        LOG.info("Warming up graph in batch number " + batchNumber);
                    }

                    accessEverything(node);
                }
        );

        new MultiThreadedBatchTransactionExecutor(executor).doExecute();
    }

    /**
     * Access the given node's properties, labels, relationships, and their properties.
     *
     * @param node to access.
     */
    private void accessEverything(Node node) {
        for (Relationship r : node.getRelationships(OUTGOING)) {
            r.getOtherNode(node);
            r.getType();
            for (String key : r.getPropertyKeys()) {
                r.getProperty(key);
            }
        }

        for (String key : node.getPropertyKeys()) {
            node.getProperty(key);
        }

        node.getLabels();
    }
}
