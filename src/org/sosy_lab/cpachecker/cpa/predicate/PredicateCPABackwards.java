/*
 *  CPAchecker is a tool for configurable software verification.
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
package org.sosy_lab.cpachecker.cpa.predicate;

import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.cpachecker.cfa.CFA;
import org.sosy_lab.cpachecker.core.ShutdownNotifier;
import org.sosy_lab.cpachecker.core.reachedset.ReachedSetFactory;
import org.sosy_lab.cpachecker.exceptions.CPAException;
import org.sosy_lab.cpachecker.util.predicates.interfaces.PathFormulaManager;
import org.sosy_lab.cpachecker.util.predicates.interfaces.view.FormulaManagerView;
import org.sosy_lab.cpachecker.util.predicates.pathformula.PathFormulaManagerImpl;


public class PredicateCPABackwards extends PredicateCPA {

  protected PredicateCPABackwards(Configuration pConfig, LogManager pLogger, BlockOperator pBlk, CFA pCfa,
      ReachedSetFactory pReachedSetFactory, ShutdownNotifier pShutdownNotifier)
          throws InvalidConfigurationException, CPAException {

    super(pConfig, pLogger, pBlk, pCfa, pReachedSetFactory, pShutdownNotifier);
  }

  @Override
  protected PathFormulaManager createPathFormulaManagerImpl(FormulaManagerView pFormulaManager, CFA pCfa)
      throws InvalidConfigurationException {

    return new PathFormulaManagerImpl(pFormulaManager, config, logger, shutdownNotifier, pCfa, true);
  }

}