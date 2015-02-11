/*
 * CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2014  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.constraints;

import org.sosy_lab.cpachecker.core.counterexample.Model;
import org.sosy_lab.cpachecker.cpa.constraints.constraint.Constraint;
import org.sosy_lab.cpachecker.cpa.value.type.symbolic.SymbolicValueVisitor;
import org.sosy_lab.cpachecker.util.predicates.interfaces.BooleanFormula;
import org.sosy_lab.cpachecker.util.predicates.interfaces.Formula;


/**
 * Class for creating {@link Formula}s out of {@link Constraint}s
 *
 * @param T concrete {@link Formula} type the implementation creates
 */
public interface FormulaCreator<T extends Formula> extends ConstraintVisitor<T>, SymbolicValueVisitor<T> {
  BooleanFormula createFormula(Constraint pConstraint);

  BooleanFormula transformAssignment(Model.AssignableTerm pTerm, Object termAssignment);
}