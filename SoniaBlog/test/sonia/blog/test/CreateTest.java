/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.test;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sonia.blog.api.app.BlogContext;
import static org.junit.Assert.*;

/**
 *
 * @author sdorra
 */
public class CreateTest {

    public CreateTest() {
    }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownClass() throws Exception
  {
  }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void test()
    {
      BlogContext context = BlogContext.getInstance();
      EntityManager em = context.getEntityManager();
      
      
      List list = em.createQuery("from Entry").getResultList();
      
      for ( Object o : list ){
        System.out.println( o );
      }
      
      em.close();
    }
    
}