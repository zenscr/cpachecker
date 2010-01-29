/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker. 
 *
 *  Copyright (C) 2007-2008  Dirk Beyer and Erkan Keremoglu.
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
 *    http://www.cs.sfu.ca/~dbeyer/CPAchecker/
 */
/**
 *
 */
package cpa.dominator.parametric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cfa.objectmodel.CFAEdge;
import cpa.common.interfaces.AbstractElement;
import cpa.common.interfaces.ConfigurableProgramAnalysis;
import cpa.common.interfaces.Precision;
import cpa.common.interfaces.TransferRelation;
import exceptions.CPATransferException;

/**
 * @author holzera
 *
 */
public class DominatorTransferRelation implements TransferRelation {

	private final DominatorDomain domain;
	private final ConfigurableProgramAnalysis cpa;

	public DominatorTransferRelation(DominatorDomain domain, ConfigurableProgramAnalysis cpa) {
		if (domain == null) {
			throw new IllegalArgumentException("domain is null!");
		}

		if (cpa == null) {
			throw new IllegalArgumentException("cpa is null!");
		}

		this.domain = domain;
		this.cpa = cpa;
	}

	@Override
	public Collection<DominatorElement> getAbstractSuccessors(
	    AbstractElement element, Precision prec, CFAEdge cfaEdge) throws CPATransferException {

	  assert element instanceof DominatorElement;
    
    DominatorElement dominatorElement = (DominatorElement)element;
    
    Collection<? extends AbstractElement> successorsOfDominatedElement = this.cpa.getTransferRelation().getAbstractSuccessors(dominatorElement.getDominatedElement(), prec, cfaEdge);
    
    Collection<DominatorElement> successors = new ArrayList<DominatorElement>(successorsOfDominatedElement.size());
    for (AbstractElement successorOfDominatedElement : successorsOfDominatedElement) {
      if (successorOfDominatedElement.equals(this.cpa.getAbstractDomain().getBottomElement())) {
        // don't need to return bottom, may just leave out this element from result set
        continue;
      }
      
      if (successorOfDominatedElement.equals(this.cpa.getAbstractDomain().getTopElement())) {
        successors.add(this.domain.getTopElement());
      
      } else {
        DominatorElement successor = new DominatorElement(successorOfDominatedElement, dominatorElement);
        successor.update(successorOfDominatedElement);
        successors.add(successor);
      }
    }
    
    return successors;	  
	}

  @Override
  public Collection<? extends AbstractElement> strengthen(AbstractElement element,
                         List<AbstractElement> otherElements, CFAEdge cfaEdge,
                         Precision precision) {    
    return null;
  }
}
