package com.graphaware.module.warmup;

import com.graphaware.tx.executor.batch.BatchTransactionExecutor;
import com.graphaware.tx.executor.batch.IterableInputBatchTransactionExecutor;
import com.graphaware.tx.executor.batch.MultiThreadedBatchTransactionExecutor;
import com.graphaware.tx.executor.batch.UnitOfWork;
import com.graphaware.tx.executor.single.TransactionCallback;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.tooling.GlobalGraphOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarmUp {

    private static final Logger LOG = LoggerFactory.getLogger(WarmUp.class);

    private final GraphDatabaseService database;

    public WarmUp(GraphDatabaseService database) {
        this.database = database;
    }

    public void warmUp() {
        BatchTransactionExecutor executor = new IterableInputBatchTransactionExecutor<>(
                database,
                1000,
                new TransactionCallback<Iterable<Node>>() {
                    @Override
                    public Iterable<Node> doInTransaction(GraphDatabaseService database) throws Exception {
                        return GlobalGraphOperations.at(database).getAllNodes();
                    }
                },
                new UnitOfWork<Node>() {
                    @Override
                    public void execute(GraphDatabaseService database, Node node, int batchNumber, int stepNumber) {
                        if (stepNumber % 1000 == 0) {
                            LOG.info("Warming up graph in batch number " + batchNumber);
                        }

                        try {
                            for (Relationship r : node.getRelationships()) {
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
                        } catch (Exception e) {
                            System.out.println("e!");
                        }

                    }
                }
        );

        new MultiThreadedBatchTransactionExecutor(executor).doExecute();
    }
}
