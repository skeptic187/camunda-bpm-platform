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

import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;

/**
 * @author Thorben Lindhauer
 *
 */
public class MigratingScopeActivityInstance extends MigratingActivityInstance {

  @Override
  public void detachState() {
    ExecutionEntity currentScopeExecution = resolveScopeExecution();

    ExecutionEntity parentExecution = currentScopeExecution.getParent();
    ExecutionEntity parentScopeExecution = parentExecution.isConcurrent() ? parentExecution.getParent() : parentExecution;
    currentScopeExecution.setParent(null);

    if (parentExecution.isConcurrent()) {
      parentExecution.remove();
      parentScopeExecution.tryPruneLastConcurrentChild();
    }
  }

  @Override
  public void attachState(ExecutionEntity newScopeExecution) {
    ExecutionEntity currentScopeExecution = resolveScopeExecution();
    currentScopeExecution.setParent(newScopeExecution);

    if (targetScope.getActivityBehavior() instanceof CompositeActivityBehavior) {
      newScopeExecution.setActivityInstanceId(activityInstance.getId());
    }
  }

  @Override
  public ExecutionEntity getFlowScopeExecution() {
    ExecutionEntity parent = scopeExecution.getParent();
    return parent.isScope() ? parent : parent.getParent();
  }

}
