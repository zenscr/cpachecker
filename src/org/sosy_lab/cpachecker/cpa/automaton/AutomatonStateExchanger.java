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
package org.sosy_lab.cpachecker.cpa.automaton;

import java.util.Map;

import org.sosy_lab.cpachecker.exceptions.CPAException;

import com.google.common.collect.Maps;


public class AutomatonStateExchanger {

  private final Map<String, AutomatonInternalState> qualifiedAutomatonStateNameToInternalState;
  private final Map<String, ControlAutomatonCPA> nameToCPA;

  public AutomatonStateExchanger() {
    qualifiedAutomatonStateNameToInternalState = Maps.newHashMap();
    nameToCPA = Maps.newHashMap();
  }

  public boolean registerAutomaton(final ControlAutomatonCPA pAutomatonCPA) {
    final String prefix = pAutomatonCPA.getAutomaton().getName() + "::";
    String qualifiedName;

    if (nameToCPA.put(pAutomatonCPA.getAutomaton().getName(), pAutomatonCPA) != null) {
      return false;
    }

    for (AutomatonInternalState internal : pAutomatonCPA.getAutomaton().getStates()) {
      qualifiedName = prefix + internal.getName();
      if (qualifiedAutomatonStateNameToInternalState.put(qualifiedName, internal) != null) {
        return false;
      }
    }

    return true;
  }

  public AutomatonState replaceStateByStateInAutomatonOfSameInstance(final AutomatonState toReplace) throws CPAException {
    String qualifiedName = toReplace.getOwningAutomatonName()+"::" +toReplace.getInternalStateName();
    if (qualifiedAutomatonStateNameToInternalState.containsKey(qualifiedName)) {
      return AutomatonState.automatonStateFactory(toReplace.getVars(),
                 qualifiedAutomatonStateNameToInternalState.get(qualifiedName),
            nameToCPA.get(toReplace.getOwningAutomatonName()), toReplace.getAssumptions(), toReplace.getMatches(), toReplace.getFailedMatches(),
            toReplace.isTarget() ? toReplace.getViolatedPropertyDescription() : null); }
    throw new CPAException("Changing state failed, unknown state.");
  }

  public boolean considersAutomaton(final String pAutomatonName) {
    return nameToCPA.containsKey(pAutomatonName);
  }

}