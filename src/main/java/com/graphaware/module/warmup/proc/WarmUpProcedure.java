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

package com.graphaware.module.warmup.proc;

import com.graphaware.module.warmup.logic.WarmUp;
import org.neo4j.collection.RawIterator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.collection.Iterables;
import org.neo4j.kernel.api.exceptions.ProcedureException;
import org.neo4j.kernel.api.proc.CallableProcedure;
import org.neo4j.kernel.api.proc.Neo4jTypes;

import java.util.Collections;

import static org.neo4j.kernel.api.proc.ProcedureSignature.procedureName;
import static org.neo4j.kernel.api.proc.ProcedureSignature.procedureSignature;

public class WarmUpProcedure extends CallableProcedure.BasicProcedure {

    private final WarmUp warmUp;

    public WarmUpProcedure(GraphDatabaseService database) {
        super(procedureSignature(procedureName("ga", "warmup", "run")).out("result", Neo4jTypes.NTString).build());
        this.warmUp = new WarmUp(database);
    }

    @Override
    public RawIterator<Object[], ProcedureException> apply(Context ctx, Object[] input) throws ProcedureException {
        warmUp.warmUp();
        return Iterables.asRawIterator(Collections.<Object[]>singleton(new String[]{"DONE"}).iterator());
    }
}