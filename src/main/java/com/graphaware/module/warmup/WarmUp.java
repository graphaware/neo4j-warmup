package com.graphaware.module.warmup;

import com.graphaware.tx.executor.batch.BatchTransactionExecutor;
import com.graphaware.tx.executor.batch.IterableInputBatchTransactionExecutor;
import com.graphaware.tx.executor.batch.MultiThreadedBatchTransactionExecutor;
import com.graphaware.tx.executor.batch.UnitOfWork;
import com.graphaware.tx.executor.single.TransactionCallback;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.tooling.GlobalGraphOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.neo4j.graphdb.Direction.*;

/**
 * A component able to warmup the caches using all available cores.
 */
public class WarmUp {

    private static final Logger LOG = LoggerFactory.getLogger(WarmUp.class);
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
        BatchTransactionExecutor executor = new IterableInputBatchTransactionExecutor<>(
                database,
                BATCH_SIZE,
                new TransactionCallback<Iterable<Node>>() {
                    @Override
                    public Iterable<Node> doInTransaction(GraphDatabaseService database) throws Exception {
                        return GlobalGraphOperations.at(database).getAllNodes();
                    }
                },
                new UnitOfWork<Node>() {
                    @Override
                    public void execute(GraphDatabaseService database, Node node, int batchNumber, int stepNumber) {
                        if (stepNumber % BATCH_SIZE == 1) {
                            LOG.info("Warming up graph in batch number " + batchNumber);
                        }

                        accessEverything(node);
                    }
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
