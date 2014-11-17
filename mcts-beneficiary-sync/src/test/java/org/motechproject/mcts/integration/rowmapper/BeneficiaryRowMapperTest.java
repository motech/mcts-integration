package org.motechproject.mcts.integration.rowmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.motechproject.mcts.integration.model.Beneficiary;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiaryRowMapperTest {

    @InjectMocks
    private BeneficiaryRowMapper beneficiaryRowMapper = new BeneficiaryRowMapper();

    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    @Before
    public void setUp() throws SQLException {

        Mockito.mock(ResultSet.class);
        doAnswer(new Answer<String>() {
            private int count = 0;
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                count++;
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(1, args.length);
                String arg = (String) args[0];
                assertNotNull(arg);
                if (count == 1) {
                    assertEquals("mcts_id", arg);
                    return "id";
                } else if (count == 2) {
                    assertEquals("mobile_number", arg);
                    return "998";
                }
                return null;
            }
        }).when(resultSet).getString(anyString());

        doAnswer(new Answer<Integer>() {
            private int count = 0;

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                count++;
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(1, args.length);
                String arg = (String) args[0];
                assertNotNull(arg);
                if (count == 1) {
                    assertEquals("mother_id", arg);
                    return 1;
                } else if (count == 2) {
                    assertEquals("service_type", arg);
                    return 2;
                } else if (count == 3) {
                    assertEquals("anc1_hblevel", arg);
                    return 5;
                } else if (count == 4) {
                    assertEquals("anc2_hblevel", arg);
                    return 6;
                } else if (count == 5) {
                    assertEquals("anc3_hblevel", arg);
                    return 7;
                } else if (count == 6) {
                    assertEquals("anc4_hblevel", arg);
                    return 8;
                }
                return null;
            }
        }).when(resultSet).getInt(anyString());

        doAnswer(new Answer<Date>() {

            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(1, args.length);
                String arg = (String) args[0];
                assertNotNull(arg);
                assertEquals("delivery_date", arg);
                return null;
            }
        }).when(resultSet).getDate(anyString());
    }

    @Test
    public void mapRow() throws SQLException {
        Beneficiary beneficiary = beneficiaryRowMapper.mapRow(resultSet, 1);

        assertNotNull(beneficiary);
        assertNotNull(beneficiary.getMctsPregnantMotherId());
        assertEquals(1, (int) beneficiary.getMctsPregnantMotherId());
        assertNotNull(beneficiary.getMctsId());
        assertEquals("id", beneficiary.getMctsId());
        assertNotNull(beneficiary.getServiceType());
        assertEquals(2, (int) beneficiary.getServiceType());
        assertNull(beneficiary.getServiceDeliveryDate());
        assertNotNull(beneficiary.getMobileNumber());
        assertEquals("998", beneficiary.getMobileNumber());
        assertNotNull(beneficiary.getAnc1HBLevel());
        assertEquals(5, (int) beneficiary.getAnc1HBLevel());
        assertNull(beneficiary.getAnc2HBLevel());
        assertNull(beneficiary.getAnc3HBLevel());
        assertNull(beneficiary.getAnc4HBLevel());

        verify(resultSet, times(6)).getInt(anyString());
        verify(resultSet, times(2)).getString(anyString());
        verify(resultSet).getDate(anyString());
    }

}
