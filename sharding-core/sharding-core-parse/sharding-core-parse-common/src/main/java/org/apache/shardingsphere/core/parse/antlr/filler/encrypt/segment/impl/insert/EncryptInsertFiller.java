/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.parse.antlr.filler.encrypt.segment.impl.insert;

import org.apache.shardingsphere.core.metadata.table.ShardingTableMetaData;
import org.apache.shardingsphere.core.parse.antlr.filler.encrypt.SQLSegmentEncryptFiller;
import org.apache.shardingsphere.core.parse.antlr.sql.segment.InsertSegment;
import org.apache.shardingsphere.core.parse.lexer.token.DefaultKeyword;
import org.apache.shardingsphere.core.parse.parser.sql.SQLStatement;
import org.apache.shardingsphere.core.parse.parser.sql.dml.insert.InsertStatement;
import org.apache.shardingsphere.core.parse.parser.token.InsertValuesToken;
import org.apache.shardingsphere.core.rule.EncryptRule;

/**
 * Encrypt insert filler.
 *
 * @author duhongjun
 */
public class EncryptInsertFiller implements SQLSegmentEncryptFiller<InsertSegment> {
    
    @Override
    public void fill(final InsertSegment sqlSegment, final SQLStatement sqlStatement, final String sql, final EncryptRule encryptRule, final ShardingTableMetaData shardingTableMetaData) {
        InsertStatement insertStatement = (InsertStatement) sqlStatement;
        insertStatement.getUpdateTableAlias().put(insertStatement.getTables().getSingleTableName(), insertStatement.getTables().getSingleTableName());
        insertStatement.setInsertValuesListLastIndex(sqlSegment.getInsertValuesListLastIndex());
        insertStatement.setParametersIndex(sqlSegment.getParameterIndex());
        DefaultKeyword type;
        if (insertStatement.getInsertValues().getInsertValues().isEmpty()) {
            type = DefaultKeyword.SET;
        } else {
            type = DefaultKeyword.VALUES == insertStatement.getInsertValues().getInsertValues().get(0).getType() ? DefaultKeyword.VALUES : DefaultKeyword.SET;
        }
        insertStatement.getSQLTokens().add(new InsertValuesToken(sqlSegment.getColumnClauseStartIndex(), type));
    }
}
