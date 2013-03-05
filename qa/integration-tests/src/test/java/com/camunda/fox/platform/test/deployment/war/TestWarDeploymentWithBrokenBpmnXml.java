package com.camunda.fox.platform.test.deployment.war;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.camunda.fox.platform.test.util.DeploymentHelper;
import com.camunda.fox.platform.test.util.TestContainer;

/**
 * In this test we make sure that if a user deploys a WAR file with a broken
 * .bpmn-XML file, the deployment fails.
 * 
 * @author Daniel Meyer
 * 
 */
@RunWith(Arquillian.class)
public class TestWarDeploymentWithBrokenBpmnXml {
  
  private static final String DEPLOYMENT = "deployment";

  @ArquillianResource
  private Deployer deployer;
  
  @Deployment(managed=false, name=DEPLOYMENT)
  public static WebArchive processArchive() {    
    
    WebArchive deployment = ShrinkWrap.create(WebArchive.class, "test.war")
      .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
      .addAsLibraries(DeploymentHelper.getFoxPlatformClient())
      .addAsResource("META-INF/processes.xml", "META-INF/processes.xml")
      .addAsResource("com/camunda/fox/platform/test/deployment/war/TestWarDeploymentWithBrokenBpmnXml.testXmlInvalid.bpmn20.xml");
    
    TestContainer.addContainerSpecificResources(deployment);
    
    return deployment;
  }
  
  @Test
  public void testXmlInvalid() {
    try {
      deployer.deploy(DEPLOYMENT);
      Assert.fail("exception expected");
    }catch (Exception e) {
      // expected
    } 
  }

}
