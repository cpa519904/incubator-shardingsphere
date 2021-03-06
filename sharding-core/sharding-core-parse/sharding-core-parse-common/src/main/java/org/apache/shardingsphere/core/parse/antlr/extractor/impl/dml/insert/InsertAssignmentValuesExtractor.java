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

package org.apache.shardingsphere.core.parse.antlr.extractor.impl.dml.insert;

import com.google.common.base.Optional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.shardingsphere.core.parse.antlr.extractor.OptionalSQLSegmentExtractor;
import org.apache.shardingsphere.core.parse.antlr.extractor.impl.expression.ExpressionExtractor;
import org.apache.shardingsphere.core.parse.antlr.extractor.util.ExtractorUtils;
import org.apache.shardingsphere.core.parse.antlr.extractor.util.RuleName;
import org.apache.shardingsphere.core.parse.antlr.sql.segment.InsertValuesSegment;
import org.apache.shardingsphere.core.parse.lexer.token.DefaultKeyword;

import java.util.Map;

/**
 * Insert assignment values extractor.
 *
 * @author zhangliang
 */
@RequiredArgsConstructor
public final class InsertAssignmentValuesExtractor implements OptionalSQLSegmentExtractor {
    
    private final Map<ParserRuleContext, Integer> placeholderIndexes;
    
    private final ExpressionExtractor expressionExtractor = new ExpressionExtractor();
    
    @Override
    public Optional<InsertValuesSegment> extract(final ParserRuleContext ancestorNode) {
        Optional<ParserRuleContext> assignmentValuesNode = ExtractorUtils.findFirstChildNode(ancestorNode, RuleName.ASSIGNMENT_VALUES);
        if (!assignmentValuesNode.isPresent()) {
            return Optional.absent();
        }
        InsertValuesSegment insertValuesSegment = new InsertValuesSegment(DefaultKeyword.VALUES, 
                assignmentValuesNode.get().getStart().getStartIndex(), assignmentValuesNode.get().getStop().getStopIndex(), 
                ExtractorUtils.getAllDescendantNodes(assignmentValuesNode.get(), RuleName.QUESTION).size());
        for (ParserRuleContext each : ExtractorUtils.getAllDescendantNodes(assignmentValuesNode.get(), RuleName.ASSIGNMENT_VALUE)) {
            insertValuesSegment.getValues().add(expressionExtractor.extractCommonExpressionSegment(placeholderIndexes, each));
        }
        return Optional.of(insertValuesSegment);
    }
}
