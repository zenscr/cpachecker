/*
 * CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2015  Dirk Beyer
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 *  CPAchecker web page:
 *    http://cpachecker.sosy-lab.org
 */
package org.sosy_lab.cpachecker.cpa.cfalabels.visitors;

import java.util.Map;
import java.util.Set;

import org.sosy_lab.cpachecker.cfa.ast.c.CAddressOfLabelExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CArraySubscriptExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CBinaryExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CCastExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CCharLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CComplexCastExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpressionVisitor;
import org.sosy_lab.cpachecker.cfa.ast.c.CFieldReference;
import org.sosy_lab.cpachecker.cfa.ast.c.CFloatLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CIdExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CImaginaryLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CIntegerLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CPointerExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CStringLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CTypeIdExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CUnaryExpression;
import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.cpa.cfalabels.CFAEdgeLabel;
import org.sosy_lab.cpachecker.exceptions.CPATransferException;
import org.sosy_lab.cpachecker.exceptions.UnsupportedCCodeException;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;

/**
 * Created by zenscr on 30/09/15.
 */
public class CExpressionLabelVisitor implements CExpressionVisitor<Set<CFAEdgeLabel>, CPATransferException> {

  private CFAEdge cfaEdge;

  public CExpressionLabelVisitor(CFAEdge cfaEdge) {
    this.cfaEdge = cfaEdge;
  }

  // TODO put this in one file
  static final Map<String, CFAEdgeLabel> SPECIAL_FUNCTIONS;

  static {
    Builder<String, CFAEdgeLabel> builder = ImmutableMap.builder();
    builder.put("pthread_create", CFAEdgeLabel.PTHREAD);
    builder.put("pthread_exit", CFAEdgeLabel.PTHREAD);
    builder.put("__VERIFIER_error", CFAEdgeLabel.VERIFIER_ERROR);
    builder.put("__VERIFIER_assert", CFAEdgeLabel.VERIFIER_ASSERT);
    builder.put("__VERIFIER_assume", CFAEdgeLabel.VERIFIER_ASSUME);
    builder.put("__VERIFIER_atomic_begin", CFAEdgeLabel.VERIFIER_ATOMIC_BEGIN);
    builder.put("__VERIFIER_atomic_end", CFAEdgeLabel.VERIFIER_ATOMIC_END);
    builder.put("__VERIFIER_nondet", CFAEdgeLabel.INPUT);
    builder.put("malloc", CFAEdgeLabel.MALLOC);
    builder.put("free", CFAEdgeLabel.FREE);
    SPECIAL_FUNCTIONS = builder.build();
  }

  @Override
  public Set<CFAEdgeLabel> visit(CBinaryExpression pIastBinaryExpression)
      throws CPATransferException {
    Set<CFAEdgeLabel> labels = Sets.newHashSet();
    switch(pIastBinaryExpression.getOperator()) {
      case MULTIPLY:
      case DIVIDE:
      case PLUS:
      case MINUS:
        labels.add(CFAEdgeLabel.ARITHMETIC);
        break;
      case EQUALS:
      case NOT_EQUALS:
      case LESS_THAN:
      case GREATER_THAN:
      case LESS_EQUAL:
      case GREATER_EQUAL:
        labels.add(CFAEdgeLabel.COMPARISON);
        break;
      case BINARY_AND:
      case BINARY_XOR:
      case BINARY_OR:
      case SHIFT_LEFT:
      case SHIFT_RIGHT:
        labels.add(CFAEdgeLabel.BIT_OPERATION);
        break;
      case MODULO:
        labels.add(CFAEdgeLabel.MODULO);
        break;
    }
    labels.addAll(pIastBinaryExpression.getOperand1().accept(this));
    labels.addAll(pIastBinaryExpression.getOperand2().accept(this));
    return Sets.immutableEnumSet(labels);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CCastExpression pIastCastExpression)
      throws CPATransferException {
    Set<CFAEdgeLabel> labels = Sets.newHashSet();
    labels.add(CFAEdgeLabel.CAST);
    CTypeLabelVisitor typeLabelVisitor = new CTypeLabelVisitor(this.cfaEdge);
    labels.addAll(pIastCastExpression.getCastType().accept(typeLabelVisitor));
    labels.addAll(pIastCastExpression.getOperand().accept(this));
    return Sets.immutableEnumSet(labels);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CCharLiteralExpression pIastCharLiteralExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.LITERAL);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CFloatLiteralExpression pIastFloatLiteralExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.LITERAL);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CIntegerLiteralExpression pIastIntegerLiteralExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.LITERAL);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CStringLiteralExpression pIastStringLiteralExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.LITERAL);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CTypeIdExpression pIastTypeIdExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.TYPE_ID);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CUnaryExpression pIastUnaryExpression)
      throws CPATransferException {
    Set<CFAEdgeLabel> labels = Sets.newHashSet();
    switch(pIastUnaryExpression.getOperator()) {
      case MINUS:
        labels.add(CFAEdgeLabel.ARITHMETIC);
        break;
      case AMPER:
        labels.add(CFAEdgeLabel.ADDRESS);
        break;
      case TILDE:
        labels.add(CFAEdgeLabel.BIT_OPERATION);
        break;
      case SIZEOF:
        labels.add(CFAEdgeLabel.SIZEOF);
        break;
      case ALIGNOF:
        throw new UnsupportedCCodeException("ALIGNOF is not supported", this.cfaEdge);
    }
    return Sets.union(labels, pIastUnaryExpression.getOperand().accept(this));
  }

  @Override
  public Set<CFAEdgeLabel> visit(CImaginaryLiteralExpression PIastLiteralExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.COMPLEX_NUMBER);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CAddressOfLabelExpression pAddressOfLabelExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.ADDRESS);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CArraySubscriptExpression pIastArraySubscriptExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.ARRAY_SUBSCRIPT);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CFieldReference pIastFieldReference)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.FIELD_REFERENCE);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CIdExpression pIastIdExpression)
      throws CPATransferException {
    if(SPECIAL_FUNCTIONS.containsKey(pIastIdExpression.getName())) {
      return Sets.immutableEnumSet(SPECIAL_FUNCTIONS.get(pIastIdExpression.getName()));
    }
    for(String key : SPECIAL_FUNCTIONS.keySet()) {
      if(pIastIdExpression.getName().startsWith(key))
        return Sets.immutableEnumSet(SPECIAL_FUNCTIONS.get(key));
    }
    return Sets.immutableEnumSet(CFAEdgeLabel.ID);
  }

  @Override
  public Set<CFAEdgeLabel> visit(CPointerExpression pointerExpression)
      throws CPATransferException {
    return Sets.union(Sets.immutableEnumSet(CFAEdgeLabel.PTR), pointerExpression.getOperand().accept(this));
  }

  @Override
  public Set<CFAEdgeLabel> visit(CComplexCastExpression complexCastExpression)
      throws CPATransferException {
    return Sets.immutableEnumSet(CFAEdgeLabel.COMPLEX_NUMBER);
  }
}