/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.migration.instance;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.runtime.ActivityInstance;

/**
 * @author Thorben Lindhauer
 *
 */
public abstract class MigratingActivityInstance implements MigratingInstance {
  protected ActivityInstance activityInstance;
  // scope execution for actual scopes,
  // concurrent execution in case of non-scope activity with expanded tree
  protected ExecutionEntity scopeExecution;
  protected List<MigratingInstance> dependentInstances;
  protected ScopeImpl sourceScope;
  protected ScopeImpl targetScope;

  public abstract void detachState();

  public abstract void attachState(ExecutionEntity newScopeExecution);

  public void migrateDependentEntities() {
    // TODO: this might have to set the id also for a concurrent parent execution probably
    //   (in the case that no concurrent execution is recreated)
    scopeExecution.setProcessDefinition(targetScope.getProcessDefinition());

    if (dependentInstances != null) {
      for (MigratingInstance dependentInstance : dependentInstances) {
        dependentInstance.migrateDependentEntities();
      }
    }
  }

  protected ExecutionEntity resolveScopeExecution() {
    if (scopeExecution.getReplacedBy() != null) {
      return scopeExecution.resolveReplacedBy();
    }
    else {
      return scopeExecution;
    }
  }

  public abstract ExecutionEntity getFlowScopeExecution();

  public void addDependentInstance(MigratingInstance migratingInstance) {
    if (dependentInstances == null) {
      dependentInstances = new ArrayList<MigratingInstance>();
    }

    dependentInstances.add(migratingInstance);
  }

  public ActivityInstance getActivityInstance() {
    return activityInstance;
  }

  public ScopeImpl getSourceScope() {
    return sourceScope;
  }

  public ScopeImpl getTargetScope() {
    return targetScope;
  }
}
