package mcts.integration.beneficiary.sync.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beneficiarySyncContext.xml"})
public class BaseRepositoryIT {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session session;

    @Before
    public void after() {
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() {
        session.close();
    }
}
