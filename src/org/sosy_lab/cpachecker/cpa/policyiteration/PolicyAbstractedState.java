package org.sosy_lab.cpachecker.cpa.policyiteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.core.interfaces.FormulaReportingState;
import org.sosy_lab.cpachecker.cpa.policyiteration.congruence.CongruenceState;
import org.sosy_lab.cpachecker.util.predicates.interfaces.BooleanFormula;
import org.sosy_lab.cpachecker.util.predicates.interfaces.PathFormulaManager;
import org.sosy_lab.cpachecker.util.predicates.interfaces.view.FormulaManagerView;
import org.sosy_lab.cpachecker.util.predicates.pathformula.PathFormula;
import org.sosy_lab.cpachecker.util.predicates.pathformula.SSAMap;
import org.sosy_lab.cpachecker.util.predicates.pathformula.pointeraliasing.PointerTargetSet;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public final class PolicyAbstractedState extends PolicyState
      implements Iterable<Entry<Template, PolicyBound>>, FormulaReportingState {

  private final CongruenceState congruence;

  private final PolicyIterationManager manager;

  /**
   * Finite bounds for templates.
   */
  private final ImmutableMap<Template, PolicyBound> abstraction;

  /**
   * Expected starting {@link PointerTargetSet} and {@link SSAMap}.
   */
  private final SSAMap ssaMap;
  private final PointerTargetSet pointerTargetSet;

  /**
   * Uninstantiated predicate associated with a state.
   */
  private final BooleanFormula predicate;

  /**
   * Pointer to the latest version of the state associated with the given
   * location.
   */
  private transient Optional<PolicyAbstractedState> newVersion =
      Optional.absent();

  /**
   * If state A and state B can potentially get merged, they share the same
   * location.
   */
  private final int locationID;

  private PolicyAbstractedState(CFANode node,
      Map<Template, PolicyBound> pAbstraction,
      CongruenceState pCongruence,
      int pLocationID,
      PolicyIterationManager pManager, SSAMap pSsaMap,
      PointerTargetSet pPointerTargetSet, BooleanFormula pPredicate) {
    super(node);
    ssaMap = pSsaMap;
    pointerTargetSet = pPointerTargetSet;
    predicate = pPredicate;
    abstraction = ImmutableMap.copyOf(pAbstraction);
    congruence = pCongruence;
    locationID = pLocationID;
    manager = pManager;
  }

  public int getLocationID() {
    return locationID;
  }

  public CongruenceState getCongruence() {
    return congruence;
  }

  public void setNewVersion(PolicyAbstractedState pNewVersion) {
    newVersion = Optional.of(pNewVersion);
  }

  /**
   * @return latest version of this state found in the reached set.
   */
  public PolicyAbstractedState getLatestVersion() {
    PolicyAbstractedState latest = this;
    List<PolicyAbstractedState> toUpdate = new ArrayList<>();

    // Traverse the pointers up.
    while (latest.newVersion.isPresent()) {
      toUpdate.add(latest);
      latest = latest.newVersion.get();
    }

    // Update the pointers on the visited states.
    for (PolicyAbstractedState updated : toUpdate) {
      updated.newVersion = Optional.of(latest);
    }
    return latest;
  }

  public static PolicyAbstractedState of(
      Map<Template, PolicyBound> data,
      CFANode node,
      CongruenceState pCongruence,
      int pLocationID,
      PolicyIterationManager pManager,
      SSAMap pSSAMap,
      PointerTargetSet pPointerTargetSet,
      BooleanFormula pPredicate
  ) {
    return new PolicyAbstractedState(node, data,
        pCongruence, pLocationID, pManager, pSSAMap,
        pPointerTargetSet, pPredicate);
  }

  public PolicyAbstractedState updateAbstraction(
      Map<Template, PolicyBound> newAbstraction) {
    return new PolicyAbstractedState(getNode(),
        newAbstraction, congruence, locationID, manager, ssaMap,
        pointerTargetSet, predicate);
  }

  public PolicyAbstractedState withUpdates(
      Map<Template, PolicyBound> newAbstraction,
      CongruenceState newCongruence,
      BooleanFormula newPredicate
  ) {
    return new PolicyAbstractedState(getNode(),
        newAbstraction, newCongruence, locationID, manager, ssaMap,
        pointerTargetSet, newPredicate);
  }

  public PolicyAbstractedState updatePredicate(
      BooleanFormula newInvariant
  ) {
    return new PolicyAbstractedState(
        getNode(), abstraction, congruence, locationID, manager,
        ssaMap, pointerTargetSet, newInvariant);
  }

  public BooleanFormula getPredicate() {
    return predicate;
  }

  public PathFormula getPathFormula(FormulaManagerView fmgr) {
    return new PathFormula(
        fmgr.instantiate(predicate, ssaMap),
        ssaMap, pointerTargetSet, 1
    );
  }

  /**
   * @return {@link PolicyBound} for the given {@link Template}
   * <code>e</code> or an empty optional if it is unbounded.
   */
  public Optional<PolicyBound> getBound(Template e) {
    return Optional.fromNullable(abstraction.get(e));
  }

  /**
   * @return Empty abstracted state associated with {@code node}.
   */
  public static PolicyAbstractedState empty(CFANode node,
      SSAMap pSSAMap,
      PointerTargetSet pPointerTargetSet,
      BooleanFormula pPredicate,
      PolicyIterationManager pManager) {
    return PolicyAbstractedState.of(
        ImmutableMap.<Template, PolicyBound>of(), // abstraction
        node, // node
        CongruenceState.empty(),
        -1,
        pManager,
        pSSAMap,
        pPointerTargetSet,
        pPredicate
    );
  }

  @Override
  public boolean isAbstract() {
    return true;
  }

  @Override
  public String toDOTLabel() {
    return String.format(
        "(node=%s)%s%n %n %s %n",
        getNode(),
        (new PolicyDotWriter()).toDOTLabel(abstraction),
        predicate
    );
  }

  @Override
  public boolean shouldBeHighlighted() {
    return true;
  }

  @Override
  public String toString() {
    return String.format("(loc=%s)%s", locationID, abstraction);
  }

  @Override
  public Iterator<Entry<Template, PolicyBound>> iterator() {
    return abstraction.entrySet().iterator();
  }

  @Override
  public BooleanFormula getFormulaApproximation(FormulaManagerView fmgr, PathFormulaManager pfmgr) {
    BooleanFormula invariant = fmgr.getBooleanFormulaManager().and(
        manager.abstractStateToConstraints(fmgr, pfmgr, this)
    );
    return fmgr.uninstantiate(invariant);
  }
}
