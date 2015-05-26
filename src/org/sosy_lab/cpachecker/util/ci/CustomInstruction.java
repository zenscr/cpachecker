/*
 *  CPAchecker is a tool for configurable software verification.
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
package org.sosy_lab.cpachecker.util.ci;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.sosy_lab.common.Pair;
import org.sosy_lab.cpachecker.cfa.ast.c.CAddressOfLabelExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CArraySubscriptExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CBinaryExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CCastExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CCharLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CComplexCastExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CComplexTypeDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CDesignatedInitializer;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpressionAssignmentStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpressionStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpressionVisitor;
import org.sosy_lab.cpachecker.cfa.ast.c.CFieldReference;
import org.sosy_lab.cpachecker.cfa.ast.c.CFloatLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CFunctionCallAssignmentStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CFunctionCallExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CFunctionCallStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CFunctionDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CIdExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CImaginaryLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CInitializer;
import org.sosy_lab.cpachecker.cfa.ast.c.CInitializerExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CInitializerList;
import org.sosy_lab.cpachecker.cfa.ast.c.CIntegerLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CPointerExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CStringLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CTypeDefDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CTypeIdExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CUnaryExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CVariableDeclaration;
import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.cfa.model.MultiEdge;
import org.sosy_lab.cpachecker.cfa.model.c.CAssumeEdge;
import org.sosy_lab.cpachecker.cfa.model.c.CDeclarationEdge;
import org.sosy_lab.cpachecker.cfa.model.c.CFunctionCallEdge;
import org.sosy_lab.cpachecker.cfa.model.c.CFunctionSummaryStatementEdge;
import org.sosy_lab.cpachecker.cfa.model.c.CReturnStatementEdge;
import org.sosy_lab.cpachecker.cfa.model.c.CStatementEdge;
import org.sosy_lab.cpachecker.cfa.types.c.CBasicType;
import org.sosy_lab.cpachecker.cfa.types.c.CSimpleType;
import org.sosy_lab.cpachecker.core.ShutdownNotifier;
import org.sosy_lab.cpachecker.util.predicates.pathformula.SSAMap;
import org.sosy_lab.cpachecker.util.predicates.pathformula.SSAMap.SSAMapBuilder;

import com.google.common.base.Joiner;

// Note that this class is not complete yet. Most of the comments are just for me and my advisor, they will disappear later!
public class CustomInstruction{

  private final CFANode ciStartNode;
  private final Collection<CFANode> ciEndNodes;
  private final List<String> inputVariables;
  private final List<String> outputVariables;
  private final ShutdownNotifier shutdownNotifier;


  /**
   * Constructor of CustomInstruction.
   * Note that the input-/output variables have to be sorted alphabetically!
   * @param pCIStartNode CFANode
   * @param pCIEndNodes Collection of CFANode
   * @param pInputVariables List of String, represents the input variables
   * @param pOutputVariables List of String, represents the outputvariables
   * @param pShutdownNotifier ShutdownNotifier
   */
  public CustomInstruction(final CFANode pCIStartNode, final Collection<CFANode> pCIEndNodes,
      final List<String> pInputVariables, final List<String> pOutputVariables, final ShutdownNotifier pShutdownNotifier) {

      ciStartNode = pCIStartNode;
      ciEndNodes = pCIEndNodes;
      inputVariables = pInputVariables;
      outputVariables = pOutputVariables;
      shutdownNotifier = pShutdownNotifier;
  }

  /**
   * Returns the signature of the input and output variables,
   * this is a String containing all input and output variables.
   * @return String like (iV1, iV2, ... iVn -> oV1, oV2, ..., oVm)
   */
  public String getSignature() {
    StringBuilder sb = new StringBuilder();

    sb.append("(");
    Joiner.on(", ").appendTo(sb, inputVariables);
    sb.append(" -> ");
    Joiner.on("@1, ").appendTo(sb, outputVariables);
    if (outputVariables.size() > 0) {
      sb.append("@1");
    }
    sb.append(")");

    return sb.toString();
  }

  /**
   * Returns the (fake!) SMT description which is a
   * conjunctions of output variables and predicates (IVj = 0) for each input variable j.
   * Note that this is prefix notation!
   * @return (define-fun aci Bool((and (= IV1 0) (and (= IV2 0) (and OV1 OV2))))
   */
  public Pair<List<String>, String> getFakeSMTDescription() {
    if (inputVariables.size() == 0 && outputVariables.size() == 0) {
      return Pair.of(Collections.<String> emptyList(),
        "(define-fun ci() Bool true)");
    }
    StringBuilder sb = new StringBuilder();
    sb.append("(define-fun ci() Bool(");
    int BracketCounter = 0;

    if (inputVariables.size() != 0) {
      String last = inputVariables.get(inputVariables.size()-1);
      for (int i=0; i<inputVariables.size(); i++) {
        String variable = inputVariables.get(i);
        if (outputVariables.size()==0 && variable.equals(last)) {
          sb.append(getAssignmentOfVariableToZero(variable, false));
        } else {
          sb.append("(and ");
          sb.append(getAssignmentOfVariableToZero(variable, false));
          BracketCounter++;
        }
      }
    }

    if (outputVariables.size() != 0) {
      String last = outputVariables.get(outputVariables.size()-1);
      for (int i=0; i<outputVariables.size(); i++) {
        String variable = outputVariables.get(i);
        if (variable.equals(last)) {
          sb.append(" ");
          sb.append(getAssignmentOfVariableToZero(variable, true));
        } else {
          sb.append("(and ");
          sb.append(getAssignmentOfVariableToZero(variable, true));
          BracketCounter++;
        }
      }
    }

    for (int i=0; i<BracketCounter+1; i++) { // +1 because of the Bracket of define-fun ci Bool(...)
      sb.append(")");
    }

    List<String> outputVariableList = new ArrayList<>();
    for (String var : outputVariables) {
      outputVariableList.add("(declare-fun " + var + "@1 () Int)");
    }
    return Pair.of(outputVariableList, sb.toString());
  }

  /**
   * Returns String of the given variable: if it is an outputVariable (= variable@1 0), otherwise (= variable 0)
   * @param var String of variable
   * @param isOutputVariable boolean if the variable is an output variable
   * @return
   */
  private String getAssignmentOfVariableToZero(final String var, final boolean isOutputVariable) {
    StringBuilder sb = new StringBuilder();
    sb.append("(= ");
    sb.append(var);
    if (isOutputVariable) {
      sb.append("@1");
    }
    sb.append(" 0)");
    return sb.toString();
  }

  /**
   * Returns AppliedCustomInstruction which begins at the given aciStartNode.
   * @param aciStartNode
   * @return the resulting AppliedCustomInstruction
   * @throws InterruptedException due to the shutdownNotifier
   * @throws AppliedCustomInstructionParsingFailedException if the matching of the variables of ci and aci
   * is not clear, or their structure dosen't fit.
   */
  public AppliedCustomInstruction inspectAppliedCustomInstruction(final CFANode aciStartNode)
        throws InterruptedException, AppliedCustomInstructionParsingFailedException {
    Map<String, String> mapping = new HashMap<>();
    Set<String> outVariables = new HashSet<>();
    Set<CFANode> aciEndNodes = new HashSet<>();
    Set<Pair<CFANode, CFANode>> visitedNodes = new HashSet<>();
    Queue<Pair<CFANode, CFANode>> queue = new ArrayDeque<>();

    visitedNodes.add(Pair.of(ciStartNode, aciStartNode));
    queue.add(Pair.of(ciStartNode, aciStartNode));

    CFANode ciPred;
    CFANode aciPred;

    while (!queue.isEmpty()) {
      shutdownNotifier.shutdownIfNecessary();
      Pair<CFANode, CFANode> nextPair = queue.poll();
      ciPred = nextPair.getFirst();
      aciPred = nextPair.getSecond();

      if (ciEndNodes.contains(ciPred)) {
        aciEndNodes.add(aciPred);
        continue;
      }

      for (int i=0; i<ciPred.getNumLeavingEdges(); i++) {
        // Custom Instruction
        CFAEdge ciEdge = ciPred.getLeavingEdge(i);
        CFANode ciSucc = ciEdge.getSuccessor();

        // Applied Custom Instruction
        CFAEdge aciEdge = aciPred.getLeavingEdge(i);
        CFANode aciSucc = aciEdge.getSuccessor();

        Map<String,String> currentCiVarToAciVar = new HashMap<>(); // required for decide which variables will be the output variables
        computeMappingOfCiAndAci(ciEdge, aciEdge, mapping, currentCiVarToAciVar, outVariables);

        // breadth-first-search
        if (!visitedNodes.contains(Pair.of(ciSucc, aciSucc))) {
          queue.add(Pair.of(ciSucc, aciSucc));
          visitedNodes.add(Pair.of(ciSucc, aciSucc));
        }
      }
    }

    if (ciEndNodes.size() != aciEndNodes.size()) {
      throw new AppliedCustomInstructionParsingFailedException("The amout of endNodes of ci and aci are different!");
    }

    SSAMapBuilder ssaMapBuilder = SSAMap.emptySSAMap().builder();
    for (String var : outVariables) {
      ssaMapBuilder.setIndex(var,new CSimpleType(false, false, CBasicType.INT, false, false, false, false, false, false, false), 1);
    }

    return new AppliedCustomInstruction(aciStartNode, aciEndNodes, getFakeSMTDescriptionForACI(mapping), ssaMapBuilder.build());
  }

  /**
   * Returns the (fake!) SMT description of the before mapped variables of aci, which is a
   * conjunctions of output variables and predicates (IVj = 0) for each input variable j.
   * Note that this is prefix notation, and that the variables of aci are used instead of
   * those from the ci!
   * @return (define-fun aci Bool((and (= IV1 0) (and (= IV2 0) (and OV1 OV2))))
   */
  private Pair<List<String>, String> getFakeSMTDescriptionForACI(final Map<String,String> map) {
    if (inputVariables.size() == 0 && outputVariables.size() == 0) {
      return Pair.of(Collections.<String> emptyList(),
        "(define-fun ci() Bool true)");
    }
    StringBuilder sb = new StringBuilder();
    sb.append("(define-fun aci() Bool(");
    int BracketCounter = 0;

    if (inputVariables.size() != 0) {
      String last = inputVariables.get(inputVariables.size()-1);
      for (int i=0; i<inputVariables.size(); i++) {
        String variable = inputVariables.get(i);
        if (outputVariables.size()==0 && variable.equals(last)) {
          sb.append(getAssignmentOfVariableToZero(map.get(variable), false));
        } else {
          sb.append("(and ");
          sb.append(getAssignmentOfVariableToZero(map.get(variable), false));
          BracketCounter++;
        }
      }
    }

    if (outputVariables.size() != 0) {
      String last = outputVariables.get(outputVariables.size()-1);
      for (int i=0; i<outputVariables.size(); i++) {
        String variable = outputVariables.get(i);
        if (variable.equals(last)) {
          sb.append(" ");
          sb.append(getAssignmentOfVariableToZero(map.get(variable), true));
        } else {
          sb.append("(and ");
          sb.append(getAssignmentOfVariableToZero(map.get(variable), true));
          BracketCounter++;
        }
      }
    }

    for (int i=0; i<BracketCounter+1; i++) { // +1 because of the Bracket of define-fun ci Bool(...)
      sb.append(")");
    }

    List<String> outputVariableList = new ArrayList<>();
    for (String var : outputVariables) {
      outputVariableList.add("(declare-fun " + map.get(var) + "@1 () Int)");
    }
    return Pair.of(outputVariableList, sb.toString());
  }


  /**
   * Computes the mapping of variables of the given CI and ACI.
   * That means the structure of the ci is compared to the aci's structure.
   * All variables of the CI and ACI will be mapped, except those
   * which have different types. The latter ones will throw exceptions.
   * The mapping will be stored in the given Map ciVarToAciVar.
   * @param ciEdge CFAEdge of CustomInstruction (CI)
   * @param aciEdge CFAEdge of AppliedCustomInstruction (ACI)
   * @param ciVarToAciVar Map of variables of CI and ACI
   * @param currentCiVarToAciVar Map of variables of CI and ACI of the current edge
   * @param outVariables Collection of output variables
   * @throws AppliedCustomInstructionParsingFailedException
   */
  private void computeMappingOfCiAndAci(final CFAEdge ciEdge, final CFAEdge aciEdge,
      final Map<String, String> ciVarToAciVar, final Map<String,String> currentCiVarToAciVar,
      final Collection<String> outVariables)
          throws AppliedCustomInstructionParsingFailedException{

    if (ciEdge.getEdgeType() != aciEdge.getEdgeType()) {
      throw new AppliedCustomInstructionParsingFailedException("The edgeType of " + ciEdge + " and " + aciEdge + " are different.");
    }

    switch(ciEdge.getEdgeType()) {
      case BlankEdge:
        // no additional check needed.
        return;
      case AssumeEdge:
        compareAssumeEdge((CAssumeEdge) ciEdge, (CAssumeEdge) aciEdge, ciVarToAciVar, currentCiVarToAciVar, outVariables);
        return;
      case StatementEdge:
        compareStatementEdge((CStatementEdge) ciEdge, (CStatementEdge) aciEdge, ciVarToAciVar, currentCiVarToAciVar, outVariables);
        return;
      case DeclarationEdge:
        compareDeclarationEdge((CDeclarationEdge) ciEdge, (CDeclarationEdge) aciEdge, ciVarToAciVar, currentCiVarToAciVar, outVariables);
        return;
      case ReturnStatementEdge:
        compareReturnStatementEdge((CReturnStatementEdge) ciEdge, (CReturnStatementEdge) aciEdge, ciVarToAciVar, currentCiVarToAciVar, outVariables);
        return;
      case FunctionCallEdge:
        compareFunctionCallEdge((CFunctionCallEdge)ciEdge, (CFunctionCallEdge)aciEdge, ciVarToAciVar, currentCiVarToAciVar, outVariables);
        return;
      case FunctionReturnEdge:
        // no additional check needed.
        return;
      case MultiEdge:
        compareMultiEdge((MultiEdge) ciEdge, (MultiEdge) aciEdge, ciVarToAciVar, currentCiVarToAciVar, outVariables);
        return;
      case CallToReturnEdge:
        throw new AppliedCustomInstructionParsingFailedException("The ci edge " + ciEdge + " is a CallToReturnEdge, which is not supported!");
    }
  }

  private void compareAssumeEdge(final CAssumeEdge ciEdge, final CAssumeEdge aciEdge,
      final Map<String,String> ciVarToAciVar, final Map<String,String> currentCiVarToAciVar,
      final Collection<String> outVariables) throws AppliedCustomInstructionParsingFailedException {

    if (ciEdge.getTruthAssumption() != aciEdge.getTruthAssumption()) {
      throw new AppliedCustomInstructionParsingFailedException("The truthAssumption of the CAssumeEdges " + ciEdge + " and " + aciEdge + "are different!");
    }
    ciEdge.getExpression().accept(new StructureComparisonVisitor(aciEdge.getExpression(), ciVarToAciVar, currentCiVarToAciVar));
  }

  private void compareStatementEdge(final CStatementEdge ciEdge, final CStatementEdge aciEdge,
      final Map<String,String> ciVarToAciVar, final Map<String,String> currentCiVarToAciVar,
      final Collection<String> outVariables) throws AppliedCustomInstructionParsingFailedException {

    if (ciEdge.getStatement() instanceof CFunctionSummaryStatementEdge && aciEdge.getStatement() instanceof CFunctionSummaryStatementEdge) {
      CFunctionSummaryStatementEdge ciStmt = (CFunctionSummaryStatementEdge) ciEdge.getStatement();
      CFunctionSummaryStatementEdge aciStmt = (CFunctionSummaryStatementEdge) aciEdge.getStatement();

      if (!ciStmt.getFunctionName().equals(aciStmt.getFunctionName())){
        throw new AppliedCustomInstructionParsingFailedException("The functionName of the CFunctionSummaryStatementEdges " + ciEdge + " and " + aciEdge + " are different!");
      }

      compareStatementsOfStatementEdge(ciStmt.getFunctionCall(), aciStmt.getFunctionCall(), ciVarToAciVar, currentCiVarToAciVar, outVariables);

    }
    compareStatementsOfStatementEdge(ciEdge.getStatement(), aciEdge.getStatement(), ciVarToAciVar, currentCiVarToAciVar, outVariables);

  }

  private void compareStatementsOfStatementEdge(final CStatement ci, final CStatement aci,
      final Map<String, String> ciVarToAciVar, final Map<String, String> currentCiVarToAciVar,
      final Collection<String> outVariables) throws AppliedCustomInstructionParsingFailedException {

    if (ci instanceof CExpressionAssignmentStatement && aci instanceof CExpressionAssignmentStatement) {
      CExpressionAssignmentStatement ciStmt = (CExpressionAssignmentStatement) ci;
      CExpressionAssignmentStatement aciStmt = (CExpressionAssignmentStatement) aci;

      // left side => output variables
      ciStmt.getLeftHandSide().accept(new StructureComparisonVisitor(aciStmt.getLeftHandSide(), ciVarToAciVar, currentCiVarToAciVar));
      outVariables.addAll(currentCiVarToAciVar.keySet());

      // right side: just proof it
      ciStmt.getRightHandSide().accept(new StructureComparisonVisitor(aciStmt.getRightHandSide(), ciVarToAciVar, currentCiVarToAciVar));
    }

    else if (ci instanceof CExpressionStatement && aci instanceof CExpressionStatement) {
      CExpressionStatement ciStmt = (CExpressionStatement) ci;
      CExpressionStatement aciStmt = (CExpressionStatement) aci;
      ciStmt.getExpression().accept(new StructureComparisonVisitor(aciStmt.getExpression(), ciVarToAciVar, currentCiVarToAciVar));
    }

    else if (ci instanceof CFunctionCallAssignmentStatement && aci instanceof CFunctionCallAssignmentStatement) {
      CFunctionCallAssignmentStatement ciStmt = (CFunctionCallAssignmentStatement) ci;
      CFunctionCallAssignmentStatement aciStmt = (CFunctionCallAssignmentStatement) aci;

      // left side => output variables
      ciStmt.getLeftHandSide().accept(new StructureComparisonVisitor(aciStmt.getLeftHandSide(), ciVarToAciVar, currentCiVarToAciVar));
      outVariables.addAll(ciVarToAciVar.keySet());

      compareFunctionCallExpressions(ciStmt.getFunctionCallExpression(), aciStmt.getFunctionCallExpression(), ciVarToAciVar, currentCiVarToAciVar, outVariables);
    }

    else if (ci instanceof CFunctionCallStatement && aci instanceof CFunctionCallStatement) {
      CFunctionCallStatement ciStmt = (CFunctionCallStatement) ci;
      CFunctionCallStatement aciStmt = (CFunctionCallStatement) aci;

      compareFunctionCallExpressions(ciStmt.getFunctionCallExpression(), aciStmt.getFunctionCallExpression(), ciVarToAciVar, currentCiVarToAciVar, outVariables);
    }

    else {
      throw new AppliedCustomInstructionParsingFailedException("The types of the CStatement " + ci + " and " + aci + " are different!");
    }
  }

  private void compareFunctionCallExpressions(final CFunctionCallExpression exp,
      final CFunctionCallExpression aexp, final Map<String, String> ciVarToAciVar,
      final Map<String, String> currentCiVarToAciVar, final Collection<String> outVariables)
          throws AppliedCustomInstructionParsingFailedException {
    if (!exp.getExpressionType().equals(aexp.getExpressionType())){
      throw new AppliedCustomInstructionParsingFailedException("The expressionType of the CStatementEdges " + exp + " and " + aexp + " are different!");
    }

    exp.getFunctionNameExpression().accept(
        new StructureComparisonVisitor(aexp.getFunctionNameExpression(), ciVarToAciVar, currentCiVarToAciVar));

    List<CExpression> ciList = exp.getParameterExpressions();
    List<CExpression> aciList = aexp.getParameterExpressions();
    for (int i=0; i<ciList.size(); i++) {
      ciList.get(i).accept(new StructureComparisonVisitor(aciList.get(i), ciVarToAciVar, currentCiVarToAciVar));
    }
  }

  private void compareDeclarationEdge(final CDeclarationEdge ciEdge, final CDeclarationEdge aciEdge,
      final Map<String,String> ciVarToAciVar, final Map<String,String> currentCiVarToAciVar,
      final Collection<String> outVariables)
        throws AppliedCustomInstructionParsingFailedException {

    CDeclaration ciDec = ciEdge.getDeclaration();
    CDeclaration aciDec = aciEdge.getDeclaration();

    if (ciDec instanceof CVariableDeclaration && aciDec instanceof CVariableDeclaration) {
      CVariableDeclaration ciVarDec = (CVariableDeclaration) ciDec;
      CVariableDeclaration aciVarDec = (CVariableDeclaration) aciDec;

      if (!ciVarDec.getCStorageClass().equals(aciVarDec.getCStorageClass())) {
        throw new AppliedCustomInstructionParsingFailedException("The CVariableDeclaration of ci " + ciVarDec + " and aci " + aciVarDec + " have different StorageClasses.");
      }
      if (!ciVarDec.getType().equals(aciVarDec.getType())) {
        throw new AppliedCustomInstructionParsingFailedException("The CVariableDeclaration of ci " + ciVarDec + " and aci " + aciVarDec + " have different declaration types!");
      }
      if (!ciDec.getQualifiedName().equals(aciDec.getQualifiedName())) {
        throw new AppliedCustomInstructionParsingFailedException("The CVariableDeclaration of ci " + ciVarDec + " and aci " + aciVarDec + " have different qualified names!");
      }

      compareInitializer(ciVarDec.getInitializer(), aciVarDec.getInitializer(), ciVarToAciVar, currentCiVarToAciVar, outVariables);
      if (ciVarDec.getInitializer() != null) {
        if (ciVarDec.getInitializer() instanceof CInitializerExpression
            || ciVarDec.getInitializer() instanceof CInitializerList) {
          outVariables.add(ciVarDec.getName());
        } else {
          throw new AppliedCustomInstructionParsingFailedException("Unsupported initializer: "
              + ciVarDec.getInitializer());
        }
      }
    }

    else if (ciDec instanceof CComplexTypeDeclaration && aciDec instanceof CComplexTypeDeclaration) {
      throw new AppliedCustomInstructionParsingFailedException("The code contains a CComplexTypeDeclaration, which is unsupported.");
    }
    else if (ciDec instanceof CTypeDefDeclaration && aciDec instanceof CTypeDefDeclaration) {
      throw new AppliedCustomInstructionParsingFailedException("The code contains a CTypeDefDeclaration, which is unsupported.");
    }
    else if (ciDec instanceof CFunctionDeclaration && aciDec instanceof CFunctionDeclaration) {
      throw new AppliedCustomInstructionParsingFailedException("The code contains a CFunctionDeclaration, which is unsupported.");
    } else {
      throw new AppliedCustomInstructionParsingFailedException("The declaration of the CDeclarationEdge ci " + ciDec + " and aci " + aciDec + " have different classes.");
    }
  }

  private void compareInitializer(final CInitializer ciI, final CInitializer aciI,
      final Map<String,String> ciVarToAciVar, final Map<String,String> currentCiVarToAciVar,
      final Collection<String> outVariables)
      throws AppliedCustomInstructionParsingFailedException {

    if (ciI instanceof CInitializerExpression && aciI instanceof CInitializerExpression) {
      ((CInitializerExpression) ciI).getExpression().accept(new StructureComparisonVisitor(((CInitializerExpression) aciI).getExpression(), ciVarToAciVar, currentCiVarToAciVar));
    }

    else if (ciI instanceof CDesignatedInitializer && aciI instanceof CDesignatedInitializer) {
      throw new AppliedCustomInstructionParsingFailedException("The code contains a CDesignatedInitializer, which is unsupported.");
    }

    else if (ciI instanceof CInitializerList && aciI instanceof CInitializerList) {
      List<CInitializer> ciList = ((CInitializerList) ciI).getInitializers();
      List<CInitializer> aciList = ((CInitializerList) aciI).getInitializers();

      if (ciList.size() != aciList.size()) {
        throw new AppliedCustomInstructionParsingFailedException("The CInitializerList of the Initializer of ci " + ciI + " and aci " + aciI + " have different length.");
      } else {
        for (int i=0; i<ciList.size(); i++) {
          compareInitializer(ciList.get(i), aciList.get(i), ciVarToAciVar, currentCiVarToAciVar, outVariables);
        }
      }
    } else {
      throw new AppliedCustomInstructionParsingFailedException("The CInitializer of ci " + ciI + " and aci " + aciI + " are different.");
    }
  }

  private void compareReturnStatementEdge(final CReturnStatementEdge ciEdge,
      final CReturnStatementEdge aciEdge, final Map<String,String> ciVarToAciVar,
      final Map<String,String> currentCiVarToAciVar, final Collection<String> outVariables)
          throws AppliedCustomInstructionParsingFailedException {

    if (ciEdge.getExpression().isPresent() && aciEdge.getExpression().isPresent()){
      ciEdge.getExpression().get().accept(new StructureComparisonVisitor(aciEdge.getExpression().get(), ciVarToAciVar, currentCiVarToAciVar));

    } else if ((!ciEdge.getExpression().isPresent() && aciEdge.getExpression().isPresent())
          ||(ciEdge.getExpression().isPresent() && !aciEdge.getExpression().isPresent()) ){
      throw new AppliedCustomInstructionParsingFailedException("The expression of the CReturnStatementEdge of ci " + ciEdge + " and aci " +  aciEdge + " is present in one of them, but not in the otherone.");
    }
  }

  private void compareFunctionCallEdge(final CFunctionCallEdge ciEdge, final CFunctionCallEdge aciEdge,
      final Map<String,String> ciVarToAciVar, final Map<String,String> currentCiVarToAciVar,
      final Collection<String> outVariables) throws AppliedCustomInstructionParsingFailedException {

    List<CExpression> ciArguments = ciEdge.getArguments();
    List<CExpression> aciArguments = aciEdge.getArguments();
    if (ciArguments.size() != aciArguments.size()) {
      throw new AppliedCustomInstructionParsingFailedException("The amount of arguments of the FunctionCallEdges " + ciEdge + " and " + aciEdge + " are different!");
    }
    for (int i=0; i<ciArguments.size(); i++) {
      ciArguments.get(i).accept(new StructureComparisonVisitor(aciArguments.get(i), ciVarToAciVar, currentCiVarToAciVar));
    }
  }

  private void compareMultiEdge(final MultiEdge ciEdge, final MultiEdge aciEdge,
      final Map<String,String> ciVarToAciVar, final Map<String,String> currentCiVarToAciVar,
      final Collection<String> outVariables)
      throws AppliedCustomInstructionParsingFailedException {
    if (ciEdge.getEdges().size() != aciEdge.getEdges().size()) {
      throw new AppliedCustomInstructionParsingFailedException("The MulitEdges of ci " + ciEdge + " and aci " + aciEdge + " have a different amount of edges");
    }
    for (int i=0; i<ciEdge.getEdges().size(); i++) {
      computeMappingOfCiAndAci(ciEdge.getEdges().get(i), aciEdge.getEdges().get(i), ciVarToAciVar, currentCiVarToAciVar, outVariables);
    }
  }

  private static class StructureComparisonVisitor implements CExpressionVisitor<Void, AppliedCustomInstructionParsingFailedException>{

    private CExpression aciExp;
    private final Map<String,String> ciVarToAciVar;
    private final Map<String,String> currentCiVarToAciVar;

    public StructureComparisonVisitor(final CExpression pAciExp, final Map<String,String> pCiVarToAciVar, final Map<String,String> pCurrentCiVarToAciVar) {
      aciExp = pAciExp;
      ciVarToAciVar = pCiVarToAciVar;
      currentCiVarToAciVar = pCurrentCiVarToAciVar;
    }

    @Override
    public Void visit(final CArraySubscriptExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CArraySubscriptExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CArraySubscriptExpression, but ci is.");
      }
      CArraySubscriptExpression aciAExp = (CArraySubscriptExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of ci " + ciExp + " and aci " + aciExp + " are different.");
      }

      aciExp = aciAExp.getArrayExpression();
      ciExp.getArrayExpression().accept(this);

      aciExp = aciAExp.getSubscriptExpression();
      ciExp.getSubscriptExpression().accept(this);
      return null;
    }

    @Override
    public Void visit(final CFieldReference ciExp) throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CFieldReference)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CFieldReference, but ci is.");
      }
      CFieldReference aciFieldRefExp = (CFieldReference) aciExp;
      if (!ciExp.getExpressionType().equals(aciFieldRefExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the FieldReference of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciFieldRefExp + " (" + aciFieldRefExp.getExpressionType() + ").");
      }
      if (ciExp.isPointerDereference() != aciFieldRefExp.isPointerDereference()) {
        throw new AppliedCustomInstructionParsingFailedException("One of the ci " + ciExp + " and aci " + aciFieldRefExp + " is a pointerDereference, while the other one not.");
      }
      this.aciExp = aciFieldRefExp.getFieldOwner();
      ciExp.getFieldOwner().accept(this);
      return null;
    }

    @Override
    public Void visit(final CIdExpression ciExp) throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CIdExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CIdExpression, but ci is.");
      }
      if (aciExp instanceof CIdExpression) {
        CIdExpression aciIdExp = (CIdExpression) aciExp;
        if (!ciExp.getExpressionType().equals(aciIdExp.getExpressionType())) {
          throw new AppliedCustomInstructionParsingFailedException("The expression type of the IdExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciIdExp + " (" + aciIdExp.getExpressionType() + ").");
        }
        if (ciVarToAciVar.containsKey(ciExp.getName()) && !ciVarToAciVar.get(ciExp.getName()).equals(aciIdExp.getName())) {
          throw new AppliedCustomInstructionParsingFailedException("The mapping is not clear. The map contains " + ciExp.getName() + " with the value " + ciVarToAciVar.get(ciExp.getName()) + ", which is different to " + aciIdExp.getName() + ".");
        } else {
          ciVarToAciVar.put(ciExp.getName(), aciIdExp.getName());
          currentCiVarToAciVar.put(ciExp.getName(), aciIdExp.getName());
        }
      }

      else if (aciExp instanceof CCharLiteralExpression) {
        throw new AppliedCustomInstructionParsingFailedException("The code contains a CCharLiteralExpression, which is unsupported.");
      }

      else if (aciExp instanceof CStringLiteralExpression) {
        throw new AppliedCustomInstructionParsingFailedException("The code contains a CStringLiteralExpression, which is unsupported.");
      }

      else if (aciExp instanceof CImaginaryLiteralExpression) {
        throw new AppliedCustomInstructionParsingFailedException("The code contains a CImaginaryLiteralExpression, which is unsupported.");
      }

      else if (aciExp instanceof CIntegerLiteralExpression) {
        compareSimpleTypes(ciExp, ((CIntegerLiteralExpression) aciExp).getValue(), (CSimpleType) aciExp.getExpressionType());
      }

      else if (aciExp instanceof CFloatLiteralExpression) {
        compareSimpleTypes(ciExp, ((CFloatLiteralExpression) aciExp).getValue(), (CSimpleType) aciExp.getExpressionType());
      } else {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + ciExp + " is not a CSimpleType.");
      }
      return null;
    }

    private void compareSimpleTypes(final CIdExpression ciExp, final Number aciExpValue, final CSimpleType aciType) throws AppliedCustomInstructionParsingFailedException {
      if (ciExp.getExpressionType() instanceof CSimpleType) {
        CSimpleType ciST = (CSimpleType) ciExp.getExpressionType();

        if (isValidSimpleType(ciST, aciType)) {
          if (!ciVarToAciVar.containsKey(ciExp.getName())) {
            ciVarToAciVar.put(ciExp.getName(), aciExpValue.toString());
          } else if (!ciVarToAciVar.get(ciExp.getName()).equals(aciExpValue.toString())) {
            throw new AppliedCustomInstructionParsingFailedException("The mapping is not clear. The map contains " + ciExp.getName() + " with the value " + ciVarToAciVar.get(ciExp.getName()) + ", which is different to " + aciExpValue.toString() + ".");
          }
        } else {
          throw new AppliedCustomInstructionParsingFailedException("The simpleType of the ci " + ciExp + " is not a valid one.");
        }
      } else {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not a CIdExpression.");
      }
    }

    private boolean isValidSimpleType(final CSimpleType ciST, final CSimpleType pAciType) {
      if ((ciST.getType().isIntegerType() || ciST.getType().isFloatingPointType())
          && ciST.isComplex() == ciST.isImaginary() && ciST.isImaginary() == ciST.isLong()
          && ciST.isLong() == ciST.isLongLong() && ciST.isLongLong() == ciST.isShort()
          && ciST.isShort() == ciST.isSigned() && ciST.isSigned() == ciST.isUnsigned()) {
        return true;
      }
      return false;
    }

    @Override
    public Void visit(final CPointerExpression ciExp) throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CPointerExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CPointerExpression, but ci is.");
      }
      CPointerExpression aciPExp = (CPointerExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciPExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the CPointerExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciPExp + " (" + aciPExp.getExpressionType() + ").");
      }
      this.aciExp = aciPExp.getOperand();
      ciExp.getOperand().accept(this);
      return null;
    }

    @Override
    public Void visit(final CComplexCastExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CComplexCastExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CComplexCastExpression, but ci is.");
      }
      CComplexCastExpression aciCExp = (CComplexCastExpression) aciExp;
      if (ciExp.isImaginaryCast() != aciCExp.isImaginaryCast()) {
        throw new AppliedCustomInstructionParsingFailedException("One of the ci " + ciExp + " and aci " + aciCExp + " is an imaginaryCast, while the other one not.");
      }
      if (ciExp.isRealCast() != aciCExp.isRealCast()) {
        throw new AppliedCustomInstructionParsingFailedException("One of the ci " + ciExp + " and aci " + aciCExp + " is a realCast, while the other one not.");
      }
      if (!ciExp.getExpressionType().equals(aciCExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the CComplexCastExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciCExp + " (" + aciCExp.getExpressionType() + ").");
      }
      if (!ciExp.getType().equals(aciCExp.getType())) {
        throw new AppliedCustomInstructionParsingFailedException("The type of the CComplexCastExpression of ci " + ciExp + " (" + ciExp.getType() + ") is not equal to the one of the aci " + aciCExp + " (" + aciCExp.getType() + ").");
      }
      this.aciExp = aciCExp.getOperand();
      ciExp.getOperand().accept(this);
      return null;
    }

    @Override
    public Void visit(final CBinaryExpression ciExp) throws AppliedCustomInstructionParsingFailedException {

      if (aciExp instanceof CBinaryExpression) {
        CBinaryExpression aciBinExp = (CBinaryExpression) aciExp;

        // expression types are different
        if (!ciExp.getExpressionType().equals(aciBinExp.getExpressionType())) {
          throw new AppliedCustomInstructionParsingFailedException("The expression type of the CBinaryExpression of ci " + ciExp + " is not equal to the one of the aci " + aciBinExp + ".");
        }

        // operators are different
        if (!ciExp.getOperator().getOperator().equals(aciBinExp.getOperator().getOperator())) {
          throw new AppliedCustomInstructionParsingFailedException("The operators of the CBinaryExpression the ci " + ciExp  + " and aci " + aciBinExp + " are different.");
        }

        if (!ciExp.getCalculationType().equals(aciBinExp.getCalculationType())) {
          throw new AppliedCustomInstructionParsingFailedException("The calculationType of the CBinaryExpression of ci " + ciExp + " and aci " + aciBinExp + " are different.");
        }

        aciExp = aciBinExp.getOperand1();
        ciExp.getOperand1().accept(this);

        aciExp = aciBinExp.getOperand2();
        ciExp.getOperand2().accept(this);

      } else {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CBinaryExpression, but ci is.");
      }

      return null;
    }

    @Override
    public Void visit(final CCastExpression ciExp) throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CCastExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CCastExpression, but ci is.");
      }
      CCastExpression aciPExp = (CCastExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciPExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the CCastExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciPExp + " (" + aciPExp.getExpressionType() + ").");
      }
      this.aciExp = aciPExp.getOperand();
      ciExp.getOperand().accept(this);
      return null;
    }

    @Override
    public Void visit(final CCharLiteralExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CCharLiteralExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CCharLiteralExpression, but ci is.");
      }
      CCharLiteralExpression aciCharExp = (CCharLiteralExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciCharExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the CharLiteralExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciCharExp + " (" + aciCharExp.getExpressionType() + ").");
      }
      if (ciExp.getCharacter() == aciCharExp.getCharacter()) {
        throw new AppliedCustomInstructionParsingFailedException("The value of the CCharLiteralExpression of ci " + ciExp + " and aci " + aciCharExp + " are different.");
      }
      return null;
    }

    @Override
    public Void visit(final CFloatLiteralExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CFloatLiteralExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CFloatLiteralExpression, but ci is.");
      }
      CFloatLiteralExpression aciFloatExp = (CFloatLiteralExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciFloatExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the FloatLiteralExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciFloatExp + " (" + aciFloatExp.getExpressionType() + ").");
      }
      if (ciExp.getValue().equals(aciFloatExp.getValue())) {
        throw new AppliedCustomInstructionParsingFailedException("The value of the CCharLiteralExpression of ci " + ciExp + " and aci " + aciFloatExp + " are different.");
      }
      return null;
    }

    @Override
    public Void visit(final CIntegerLiteralExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CIntegerLiteralExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CIntegerLiteralExpression, but ci is.");
      }
      CIntegerLiteralExpression aciIntegerLiteralExp = (CIntegerLiteralExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciIntegerLiteralExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the IntegerLiteralExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciIntegerLiteralExp + " (" + aciIntegerLiteralExp.getExpressionType() + ").");
      }
      if (ciExp.getValue().equals(aciIntegerLiteralExp.getValue())) {
        throw new AppliedCustomInstructionParsingFailedException("The value of the CIntegerLiteralExpression of ci " + ciExp + " and aci " + aciIntegerLiteralExp + " are different.");
      }
      return null;
    }

    @Override
    public Void visit(final CStringLiteralExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CStringLiteralExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CStringLiteralExpression, but ci is.");
      }
      CStringLiteralExpression aciStringLiteralExp = (CStringLiteralExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciStringLiteralExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the StringLiteralExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciStringLiteralExp + " (" + aciStringLiteralExp.getExpressionType() + ").");
      }
      if (ciExp.getValue().equals(aciStringLiteralExp.getValue())) {
        throw new AppliedCustomInstructionParsingFailedException("The value of the CIntegerLiteralExpression of ci " + ciExp + " and aci " + aciStringLiteralExp + " are different.");
      }
      return null;
    }

    @Override
    public Void visit(final CTypeIdExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CTypeIdExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CTypeIdExpression, but ci is.");
      }
      CTypeIdExpression aciIdExp = (CTypeIdExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciIdExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the CTypeIdExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciIdExp + " (" + aciIdExp.getExpressionType() + ").");
      }
      if (!ciExp.getType().equals(aciIdExp.getType())) {
        throw new AppliedCustomInstructionParsingFailedException("The type of the CTypeIdExpression of ci " + ciExp + " (" + ciExp.getType() + ") is not equal to the one of the aci " + aciIdExp + " (" + aciIdExp.getType() + ").");
      }
      if (!ciExp.getOperator().equals(aciIdExp.getOperator())) {
        throw new AppliedCustomInstructionParsingFailedException("The operator of the CTypeIdExpression of ci " + ciExp + " (" + ciExp.getOperator() + ") is not equal to the one of the aci " + aciIdExp + " (" + aciIdExp.getOperator() + ").");
      }
      return null;
    }

    @Override
    public Void visit(final CUnaryExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {

      if (aciExp instanceof CUnaryExpression) {
        CUnaryExpression aciUnExp = (CUnaryExpression) aciExp;

        // expression types are different
        if (!ciExp.getExpressionType().equals(aciUnExp.getExpressionType())) {
          throw new AppliedCustomInstructionParsingFailedException("The expression type of the CUnaryExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciUnExp + " (" + aciUnExp.getExpressionType() + ").");
        }

        // operators are different
        if (!ciExp.getOperator().getOperator().equals(aciUnExp.getOperator().getOperator())) {
          throw new AppliedCustomInstructionParsingFailedException("The operators of the ci expression " + ciExp  + " and aci expression " + aciUnExp + " don't fit together!");
        }

        this.aciExp = aciUnExp.getOperand();
        ciExp.getOperand().accept(this);

      } else {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type UnaryExpression, but ci is.");
      }

      return null;
    }

    @Override
    public Void visit(final CImaginaryLiteralExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
//      if (!(aciExp instanceof CImaginaryLiteralExpression)) {
//        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CImaginaryLiteralExpression, but ci is.");
//      }
//      CImaginaryLiteralExpression aciIExp = (CImaginaryLiteralExpression) aciExp;
//      if (!ciExp.getExpressionType().equals(aciIExp.getExpressionType())) {
//        throw new AppliedCustomInstructionParsingFailedException("The expression type of the CImaginaryLiteralExpression of ci " + ciExp + " and aci " + aciIExp + " are different.");
//      }
//      ciExp.getValue().accept(new StructureComparisonVisitor(aciIExp.getValue(), ciVarToAciVar, currentCiVarToAciVar));
//      return null;
      throw new AppliedCustomInstructionParsingFailedException("The code contains a CImaginaryLiteralExpression, which is unsupported.");
    }

    @Override
    public Void visit(final CAddressOfLabelExpression ciExp)
        throws AppliedCustomInstructionParsingFailedException {
      if (!(aciExp instanceof CAddressOfLabelExpression)) {
        throw new AppliedCustomInstructionParsingFailedException("The aci expression " + aciExp + " is not from the type CAddressOfLabelExpression, but ci is.");
      }
      CAddressOfLabelExpression aciAExp = (CAddressOfLabelExpression) aciExp;
      if (!ciExp.getExpressionType().equals(aciAExp.getExpressionType())) {
        throw new AppliedCustomInstructionParsingFailedException("The expression type of the CAddressOfLabelExpression of ci " + ciExp + " (" + ciExp.getExpressionType() + ") is not equal to the one of the aci " + aciAExp + " (" + aciAExp.getExpressionType() + ").");
      }
      if (!ciExp.getLabelName().equals(aciAExp.getLabelName())) {
        throw new AppliedCustomInstructionParsingFailedException("The label name of the CAddressOfLabelExpression of ci " + ciExp + " and aci " + aciAExp + " are different.");
      }
      return null;
    }

  }
}
