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
package org.sosy_lab.cpachecker.util.refiner;

import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.core.interfaces.Precision;
import org.sosy_lab.cpachecker.exceptions.CPAException;

import com.google.common.base.Optional;

/**
 * Interface for the strongest post-operator as used in CEGAR.
 */
public interface StrongestPostOperator {

  /**
   * Computes the abstract state that represents the region of states reachable from the given state
   * by executing the given operation.
   * If the resulting abstract state is contradicting, that means that it represents no
   * concrete states, an empty <code>Optional</code> is returned.
   *
   *
   * @param origin the abstract state the computation is started at
   * @param precision th precision to use for the computation
   * @param operation the operation to perform
   * @return an <code>Optional</code> containing the computed abstract state. An empty
   *    <code>Optional</code>, if the resulting abstracted state is contradicting
   */
  Optional<? extends AbstractState> getStrongestPost(
      AbstractState origin, Precision precision, CFAEdge operation) throws CPAException;
}