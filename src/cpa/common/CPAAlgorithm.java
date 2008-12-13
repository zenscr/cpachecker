package cpa.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import common.Pair;

import logging.CustomLogLevel;
import logging.LazyLogger;

import cmdline.CPAMain;

import cpa.common.interfaces.AbstractElementWithLocation;
import cpa.common.interfaces.ConfigurableProgramAnalysis;
import cpa.common.interfaces.MergeOperator;
import cpa.common.interfaces.Precision;
import cpa.common.interfaces.PrecisionAdjustment;
import cpa.common.interfaces.StopOperator;
import cpa.common.interfaces.TransferRelation;
import exceptions.CPATransferException;
import exceptions.ErrorReachedException;
import exceptions.RefinementNeededException;
import exceptions.CPAException;

public class CPAAlgorithm
{
    private final int GC_PERIOD = 100;
    private int gcCounter = 0;

	public Collection<AbstractElementWithLocation> CPA (ConfigurableProgramAnalysis cpa, AbstractElementWithLocation initialState,
	    Precision initialPrecision) throws CPAException
	{
	  List<Pair<AbstractElementWithLocation,Precision>> waitlist = new ArrayList<Pair<AbstractElementWithLocation,Precision>>();
		Collection<Pair<AbstractElementWithLocation,Precision>> reached = createReachedSet(cpa);
		Collection<AbstractElementWithLocation> simpleReached = new HashSet<AbstractElementWithLocation>();

		LazyLogger.log(CustomLogLevel.CentralCPAAlgorithmLevel, initialState,
		" added as initial state to CPA");

		waitlist.add(new Pair<AbstractElementWithLocation,Precision>(initialState, initialPrecision));
    reached.add(new Pair<AbstractElementWithLocation,Precision>(initialState, initialPrecision));
    simpleReached.add(initialState);

		TransferRelation transferRelation = cpa.getTransferRelation ();
		MergeOperator mergeOperator = cpa.getMergeOperator ();
		StopOperator stopOperator = cpa.getStopOperator ();
		PrecisionAdjustment precisionAdjustment = cpa.getPrecisionAdjustment();
		
		while (!waitlist.isEmpty ())
		{
			// AG - BFS or DFS, according to the configuration
		  Pair<AbstractElementWithLocation,Precision> e = choose(waitlist);
		  e = precisionAdjustment.prec(e.getFirst(), e.getSecond(), reached);
      AbstractElementWithLocation element = e.getFirst();
      Precision precision = e.getSecond();

      LazyLogger.log(CustomLogLevel.CentralCPAAlgorithmLevel, element,
          " with precision ", precision, " is popped from queue");
			List<AbstractElementWithLocation> successors = null;
			try {
				successors = transferRelation.getAllAbstractSuccessors (element, precision);
			} catch (ErrorReachedException err) {
				System.out.println("Reached error state! Message is:");
				System.out.println(err.toString());
				return simpleReached;
			} catch (RefinementNeededException re) {
				doRefinement(reached, waitlist, re.getReachableToUndo(),
						re.getToWaitlist());
				continue;
			} catch (CPATransferException e1) {
				e1.printStackTrace();
				assert(false); // should not happen
			}

			for (AbstractElementWithLocation successor : successors)
			{
        LazyLogger.log(CustomLogLevel.CentralCPAAlgorithmLevel,
            "successor of ", element, " --> ", successor);

				// AG as an optimization, we allow the mergeOperator to be null,
				// as a synonym of a trivial operator that never merges

				if (mergeOperator != null) {
	        List<Pair<AbstractElementWithLocation,Precision>> toRemove = new Vector<Pair<AbstractElementWithLocation,Precision>>();
	        List<AbstractElementWithLocation> toRemoveSimple = new Vector<AbstractElementWithLocation>();
	        List<Pair<AbstractElementWithLocation,Precision>> toAdd = new Vector<Pair<AbstractElementWithLocation,Precision>>();
	        List<AbstractElementWithLocation> toAddSimple = new Vector<AbstractElementWithLocation>();
	        
					for (Pair<AbstractElementWithLocation, Precision> reachedEntry : reached) {
					  AbstractElementWithLocation reachedElement = reachedEntry.getFirst();
					  AbstractElementWithLocation mergedElement = mergeOperator.merge(
	              successor, reachedElement, precision);
					  LazyLogger.log(CustomLogLevel.CentralCPAAlgorithmLevel,
	              " Merged ", successor, " and ", reachedElement, " --> ", mergedElement);
	          if (!mergedElement.equals(reachedElement)) {
	            LazyLogger.log(
	                CustomLogLevel.CentralCPAAlgorithmLevel,
	                "reached element ", reachedElement,
	                " is removed from queue and ", mergedElement,
	                " with precision ", precision, " is added to queue");
	            waitlist.remove(new Pair<AbstractElementWithLocation,Precision>(reachedElement, reachedEntry.getSecond()));
	            waitlist.add(new Pair<AbstractElementWithLocation,Precision>(mergedElement, precision));

	            toRemove.add(new Pair<AbstractElementWithLocation,Precision>(reachedElement, reachedEntry.getSecond()));
	            toRemoveSimple.add(reachedElement);
	            toAdd.add(new Pair<AbstractElementWithLocation,Precision>(mergedElement, precision));
	            toAddSimple.add(mergedElement);
	          }
					}
					reached.removeAll(toRemove);
	        simpleReached.removeAll(toRemoveSimple);
	        reached.addAll(toAdd);
	        simpleReached.addAll(toAddSimple);

//					int numReached = reached.size (); // Need to iterate this way to avoid concurrent mod exceptions

//					for (int reachedIdx = 0; reachedIdx < numReached; reachedIdx++)
//					{
//					AbstractElement reachedElement = reached.pollFirst ();
//					AbstractElement mergedElement = mergeOperator.merge (successor, reachedElement);
//					reached.addLast (mergedElement);

//					LazyLogger.log(CustomLogLevel.CentralCPAAlgorithmLevel,
//					" Merged ", successor, " and ",
//					reachedElement, " --> ", mergedElement);

//					if (!mergedElement.equals (reachedElement))
//					{
//					LazyLogger.log(CustomLogLevel.CentralCPAAlgorithmLevel,
//					"reached element ", reachedElement,
//					" is removed from queue",
//					" and ", mergedElement,
//					" is added to queue");
//					waitlist.remove (reachedElement);
//					waitlist.add (mergedElement);
//					}
//					}
				}

				if (!stopOperator.stop (successor, simpleReached, precision))
				{
					LazyLogger.log(CustomLogLevel.CentralCPAAlgorithmLevel,
							"No need to stop ", successor,
					" is added to queue");
					// end to the end

					waitlist.add(new Pair<AbstractElementWithLocation,Precision>(successor,precision));
          reached.add(new Pair<AbstractElementWithLocation,Precision>(successor,precision));
          simpleReached.add(successor);
				}
			}
			//CPACheckerStatistics.noOfReachedSet = reached.size();
		}

		return simpleReached;
	}

  private Pair<AbstractElementWithLocation,Precision> choose(List<Pair<AbstractElementWithLocation,Precision>> waitlist) {

    if(waitlist.size() == 1 || CPAMain.cpaConfig.getBooleanValue("analysis.bfs")){
      return waitlist.remove(0);
    } else if(CPAMain.cpaConfig.getBooleanValue("analysis.topSort")) {
      Pair<AbstractElementWithLocation,Precision> currentElement = waitlist.get(0);
      for(int i=1; i<waitlist.size(); i++){
        Pair<AbstractElementWithLocation,Precision> currentTempElement = waitlist.get(i);
        if(currentTempElement.getFirst().getLocationNode().getTopologicalSortId() >
            currentElement.getFirst().getLocationNode().getTopologicalSortId()){
          currentElement = currentTempElement;
        }
      }

      waitlist.remove(currentElement);
      return currentElement;
    } else {
      return waitlist.remove(waitlist.size()-1);
    }
  }
  
	private Collection<Pair<AbstractElementWithLocation,Precision>> createReachedSet(
			ConfigurableProgramAnalysis cpa) {
		// check whether the cpa provides a method for building a specialized
		// reached set. If not, just use a HashSet
		try {
		  Method meth = cpa.getClass().getDeclaredMethod("newReachedSet");
			
			return (Collection<Pair<AbstractElementWithLocation,Precision>>)meth.invoke(cpa);
		} catch (NoSuchMethodException e) {
			// ignore, this is not an error
		  
		} catch (Exception lException) {
		  lException.printStackTrace();
		  
		  System.exit(1);
		}
		
		return new HashSet<Pair<AbstractElementWithLocation,Precision>>();
	}

	private void doRefinement(Collection<Pair<AbstractElementWithLocation, Precision>> reached,
			List<Pair<AbstractElementWithLocation, Precision>> waitlist,
			Collection<AbstractElementWithLocation> reachableToUndo,
			Collection<AbstractElementWithLocation> toWaitlist) {
		LazyLogger.log(CustomLogLevel.SpecificCPALevel,
		"Performing refinement");
		// remove from reached all the elements in reachableToUndo
		Collection<Pair<AbstractElementWithLocation, Precision>> newreached =
			new LinkedList<Pair<AbstractElementWithLocation, Precision>>();
		for (Pair<AbstractElementWithLocation, Precision> e : reached) {
			if (!reachableToUndo.contains(e.getFirst())) {
				newreached.add(e);
			} else {
				LazyLogger.log(CustomLogLevel.SpecificCPALevel,
						"Removing element: ", e.getFirst(), " from reached");
				if (waitlist.remove(e)) {
					LazyLogger.log(CustomLogLevel.SpecificCPALevel,
							"Removing element: ", e.getFirst(),
					" also from waitlist");
				}
			}
		}
		reached.clear();
		reached.addAll(newreached);
		LazyLogger.log(CustomLogLevel.SpecificCPALevel,
				"Reached now is: ", newreached);
		// and add to the wait list all the elements in toWaitlist
		boolean useBfs = CPAMain.cpaConfig.getBooleanValue("analysis.bfs");
		for (AbstractElementWithLocation e : toWaitlist) {
			LazyLogger.log(CustomLogLevel.SpecificCPALevel,
					"Adding element: ", e, " to waitlist");
			// TODO null is not a proper precision ...
			if (useBfs) {
				// end to the end
				waitlist.add(new Pair<AbstractElementWithLocation, Precision>(e,null));
			} else {
				// at to the first index
				waitlist.add(0, new Pair<AbstractElementWithLocation, Precision>(e,null));
			}
		}
		LazyLogger.log(CustomLogLevel.SpecificCPALevel,
				"Waitlist now is: ", waitlist);
		LazyLogger.log(CustomLogLevel.SpecificCPALevel,
		"Refinement done");

        if ((++gcCounter % GC_PERIOD) == 0) {
            System.gc();
            gcCounter = 0;
        }
	}
}
