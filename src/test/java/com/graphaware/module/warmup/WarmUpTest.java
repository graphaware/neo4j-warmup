/*
 * Copyright (c) 2013 GraphAware
 *
 * This file is part of GraphAware.
 *
 * GraphAware is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.module.warmup;

import com.graphaware.test.integration.GraphAwareApiTest;
import com.graphaware.tx.executor.NullItem;
import com.graphaware.tx.executor.batch.NoInputBatchTransactionExecutor;
import com.graphaware.tx.executor.batch.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.neo4j.graphdb.*;

import java.util.Random;

import static com.graphaware.test.util.TestUtils.get;

/**
 * {@link com.graphaware.test.integration.NeoServerIntegrationTest} for {@link WarmUp} module and {@link WarmUpApi}.
 */
public class WarmUpTest extends GraphAwareApiTest {

    private Random random = new Random();

    @Test
    public void warmupReturnsOK() throws InterruptedException {
        new NoInputBatchTransactionExecutor(getDatabase(), 1000, 10000, new UnitOfWork<NullItem>() {
            @Override
            public void execute(GraphDatabaseService database, NullItem input, int batchNumber, int stepNumber) {
                Node node1 = getOrCreateNode(database);
                Node node2 = getOrCreateNode(database);
                Relationship r = node1.createRelationshipTo(node2, DynamicRelationshipType.withName("SOME_TYPE_" + random.nextInt(10)));
                r.setProperty("test", "something" + random.nextInt());
            }
        }).execute();

        Thread.sleep(500);

        get(baseUrl() + "/warmup", HttpStatus.OK_200);
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
