package org.sosy_lab.cpachecker.cfa.blocks.builder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import org.sosy_lab.cpachecker.cfa.objectmodel.BlankEdge;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFAEdge;
import org.sosy_lab.cpachecker.cfa.objectmodel.CFANode;

/**
 * Helper class to find the loop body using a given loopHead (loopHeads are identified by the parser).
 * @author dwonisch
 *
 */
public class LoopDetector {
  public Set<CFANode> detectLoopBody(CFANode loopHead) {
    assert loopHead.isLoopStart() : "Illegal argument for LoopDector::computeCachedSubtree: Given node has to be the start of a loop.";
    
    Set<CFANode> loopBody = new HashSet<CFANode>();
    loopBody.add(loopHead);
    
    for(int i = 0; i < loopHead.getNumEnteringEdges(); i++) {
      CFAEdge edge = loopHead.getEnteringEdge(i);
      if(isBackedge(edge, loopHead) && edge.getSuccessor() != edge.getPredecessor()) {
        addLoopBodyForEdge(edge, loopBody);
      }
    }
    
    return loopBody;
  }

  private void addLoopBodyForEdge(CFAEdge pEdge, Set<CFANode> pLoopBody) {
    CFANode startNode = pEdge.getPredecessor();
    Deque<CFANode> stack = new ArrayDeque<CFANode>();
    
    pLoopBody.add(startNode);
    stack.push(startNode);      
   
    while(!stack.isEmpty()) {
      CFANode node = stack.pop();
      for(int i = 0; i < node.getNumEnteringEdges(); i++) {
        CFANode nextNode = node.getEnteringEdge(i).getPredecessor();    
        if(node.getEnteringSummaryEdge() != null) {
          nextNode = node.getEnteringSummaryEdge().getPredecessor();
        }
        if(!pLoopBody.contains(nextNode)) {
          stack.push(nextNode);
          pLoopBody.add(nextNode);
        }
      }
    }    
  }

  private boolean isBackedge(CFAEdge pEdge, CFANode pNode) {
    return !(pEdge instanceof BlankEdge && ((BlankEdge)pEdge).getRawStatement().equals("while"));
  }
}
