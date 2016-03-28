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

package com.graphaware.module.warmup;

import com.graphaware.module.warmup.api.WarmUpApi;
import com.graphaware.module.warmup.logic.WarmUp;
import com.graphaware.test.integration.GraphAwareIntegrationTest;
import com.graphaware.tx.executor.batch.NoInputBatchTransactionExecutor;
import org.junit.Test;
import org.neo4j.graphdb.*;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * {@link GraphAwareIntegrationTest} for {@link WarmUp} module and {@link WarmUpApi}.
 */
public class WarmUpProcedureTest extends GraphAwareIntegrationTest {

    private Random random = new Random();

    @Test
    public void warmupDoesNotThrowExceptions() throws InterruptedException {
        new NoInputBatchTransactionExecutor(getDatabase(), 1000, 10000, (database, input, batchNumber, stepNumber) -> {
            Node node1 = getOrCreateNode(database);
            Node node2 = getOrCreateNode(database);
            Relationship r = node1.createRelationshipTo(node2, RelationshipType.withName("SOME_TYPE_" + random.nextInt(10)));
            r.setProperty("test", "something" + random.nextInt());
        }).execute();

        Thread.sleep(500);

        assertEquals("DONE", getDatabase().execute("CALL ga.warmup.run()").next().get("result"));
    }

    private Node getOrCreateNode(GraphDatabaseService database) {
        try {
            int i = random.nextInt(100000);
            return database.getNodeById(i);
        } catch (NotFoundException e) {
            Node node = database.createNode();
            node.setProperty("test", "something" + random.nextInt());
            node.setProperty("bla", "something" + random.nextInt());
            return node;
        }
    }
}
