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
package org.camunda.bpm.engine.test;

import org.junit.Assert;
import org.camunda.bpm.engine.ArtifactFactory;
import org.camunda.bpm.engine.impl.DefaultArtifactFactory;
import org.junit.Test;

/**
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class DefaultArtifactFactoryTest {

  @Test
  public void testDefaultArtifactService() throws Exception {
    ArtifactFactory artifactFactory = new DefaultArtifactFactory();

    DummyArtifact artifact = artifactFactory.getArtifact(DummyArtifact.class);
    Assert.assertNotNull(artifact);
  }


  public static class DummyArtifact {
    // no content
  }
}
